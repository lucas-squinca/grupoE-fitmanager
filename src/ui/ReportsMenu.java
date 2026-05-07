package ui;

import application.FitManager;
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
                    running = false;
                    break;
                default:
                    ui.showError("Opção inválida.");
            }
        }
    }

    // MÉTODOS DE CADA RELATÓRIO

    private void reportStudents() {
        ArrayList<Student> students = fitManager.listStudents();

        if (students.isEmpty()) {
            ui.showMessage("Não existem alunos registados no sistema.");
            return;
        }

        // 1. Cria o construtor de texto
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("--- RELATÓRIO GERAL DE ALUNOS ---\n");

        // 2. Acumula os dados linha por linha
        for (Student s : students) {
            String status = s.isActive() ? "ATIVO" : "INATIVO";

            // Usa String.format para montar o texto formatado e anexa no StringBuilder
            String linha = String.format("Nome: %-20s | CPF: %-14s | Idade: %02d anos | Status: %s\n",
                    s.getName(), s.getCpf(), s.calculateAge(), status);
            relatorio.append(linha);
        }

        // Manda a interface (seja ela Terminal ou JOptionPane) para exibir o texto no final.
        ui.showMessage(relatorio.toString());
    }

    private void reportPlans() {
        ArrayList<Plan> plans = fitManager.listPlans();

        if (plans.isEmpty()) {
            ui.showMessage("Não existem planos registados no sistema.");
            return;
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("--- RELATÓRIO GERAL DE PLANOS ---\n");
        for (Plan p : plans) {
            // Calcula o preço base total apenas para exibição no relatório
            double totalBase = p.calculateTotalPrice(p.getMinDurationMonths());
            String linha = String.format("Plano: %-15s (%-10s) | Mínimo: %02d meses | R$ %6.2f/mês | Total Base: R$ %7.2f\n",
                    p.getName(), p.getType(), p.getMinDurationMonths(), p.getPricePerMonth(), totalBase);

            relatorio.append(linha);
        }

        ui.showMessage(relatorio.toString());
    }

    private void reportEnrollments() {
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