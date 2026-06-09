package ui;

import application.FitManager;
import application.OperationResult;
import domain.Enrollment;
import domain.Plan;
import domain.Student;
import domain.Payment;
import domain.PixPayment;
import domain.CreditCardPayment;
import domain.DebitCardPayment;
import domain.CashPayment;

import java.util.ArrayList;

public class ReportsMenu {
    private UserInterface ui;
    private FitManager fitManager;

    public ReportsMenu(UserInterface ui, FitManager fitManager) {
        this.ui = ui;
        this.fitManager = fitManager;
    }

    public void run() {
        String[] options = {
                "Relatório de Alunos",
                "Relatório de Planos",
                "Relatório de Matrículas",
                "Relatório Financeiro Mensal",
                "Voltar"
        };

        boolean running = true;

        while (running) {
            int choice = ui.showMenu("RELATÓRIOS E LISTAGENS", options);

            switch (choice) {
                case 1:
                    reportStudents();
                    break;
                case 2:
                    reportPlans();
                    break;
                case 3:
                    reportEnrollments();
                    break;
                case 4:
                    generateFinancialReport();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    ui.showError("Opção inválida.");
            }
        }
    }

    private void reportStudents() {
        OperationResult<ArrayList<Student>> listResult = fitManager.listStudents();
        ArrayList<Student> students = listResult.getData();

        if (students.isEmpty()) {
            ui.showMessage("Não existem alunos registrados no sistema.");
            return;
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("--- RELATÓRIO GERAL DE ALUNOS ---\n");

        for (Student s : students) {
            String status = s.isActive() ? "ATIVO" : "INATIVO";
            String linha = String.format("Nome: %-20s | CPF: %-14s | Idade: %02d anos | Status: %s\n",
                    s.getName(), s.getCpf(), s.calculateAge(), status);
            relatorio.append(linha);
        }

        ui.showMessage(relatorio.toString());
    }

    private void reportPlans() {
        OperationResult<ArrayList<Plan>> listResult = fitManager.listPlans();
        ArrayList<Plan> plans = listResult.getData();

        if (plans.isEmpty()) {
            ui.showMessage("Não existem planos registrados no sistema.");
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

    private void reportEnrollments() {
        OperationResult<ArrayList<Enrollment>> listResult = fitManager.listEnrollments();
        ArrayList<Enrollment> enrollments = listResult.getData();

        if (enrollments.isEmpty()) {
            ui.showMessage("Não existem matrículas registradas no sistema.");
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

    private void generateFinancialReport() {
        int month = 0;
        int year = 0;

        try {
            month = Integer.parseInt(ui.getInput("Digite o mês (1 a 12)"));
            if (month < 1 || month > 12) {
                ui.showError("Mês inválido. Deve ser entre 1 e 12.");
                return;
            }

            year = Integer.parseInt(ui.getInput("Digite o ano (ex: 2026)"));
        } catch (NumberFormatException e) {
            ui.showError("Entrada inválida. Digite apenas números inteiros.");
            return;
        }

        OperationResult<ArrayList<Enrollment>> listResult = fitManager.listEnrollments();
        ArrayList<Enrollment> enrollments = listResult.getData();

        double totalPix = 0.0;
        double totalCredit = 0.0;
        double totalDebit = 0.0;
        double totalCash = 0.0;
        double totalGeral = 0.0;
        int pagamentosEncontrados = 0;

        for (Enrollment e : enrollments) {
            for (Payment p : e.getPayments()) {
                if (p.getDate().getMonthValue() == month && p.getDate().getYear() == year) {

                    pagamentosEncontrados++;
                    double valor = p.getAmount();
                    totalGeral += valor;

                    if (p instanceof PixPayment) {
                        totalPix += valor;
                    } else if (p instanceof CreditCardPayment) {
                        totalCredit += valor;
                    } else if (p instanceof DebitCardPayment) {
                        totalDebit += valor;
                    } else if (p instanceof CashPayment) {
                        totalCash += valor;
                    }
                }
            }
        }

        if (pagamentosEncontrados == 0) {
            ui.showMessage(String.format("Nenhum pagamento encontrado para %02d/%d.", month, year));
            return;
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append(String.format("--- RELATÓRIO FINANCEIRO (%02d/%d) ---\n\n", month, year));
        relatorio.append(String.format("[PIX]                 R$ %8.2f\n", totalPix));
        relatorio.append(String.format("[Cartão de Crédito]   R$ %8.2f\n", totalCredit));
        relatorio.append(String.format("[Cartão de Débito]    R$ %8.2f\n", totalDebit));
        relatorio.append(String.format("[Dinheiro em Espécie] R$ %8.2f\n", totalCash));
        relatorio.append("-------------------------------------\n");
        relatorio.append(String.format("TOTAL ARRECADADO:     R$ %8.2f\n", totalGeral));
        relatorio.append(String.format("(%d pagamentos contabilizados)\n", pagamentosEncontrados));

        ui.showMessage(relatorio.toString());
    }
}
