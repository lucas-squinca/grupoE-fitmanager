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

        // Captura do Enum usando a sua própria interface robusta
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
                return; // Aborta e volta pro menu de planos
        }

        try {
            String durationStr = ui.getInput("Digite a duração mínima em meses");
            int minDuration = Integer.parseInt(durationStr);

            String priceStr = ui.getInput("Digite o preço por mês (ex: 99.90)");
            double pricePerMonth = Double.parseDouble(priceStr);

            // Delega para o FitManager
            OperationResult result = fitManager.registerPlan(name, description, type, minDuration, pricePerMonth);

            if (result.isSuccess()) {
                ui.showMessage(result.getMessage());
            } else {
                ui.showError(result.getMessage());
            }

        } catch (NumberFormatException e) {
            ui.showError("Erro: Duração deve ser um número inteiro e o preço deve ser numérico (use '.' para centavos).");
        }
    }

    private void consultPlan() {
        String name = ui.getInput("Digite o nome do plano para consulta");
        Plan plan = fitManager.findPlanByName(name);

        if (plan == null) {
            ui.showError("Nenhum plano encontrado com este nome.");
        } else {
            ui.showMessage("Dados do Plano:");
            System.out.println("Nome: " + plan.getName() + " | Tipo: " + plan.getType());
            System.out.println("Descrição: " + plan.getDescription());
            System.out.println("Duração Mínima: " + plan.getMinDurationMonths() + " meses");
            System.out.printf("Preço por mês: R$ %.2f\n", plan.getPricePerMonth());
        }
    }

    private void updatePrice() {
        String name = ui.getInput("Digite o nome do plano que deseja alterar");

        try {
            String newPriceStr = ui.getInput("Digite o novo preço (ex: 120.00)");
            double newPrice = Double.parseDouble(newPriceStr);

            OperationResult result = fitManager.updatePlanPrice(name, newPrice);

            if (result.isSuccess()) {
                ui.showMessage(result.getMessage());
            } else {
                ui.showError(result.getMessage());
            }
        } catch (NumberFormatException e) {
            ui.showError("Erro: O preço deve ser um valor numérico válido.");
        }
    }

    private void listPlans() {
        ArrayList<Plan> plans = fitManager.listPlans();

        if (plans.isEmpty()) {
            ui.showMessage("Nenhum plano cadastrado no sistema.");
            return;
        }

        ui.showMessage("Lista de Planos Disponíveis:");
        for (Plan p : plans) {
            System.out.printf("- %s (%s) | Duração min: %d meses | Preço: R$ %.2f\n",
                    p.getName(), p.getType(), p.getMinDurationMonths(), p.getPricePerMonth());
        }
    }
}