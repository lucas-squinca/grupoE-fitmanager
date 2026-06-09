package application;

import domain.Student;
import persistence.Repository;
import exceptions.ValidationException;
import exceptions.PersistenceException;
import java.time.LocalDate;

public class StudentService extends Repository<Student> {

    public StudentService() {
        super();
    }

    public boolean cpfExists(String cpf) {
        for (Student student : this.elements) {
            if (student.getCpf().equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    public OperationResult<Student> registerStudent(String name, String cpf, String contact, LocalDate birthDate) {
        if (cpfExists(cpf)) {
            return new OperationResult<>(false, "Erro: Este CPF já está cadastrado.");
        }

        try {
            Student newStudent = new Student(name, cpf, contact, birthDate);
            this.add(newStudent);
            return new OperationResult<>(true, "Aluno registrado com sucesso!", newStudent);
        } catch (ValidationException e) {
            return new OperationResult<>(false, e.getMessage());
        }
    }

    public OperationResult<Student> findByCPF(String cpf) {
        for (Student student : this.elements) {
            if (student.getCpf().equals(cpf)) {
                return new OperationResult<>(true, "Aluno encontrado com sucesso.", student);
            }
        }
        return new OperationResult<>(false, "Erro: Aluno não encontrado.");
    }

    public OperationResult<Void> removeStudent(String cpf) {
        OperationResult<Student> searchResult = findByCPF(cpf);

        if (!searchResult.isSuccess()) {
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

        try {
            student.setName(newName);
            student.setContact(newContact);
            return new OperationResult<>(true, "Cadastro do aluno atualizado com sucesso!");
        } catch (ValidationException e) {
            return new OperationResult<>(false, e.getMessage());
        }
    }

    @Override
    public void save(String filePath) throws PersistenceException {
    }

    @Override
    public void load(String filePath) throws PersistenceException {
    }
}