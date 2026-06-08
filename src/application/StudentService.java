package application;

import domain.Student;
import persistence.Repository;
import java.time.LocalDate;

public class StudentService extends Repository<Student> {

    public StudentService() {
        super();
    }

    public boolean cpfExists(String cpf) {
        for (Student student : this.elements) {
            if(student.getCpf().equals(cpf)){
                return true;
            }
        }
        return false;
    }

    public OperationResult<Student> registerStudent(String name, String cpf, String contact, LocalDate birthDate) {
        if (name == null || name.trim().isEmpty() || cpf == null || contact == null || contact.trim().isEmpty() || birthDate == null) {
            return new OperationResult<>(false, "Erro: Todos os campos são obrigatórios.");
        }

        if (!Student.validateCpf(cpf)) {
            return new OperationResult<>(false, "Erro: Formato de CPF inválido.");
        }

        if (cpfExists(cpf)) {
            return new OperationResult<>(false, "Erro: Este CPF já está cadastrado.");
        }

        Student newStudent = new Student(name, cpf, contact, birthDate);

        this.add(newStudent);

        return new OperationResult<>(true, "Aluno registrado com sucesso!", newStudent);
    }

    public OperationResult<Student> findByCPF(String cpf) {
        for (Student student : this.elements) {
            if(student.getCpf().equals(cpf)){
                return new OperationResult<>(true, "Aluno encontrado com sucesso.", student);
            }
        }
        return new OperationResult<>(false, "Erro: Aluno não encontrado.");
    }

    public OperationResult<Void> removeStudent(String cpf) {
        OperationResult<Student> searchResult = findByCPF(cpf);

        if(!searchResult.isSuccess()){
            return new OperationResult<>(false, searchResult.getMessage());
        }

        Student student = searchResult.getData();
        student.deactivate();

        return new OperationResult<>(true, "Matrícula do aluno desativada com sucesso!");
    }

    public OperationResult<Void> updateStudent(String cpf, String newName, String newContact) {
        OperationResult<Student> searchResult = findByCPF(cpf);

        if (!searchResult.isSuccess()) {
            return new OperationResult<>(false, searchResult.getMessage());
        }

        Student student = searchResult.getData();

        if (newName == null || newName.trim().isEmpty()) {
            return new OperationResult<>(false, "Erro: O nome do aluno não pode ficar em branco.");
        }
        if (newContact == null || newContact.trim().isEmpty()) {
            return new OperationResult<>(false, "Erro: O contato do aluno não pode ficar em branco.");
        }

        student.setName(newName);
        student.setContact(newContact);

        return new OperationResult<>(true, "Cadastro do aluno atualizado com sucesso!");
    }

    @Override
    public void save(String filePath) {
    }

    @Override
    public void load(String filePath) {
    }
}