package ui;

import application.FitManager;
import application.OperationResult;
import domain.Enrollment;
import domain.Plan;
import domain.Student;

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

    // MÉTODOS DE CADA RELATÓRIO

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
        ui.showMessage("O Relatório Financeiro será implementado na última fase desta etapa!");
    }
}