package application;

import domain.Student;
import java.time.LocalDate;
import java.util.ArrayList;

public class StudentService {
    private ArrayList<Student> students;

    public StudentService() {
        this.students = new ArrayList<>();
    }

    public boolean cpfExists(String cpf) {
        for (Student student : students) {
            if(student.getCpf().equals(cpf)){
                return true;
            }
        }
        return false;
    }

    public OperationResult registerStudent(String name, String cpf, String contact, LocalDate birthDate) {
        // 1. Validação: Campos obrigatórios não podem ser nulos ou vazios
        if (name == null || name.trim().isEmpty() || cpf == null || contact == null || contact.trim().isEmpty() || birthDate == null) {
            return new OperationResult(false, "Erro: Todos os campos são obrigatórios.");
        }

        // 2. Validação: Formato do CPF
        if (!Student.validateCpf(cpf)) {
            return new OperationResult(false, "Erro: Formato de CPF inválido.");
        }

        // 3. Validação: CPF deve ser único no sistema
        if (cpfExists(cpf)) {
            return new OperationResult(false, "Erro: Este CPF já está cadastrado.");
        }

        // 4. Sucesso: Se passou por todas as validações, cria o aluno e guarda na lista
        Student newStudent = new Student(name, cpf, contact, birthDate);
        this.students.add(newStudent);

        return new OperationResult(true, "Aluno registrado com sucesso!", newStudent);
    }

    public Student findByCPF(String cpf) {
        for (Student student : students) {
            if(student.getCpf().equals(cpf)){
                return student;
            }
        }
        return null;
    }

    public OperationResult removeStudent(String cpf) {
        Student student = findByCPF(cpf);

        if(student == null){
            return new OperationResult(false, "Erro: Aluno não encontrado");
        }

        student.deactivate();
        return new OperationResult(true, "Matricula do aluno desativada com sucesso!");
    }

    public OperationResult updateStudent(String cpf, String newName, String newContact) {
        Student student = findByCPF(cpf);

        if (student == null) {
            return new OperationResult(false, "Erro: Aluno não encontrado.");
        }
        if (newName == null || newName.trim().isEmpty()) {
            return new OperationResult(false, "Erro: O nome do aluno não pode ficar em branco.");
        }
        if (newContact == null || newContact.trim().isEmpty()) {
            return new OperationResult(false, "Erro: O contato do aluno não pode ficar em branco.");
        }
        
        student.setName(newName);
        student.setContact(newContact);

        return new OperationResult(true, "Cadastro do aluno atualizado com sucesso!");
    }

    public ArrayList<Student> listStudents() {
        return this.students;
    }

}
