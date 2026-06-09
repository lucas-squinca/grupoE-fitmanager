package ui;

import application.FitManager;
import application.OperationResult;
import domain.Enrollment;
import domain.Payment;
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

    private void performEnrollment() {
        ui.showMessage("--- Dados da Matrícula ---");
        String cpf = ui.getInput("Digite o CPF do aluno");
        String planName = ui.getInput("Digite o nome do plano");

        LocalDate startDate = null;
        while (startDate == null) {
            String dateStr = ui.getInput("Digite a data de início (dd/mm/aaaa)");
            try {
                startDate = LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                ui.showError("Formato de data inválido. Use dd/mm/aaaa e tente novamente.");
            }
        }

        int duration = 0;
        boolean validDuration = false;
        while (!validDuration) {
            try {
                String durationStr = ui.getInput("Digite a duração em meses");
                duration = Integer.parseInt(durationStr);
                validDuration = true;
            } catch (NumberFormatException e) {
                ui.showError("Erro: A duração deve ser um número inteiro. Tente novamente.");
            }
        }

        ui.showMessage("--- Dados do Pagamento Inicial ---");
        double amount = 0.0;
        boolean validAmount = false;
        while (!validAmount) {
            try {
                String amountStr = ui.getInput("Digite o valor do pagamento inicial (ex: 50.00)");
                amount = Double.parseDouble(amountStr);
                validAmount = true;
            } catch (NumberFormatException e) {
                ui.showError("Erro: O valor deve ser numérico (use '.' para centavos). Tente novamente.");
            }
        }

        String[] paymentOptions = {"PIX", "Cartão de Crédito", "Cartão de Débito", "Dinheiro"};
        int typeChoice = ui.showMenu("FORMA DE PAGAMENTO", paymentOptions);

        PaymentType paymentType = null;
        String pixKey = "";
        String cardLastDigits = "";
        int installments = 1;
        double amountReceived = 0.0;

        switch (typeChoice) {
            case 1:
                paymentType = PaymentType.PIX;
                pixKey = ui.getInput("Digite a chave PIX");
                break;
            case 2:
                paymentType = PaymentType.CREDIT_CARD;
                cardLastDigits = ui.getInput("Digite os últimos 4 dígitos do cartão");

                boolean validInstallments = false;
                while (!validInstallments) {
                    try {
                        installments = Integer.parseInt(ui.getInput("Digite o número de parcelas"));
                        validInstallments = true;
                    } catch (NumberFormatException e) {
                        ui.showError("Erro: O número de parcelas deve ser um número inteiro. Tente novamente.");
                    }
                }
                break;
            case 3:
                paymentType = PaymentType.DEBIT_CARD;
                cardLastDigits = ui.getInput("Digite os últimos 4 dígitos do cartão");
                break;
            case 4:
                paymentType = PaymentType.CASH;
                boolean validReceived = false;
                while (!validReceived) {
                    try {
                        amountReceived = Double.parseDouble(ui.getInput("Digite o valor recebido pelo aluno para cálculo de troco"));
                        validReceived = true;
                    } catch (NumberFormatException e) {
                        ui.showError("Erro: O valor recebido deve ser numérico. Tente novamente.");
                    }
                }
                break;
            default:
                ui.showError("Forma de pagamento inválida. Operação abortada.");
                return;
        }

        String description = ui.getInput("Digite uma descrição (ex: 'Mensalidade 1')");

        OperationResult<Enrollment> result = fitManager.enrollStudent(cpf, planName, startDate, duration, amount, paymentType, description, pixKey, cardLastDigits, installments, amountReceived);

        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }

    private void registerPayment() {
        int code = 0;
        boolean validCode = false;
        while (!validCode) {
            try {
                String codeStr = ui.getInput("Digite o código da matrícula");
                code = Integer.parseInt(codeStr);
                validCode = true;
            } catch (NumberFormatException e) {
                ui.showError("Erro: O código da matrícula deve ser um número inteiro. Tente novamente.");
            }
        }

        double amount = 0.0;
        boolean validAmount = false;
        while (!validAmount) {
            try {
                String amountStr = ui.getInput("Digite o valor do pagamento (ex: 100.00)");
                amount = Double.parseDouble(amountStr);
                validAmount = true;
            } catch (NumberFormatException e) {
                ui.showError("Erro: O valor deve ser numérico. Tente novamente.");
            }
        }

        String[] paymentOptions = {"PIX", "Cartão de Crédito", "Cartão de Débito", "Dinheiro"};
        int typeChoice = ui.showMenu("FORMA DE PAGAMENTO", paymentOptions);

        PaymentType paymentType = null;
        String pixKey = "";
        String cardLastDigits = "";
        int installments = 1;
        double amountReceived = 0.0;

        switch (typeChoice) {
            case 1:
                paymentType = PaymentType.PIX;
                pixKey = ui.getInput("Digite a chave PIX");
                break;
            case 2:
                paymentType = PaymentType.CREDIT_CARD;
                cardLastDigits = ui.getInput("Digite os últimos 4 dígitos do cartão");

                boolean validInstallments = false;
                while (!validInstallments) {
                    try {
                        installments = Integer.parseInt(ui.getInput("Digite o número de parcelas"));
                        validInstallments = true;
                    } catch (NumberFormatException e) {
                        ui.showError("Erro: O número de parcelas deve ser inteiro.");
                    }
                }
                break;
            case 3:
                paymentType = PaymentType.DEBIT_CARD;
                cardLastDigits = ui.getInput("Digite os últimos 4 dígitos do cartão");
                break;
            case 4:
                paymentType = PaymentType.CASH;
                boolean validReceived = false;
                while (!validReceived) {
                    try {
                        amountReceived = Double.parseDouble(ui.getInput("Digite o valor recebido"));
                        validReceived = true;
                    } catch (NumberFormatException e) {
                        ui.showError("Erro: O valor recebido deve ser numérico.");
                    }
                }
                break;
            default:
                ui.showError("Forma de pagamento inválida.");
                return;
        }

        String description = ui.getInput("Digite uma descrição");

        OperationResult<Payment> result = fitManager.registerPayment(code, amount, paymentType, description, pixKey, cardLastDigits, installments, amountReceived);

        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }

    private void cancelEnrollment() {
        int code = 0;
        boolean validCode = false;
        while (!validCode) {
            try {
                String codeStr = ui.getInput("Digite o código da matrícula a ser cancelada");
                code = Integer.parseInt(codeStr);
                validCode = true;
            } catch (NumberFormatException e) {
                ui.showError("Código inválido. Digite um número inteiro.");
            }
        }

        OperationResult<Void> result = fitManager.cancelEnrollment(code);

        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }

    private void consultActiveEnrollment() {
        String cpf = ui.getInput("Digite o CPF do aluno");

        OperationResult<Enrollment> searchResult = fitManager.findActiveEnrollment(cpf);

        if (!searchResult.isSuccess()) {
            ui.showError(searchResult.getMessage());
        } else {
            Enrollment enrollment = searchResult.getData();

            StringBuilder dadosMatricula = new StringBuilder();
            dadosMatricula.append("--- Matrícula Ativa Encontrada ---\n\n");

            dadosMatricula.append(String.format("Código: %d\n", enrollment.getCode()));
            dadosMatricula.append(String.format("Plano: %s\n", enrollment.getPlan().getName()));
            dadosMatricula.append(String.format("Período: %s até %s\n", enrollment.getStartDate(), enrollment.getEndDate()));
            dadosMatricula.append(String.format("Valor Total: R$ %.2f\n", enrollment.getTotalPrice()));
            dadosMatricula.append(String.format("Saldo Pendente: R$ %.2f\n", enrollment.calculateBalance()));

            ui.showMessage(dadosMatricula.toString());
        }
    }

    private void listHistory() {
        OperationResult<ArrayList<Enrollment>> listResult = fitManager.listEnrollments();
        ArrayList<Enrollment> enrollments = listResult.getData();

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