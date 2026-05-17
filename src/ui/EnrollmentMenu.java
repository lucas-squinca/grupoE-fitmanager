package ui;

import application.FitManager;
import application.OperationResult;
import domain.Enrollment;
import domain.PaymentType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class EnrollmentMenu {
    private UserInterface ui;
    private FitManager fitManager;
    private DateTimeFormatter dateFormatter;

    public EnrollmentMenu(UserInterface ui, FitManager fitManager) {
        this.ui = ui;
        this.fitManager = fitManager;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public void run() {
        // Opções exatas exigidas no PDF
        String[] options = {
                "Realizar matrícula",
                "Registrar pagamento",
                "Cancelar matrícula",
                "Consultar matrícula ativa",
                "Listar histórico",
                "Voltar"
        };

        boolean running = true;

        while (running) {
            int choice = ui.showMenu("GERENCIAR MATRÍCULAS", options);

            switch (choice) {
                case 1:
                    performEnrollment();
                    break;
                case 2:
                    registerPayment();
                    break;
                case 3:
                    cancelEnrollment();
                    break;
                case 4:
                    consultActiveEnrollment();
                    break;
                case 5:
                    listHistory();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    ui.showError("Opção inválida.");
            }
        }
    }

    // MÉTODOS DE CADA OPÇÃO DO MENU

    private void performEnrollment() {
        ui.showMessage("--- Dados da Matrícula ---");
        String cpf = ui.getInput("Digite o CPF do aluno");
        String planName = ui.getInput("Digite o nome do plano");
        String dateStr = ui.getInput("Digite a data de início (dd/mm/aaaa)");

        LocalDate startDate = null;
        try {
            startDate = LocalDate.parse(dateStr, dateFormatter);

            String durationStr = ui.getInput("Digite a duração em meses");
            int duration = Integer.parseInt(durationStr);

            ui.showMessage("--- Dados do Pagamento Inicial ---");
            String amountStr = ui.getInput("Digite o valor do pagamento inicial (ex: 50.00)");
            double amount = Double.parseDouble(amountStr);

            // Reutiliza o showMenu para escolher o PaymentType com segurança
            String[] paymentOptions = {"PIX", "Cartão de Crédito", "Cartão de Débito", "Dinheiro"};
            int typeChoice = ui.showMenu("FORMA DE PAGAMENTO", paymentOptions);

            PaymentType paymentType = null;
            switch (typeChoice) {
                case 1: paymentType = PaymentType.PIX; break;
                case 2: paymentType = PaymentType.CREDIT_CARD; break;
                case 3: paymentType = PaymentType.DEBIT_CARD; break;
                case 4: paymentType = PaymentType.CASH; break;
                default:
                    ui.showError("Forma de pagamento inválida. Operação abortada.");
                    return;
            }

            String description = ui.getInput("Digite uma descrição (ex: 'Mensalidade 1')");

            // Delega tudo de uma vez para o FitManager coordenar
            OperationResult result = fitManager.enrollStudent(cpf, planName, startDate, duration, amount, paymentType, description);

            if (result.isSuccess()) {
                ui.showMessage(result.getMessage());
            } else {
                ui.showError(result.getMessage());
            }

        } catch (DateTimeParseException e) {
            ui.showError("Formato de data inválido. Use dd/mm/aaaa.");
        } catch (NumberFormatException e) {
            ui.showError("Valor numérico inválido para duração ou pagamento.");
        }
    }

    private void registerPayment() {
        try {
            String codeStr = ui.getInput("Digite o código da matrícula");
            int code = Integer.parseInt(codeStr);

            String amountStr = ui.getInput("Digite o valor do pagamento (ex: 100.00)");
            double amount = Double.parseDouble(amountStr);

            String[] paymentOptions = {"PIX", "Cartão de Crédito", "Cartão de Débito", "Dinheiro"};
            int typeChoice = ui.showMenu("FORMA DE PAGAMENTO", paymentOptions);

            PaymentType paymentType = null;
            switch (typeChoice) {
                case 1: paymentType = PaymentType.PIX; break;
                case 2: paymentType = PaymentType.CREDIT_CARD; break;
                case 3: paymentType = PaymentType.DEBIT_CARD; break;
                case 4: paymentType = PaymentType.CASH; break;
                default:
                    ui.showError("Forma de pagamento inválida.");
                    return;
            }

            String description = ui.getInput("Digite uma descrição");

            OperationResult result = fitManager.registerPayment(code, amount, paymentType, description);

            if (result.isSuccess()) {
                ui.showMessage(result.getMessage());
            } else {
                ui.showError(result.getMessage());
            }

        } catch (NumberFormatException e) {
            ui.showError("Entrada inválida. Código e valor devem ser numéricos.");
        }
    }

    private void cancelEnrollment() {
        try {
            String codeStr = ui.getInput("Digite o código da matrícula a ser cancelada");
            int code = Integer.parseInt(codeStr);

            OperationResult result = fitManager.cancelEnrollment(code);

            if (result.isSuccess()) {
                ui.showMessage(result.getMessage());
            } else {
                ui.showError(result.getMessage());
            }
        } catch (NumberFormatException e) {
            ui.showError("Código inválido. Digite um número.");
        }
    }

    private void consultActiveEnrollment() {
        String cpf = ui.getInput("Digite o CPF do aluno");
        Enrollment enrollment = fitManager.findActiveEnrollment(cpf);

        if (enrollment == null) {
            ui.showError("Nenhuma matrícula ativa encontrada para este aluno.");
        } else {
            ui.showMessage("Matrícula Ativa Encontrada:");
            System.out.println("Código: " + enrollment.getCode());
            System.out.println("Plano: " + enrollment.getPlan().getName());
            System.out.println("Período: " + enrollment.getStartDate() + " até " + enrollment.getEndDate());
            System.out.printf("Valor Total: R$ %.2f\n", enrollment.getTotalPrice());
            System.out.printf("Saldo Pendente: R$ %.2f\n", enrollment.calculateBalance());
        }
    }

    private void listHistory() {
        ArrayList<Enrollment> enrollments = fitManager.listEnrollments();

        if (enrollments.isEmpty()) {
            ui.showMessage("Não existem matrículas registadas no sistema.");
            return;
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("--- RELATÓRIO GERAL DE MATRÍCULAS ---\n");

        for (Enrollment e : enrollments) {
            String linha = String.format("Cód: %04d | Aluno: %-15s | Plano: %-10s | Status: %-9s | Saldo: R$ %7.2f\n", e.getCode(), e.getStudent().getName(), e.getPlan().getName(), e.getStatus(), e.calculateBalance());

            relatorio.append(linha);
        }

        ui.showMessage(relatorio.toString());
    }
}