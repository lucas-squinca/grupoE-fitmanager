package ui;

import application.FitManager;
import application.OperationResult;
import domain.Student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class StudentMenu {
    private UserInterface ui;
    private FitManager fitManager;
    private DateTimeFormatter dateFormatter;

    public StudentMenu(UserInterface ui, FitManager fitManager) {
        this.ui = ui;
        this.fitManager = fitManager;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public void run() {
        String[] options = {
                "Cadastrar novo aluno",
                "Consultar por CPF",
                "Editar cadastro",
                "Excluir aluno",
                "Listar todos",
                "Voltar"
        };

        boolean running = true;

        while (running) {
            int choice = ui.showMenu("GERENCIAR ALUNOS", options);

            switch (choice) {
                case 1:
                    registerStudent();
                    break;
                case 2:
                    consultStudent();
                    break;
                case 3:
                    ui.showMessage("Funcionalidade de edição será implementada em breve.");
                    break;
                case 4:
                    removeStudent();
                    break;
                case 5:
                    listStudents();
                    break;
                case 6:
                    running = false; // Sai do loop e volta pro MainMenu
                    break;
                default:
                    ui.showError("Opção inválida.");
            }
        }
    }

    // MÉTODOS DE CADA OPÇÃO DO MENU

    private void registerStudent() {
        // 1. O Menu coleta os dados
        String name = ui.getInput("Digite o nome");
        String cpf = ui.getInput("Digite o CPF (apenas números)");
        String contact = ui.getInput("Digite o e-mail/telefone");
        String dateStr = ui.getInput("Digite a data de nascimento (dd/mm/aaaa)");

        LocalDate birthDate = null;
        try {
            // Tenta converter a string digitada para uma data real
            birthDate = LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            ui.showError("Formato de data inválido. Use dd/mm/aaaa.");
            return; // Aborta o cadastro e volta pro menu
        }

        // 2. O FitManager coordena
        OperationResult result = fitManager.registerStudent(name, cpf, contact, birthDate);

        // 3. O Menu exibe o resultado
        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }

    private void consultStudent() {
        String cpf = ui.getInput("Digite o CPF para consulta");
        Student student = fitManager.findStudentByCpf(cpf);

        if (student == null) {
            ui.showError("Nenhum aluno encontrado com este CPF.");
        } else {
            // Se achou, mostra os dados
            ui.showMessage("Dados do Aluno:");
            System.out.println("Nome: " + student.getName() + " (Ativo: " + student.isActive() + ")");
            System.out.println("CPF: " + student.getCpf());
            System.out.println("Idade: " + student.calculateAge() + " anos");
        }
    }

    private void removeStudent() {
        String cpf = ui.getInput("Digite o CPF do aluno a ser inativado");

        // Repassa para o FitManager tentar excluir
        OperationResult result = fitManager.removeStudent(cpf);

        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }

    private void listStudents() {
        ArrayList<Student> students = fitManager.listStudents();

        if (students.isEmpty()) {
            ui.showMessage("Nenhum aluno cadastrado no sistema.");
            return;
        }

        ui.showMessage("Lista de Alunos:");
        for (Student s : students) {
            String status = s.isActive() ? "ATIVO" : "INATIVO";
            System.out.println("- " + s.getName() + " | CPF: " + s.getCpf() + " | [" + status + "]");
        }
    }
}