package ui;

import application.FitManager;
import application.OperationResult;
import domain.Plan;
import domain.PlanType;

import java.util.ArrayList;

public class PlanMenu {
    private UserInterface ui;
    private FitManager fitManager;

    public PlanMenu(UserInterface ui, FitManager fitManager) {
        this.ui = ui;
        this.fitManager = fitManager;
    }

    public void run() {
        String[] options = {
                "Cadastrar novo plano",
                "Consultar por nome",
                "Alterar preço",
                "Listar todos",
                "Voltar"
        };

        boolean running = true;

        while (running) {
            int choice = ui.showMenu("GERENCIAR PLANOS", options);

            switch (choice) {
                case 1:
                    registerPlan();
                    break;
                case 2:
                    consultPlan();
                    break;
                case 3:
                    updatePrice();
                    break;
                case 4:
                    listPlans();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    ui.showError("Opção inválida.");
            }
        }
    }

    // MÉTODOS DE CADA OPÇÃO DO MENU

    private void registerPlan() {
        String name = ui.getInput("Digite o nome do plano");
        String description = ui.getInput("Digite a descrição");

        String[] typeOptions = {"Mensal", "Trimestral", "Semestral", "Anual"};
        int typeChoice = ui.showMenu("SELECIONE O TIPO", typeOptions);

        PlanType type = null;
        switch (typeChoice) {
            case 1: type = PlanType.MONTHLY; break;
            case 2: type = PlanType.QUARTERLY; break;
            case 3: type = PlanType.SEMI_ANNUAL; break;
            case 4: type = PlanType.ANNUAL; break;
            default:
                ui.showError("Opção de tipo inválida. Operação cancelada.");
                return;
        }

        int minDuration = -1;
        double pricePerMonth = -1.0;

        boolean validDuration = false;
        while (!validDuration) {
            try {
                String durationStr = ui.getInput("Digite a duração mínima em meses");
                minDuration = Integer.parseInt(durationStr);
                validDuration = true;
            } catch (NumberFormatException e) {
                ui.showError("Erro: A duração deve ser um número inteiro. Tente novamente.");
            }
        }

        boolean validPrice = false;
        while (!validPrice) {
            try {
                String priceStr = ui.getInput("Digite o preço por mês (ex: 99.90)");
                pricePerMonth = Double.parseDouble(priceStr);
                validPrice = true;
            } catch (NumberFormatException e) {
                ui.showError("Erro: O preço deve ser um valor numérico válido (use '.' para centavos). Tente novamente.");
            }
        }

        OperationResult<Plan> result = fitManager.registerPlan(name, description, type, minDuration, pricePerMonth);

        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }

    private void consultPlan() {
        String name = ui.getInput("Digite o nome do plano para consulta");

        OperationResult<Plan> searchResult = fitManager.findPlanByName(name);

        if (!searchResult.isSuccess()) {
            ui.showError(searchResult.getMessage());
        } else {
            // Extrai o plano do resultado
            Plan plan = searchResult.getData();

            StringBuilder dadosPlano = new StringBuilder();
            dadosPlano.append("--- Dados do Plano ---\n\n");

            String tipoPlano = plan.getClass().getSimpleName();

            dadosPlano.append(String.format("Nome: %s | Tipo: %s\n", plan.getName(), tipoPlano));
            dadosPlano.append(String.format("Descrição: %s\n", plan.getDescription()));
            dadosPlano.append(String.format("Duração Mínima: %d meses\n", plan.getMinDurationMonths()));
            dadosPlano.append(String.format("Preço por mês: R$ %.2f\n", plan.getPricePerMonth()));

            ui.showMessage(dadosPlano.toString());
        }
    }

    private void updatePrice() {
        String name = ui.getInput("Digite o nome do plano que deseja alterar");

        double newPrice = -1.0;

        boolean validPrice = false;
        while (!validPrice) {
            try {
                String newPriceStr = ui.getInput("Digite o novo preço (ex: 120.00)");
                newPrice = Double.parseDouble(newPriceStr);
                validPrice = true;
            } catch (NumberFormatException e) {
                ui.showError("Erro: O preço deve ser um valor numérico válido. Tente novamente.");
            }
        }

        OperationResult<Void> result = fitManager.updatePlanPrice(name, newPrice);

        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }

    private void listPlans() {
        OperationResult<ArrayList<Plan>> listResult = fitManager.listPlans();
        ArrayList<Plan> plans = listResult.getData();

        if (plans.isEmpty()) {
            ui.showMessage("Não existem planos registados no sistema.");
            return;
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("--- RELATÓRIO GERAL DE PLANOS ---\n");
        for (Plan p : plans) {
            double totalBase = p.calculateTotalPrice(p.getMinDurationMonths());
            String linha = String.format("Plano: %-15s (%-10s) | Mínimo: %02d meses | R$ %6.2f/mês | Total Base: R$ %7.2f\n",
                    p.getName(), p.getType(), p.getMinDurationMonths(), p.getPricePerMonth(), totalBase);

            relatorio.append(linha);
        }

        ui.showMessage(relatorio.toString());
    }
}