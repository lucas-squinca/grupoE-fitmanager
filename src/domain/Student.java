package domain;

import exceptions.ValidationException;
import java.time.LocalDate;
import java.time.Period;
import java.io.Serializable;

public class Student implements Serializable{
    private String name;
    private String cpf;
    private String contact;
    private LocalDate birthDate;
    private boolean active;

    public Student(String name, String cpf, String contact, LocalDate birthDate) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Erro Crítico: O nome do aluno não pode ser vazio.");
        }
        if (cpf == null || !validateCpf(cpf)) {
            throw new ValidationException("Erro Crítico: Formato de CPF inválido.");
        }
        if (contact == null || contact.trim().isEmpty()) {
            throw new ValidationException("Erro Crítico: O contato não pode ser vazio.");
        }
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Erro Crítico: Data de nascimento inválida (nula ou no futuro).");
        }

        this.name = name;
        this.cpf = cpf;
        this.contact = contact;
        this.birthDate = birthDate;
        this.active = true;
    }

    public String getName() { return name; }
    public String getCpf() { return cpf; }
    public String getContact() { return contact; }
    public LocalDate getBirthDate() { return birthDate; }
    public boolean isActive() { return active; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Erro Crítico: O nome do aluno não pode ficar em branco.");
        }
        this.name = name;
    }

    public void setContact(String contact) {
        if (contact == null || contact.trim().isEmpty()) {
            throw new ValidationException("Erro Crítico: O contato do aluno não pode ficar em branco.");
        }
        this.contact = contact;
    }

    public void deactivate() {
        this.active = false;
    }

    public int calculateAge() {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public static boolean validateCpf(String cpf) {
        if (cpf == null) return false;
        String cleanCpf = cpf.replaceAll("[^0-9]", "");
        return cleanCpf.length() == 11;
    }
}