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

    private void updateStudent() {
        ui.showMessage("--- Editar Cadastro do Aluno ---");
        String cpf = ui.getInput("Digite o CPF do aluno que deseja editar");

        Student student = fitManager.findStudentByCpf(cpf);

        if (student == null) {
            ui.showError("Nenhum aluno encontrado com este CPF.");
            return;
        }

        ui.showMessage("Dados atuais -> Nome: " + student.getName() + " | Contato: " + student.getContact());

        String newName = ui.getInput("Digite o novo nome (ou aperte Enter para manter o atual)");
        String newContact = ui.getInput("Digite o novo contato (ou aperte Enter para manter o atual)");

        if (newName.trim().isEmpty()) {
            newName = student.getName();
        }
        if (newContact.trim().isEmpty()) {
            newContact = student.getContact();
        }

        OperationResult result = fitManager.updateStudent(cpf, newName, newContact);

        if (result.isSuccess()) {
            ui.showMessage(result.getMessage());
        } else {
            ui.showError(result.getMessage());
        }
    }
}