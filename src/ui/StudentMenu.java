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
                    updateStudent();
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

    private void registerStudent() {
        String name = ui.getInput("Digite o nome");
        String cpf = ui.getInput("Digite o CPF (apenas números)");
        String contact = ui.getInput("Digite o e-mail/telefone");

        LocalDate birthDate = null;

        while (birthDate == null) {
            String dateStr = ui.getInput("Digite a data de nascimento (dd/mm/aaaa)");
            try {
                birthDate = LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                ui.showError("Formato de data inválido. Use dd/mm/aaaa e tente novamente.");
            }
        }

        OperationResult<Student> result = fitManager.registerStudent(name, cpf, contact, birthDate);

        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }

    private void consultStudent() {
        String cpf = ui.getInput("Digite o CPF para consulta");

        OperationResult<Student> searchResult = fitManager.findStudentByCpf(cpf);

        if (!searchResult.isSuccess()) {
            ui.showError(searchResult.getMessage());
        } else {
            // Extrai o estudante do resultado validado
            Student student = searchResult.getData();

            StringBuilder dadosAluno = new StringBuilder();
            dadosAluno.append("--- Dados do Aluno ---\n\n");

            String status = student.isActive() ? "ATIVO" : "INATIVO";

            dadosAluno.append(String.format("Nome: %s | [%s]\n", student.getName(), status));
            dadosAluno.append(String.format("CPF: %s\n", student.getCpf()));
            dadosAluno.append(String.format("Idade: %d anos\n", student.calculateAge()));

            ui.showMessage(dadosAluno.toString());
        }
    }

    private void removeStudent() {
        String cpf = ui.getInput("Digite o CPF do aluno a ser inativado");

        OperationResult<Void> result = fitManager.removeStudent(cpf);

        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }

    private void listStudents() {
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

    private void updateStudent() {
        ui.showMessage("--- Editar Cadastro do Aluno ---");
        String cpf = ui.getInput("Digite o CPF do aluno que deseja editar");

        OperationResult<Student> searchResult = fitManager.findStudentByCpf(cpf);

        if (!searchResult.isSuccess()) {
            ui.showError(searchResult.getMessage());
            return;
        }

        // Extrai o aluno do resultado
        Student student = searchResult.getData();

        ui.showMessage("Dados atuais -> Nome: " + student.getName() + " | Contato: " + student.getContact());

        String newName = ui.getInput("Digite o novo nome (ou aperte Enter para manter o atual)");
        String newContact = ui.getInput("Digite o novo contato (ou aperte Enter para manter o atual)");

        if (newName.trim().isEmpty()) {
            newName = student.getName();
        }
        if (newContact.trim().isEmpty()) {
            newContact = student.getContact();
        }

        OperationResult<Void> result = fitManager.updateStudent(cpf, newName, newContact);

        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }
}