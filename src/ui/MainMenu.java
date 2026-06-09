package ui;

import application.FitManager;
import application.OperationResult;

public class MainMenu {
    private UserInterface ui;
    private FitManager fitManager;

    public MainMenu(UserInterface ui, FitManager fitManager) {
        this.ui = ui;
        this.fitManager = fitManager;
    }

    public void start() {
        ui.showMessage("Iniciando o FitManager e carregando a base de dados...");
        OperationResult<Void> loadResult = fitManager.loadAllData();

        if (!loadResult.isSuccess()) {
            ui.showError(loadResult.getMessage());
            ui.showMessage("O sistema iniciará com uma base de dados vazia.");
        } else {
            ui.showMessage(loadResult.getMessage());
        }

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
                    ui.showMessage("Salvando alterações na base de dados...");
                    OperationResult<Void> saveResult = fitManager.saveAllData();

                    if (saveResult.isSuccess()) {
                        ui.showMessage(saveResult.getMessage());
                    } else {
                        ui.showError(saveResult.getMessage());
                    }

                    running = false;
                    ui.showMessage("Encerrando o sistema. Até logo!");
                    break;
                default:
                    ui.showError("Opção inválida. Escolha um número entre 1 e 5.");
            }
        }
    }
}