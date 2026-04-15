package ui;

import application.FitManager;

public class MainMenu {
    private UserInterface ui;
    private FitManager fitManager;

    public MainMenu(UserInterface ui, FitManager fitManager) {
        this.ui = ui;
        this.fitManager = fitManager;
    }

    public void start() {
        String[] options = {
                "Gerenciar alunos",
                "Gerenciar planos",
                "Gerenciar matrículas",
                "Relatórios / listagens",
                "Sair"
        };

        boolean running = true;

        while (running) {
            int choice = ui.showMenu("FITMANAGER", options);

            switch (choice) {
                case 1:
                    StudentMenu studentMenu = new StudentMenu(ui, fitManager);
                    studentMenu.run();
                    break;
                case 2:
                    PlanMenu planMenu = new PlanMenu(ui, fitManager);
                    planMenu.run();
                    break;
                case 3:
                    EnrollmentMenu enrollmentMenu = new EnrollmentMenu(ui, fitManager);
                    enrollmentMenu.run();
                    break;
                case 4:
                    ReportsMenu reportsMenu = new ReportsMenu(ui, fitManager);
                    reportsMenu.run();
                    break;

                case 5:
                    running = false;
                    ui.showMessage("Encerrando o sistema. Até logo!");
                    break;
                default:
                    ui.showError("Opção inválida. Escolha um número entre 1 e 5.");
            }
        }
    }
}