package domain;

import java.time.LocalDate;
import java.time.Period;

public class Student {
    private String name;
    private String cpf;
    private String contact;
    private LocalDate birthDate;
    private boolean active;

    public Student(String name, String cpf, String contact, LocalDate birthDate) {
        this.name = name;
        this.cpf = cpf;
        this.contact = contact;
        this.birthDate = birthDate;
        this.active = true;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    // Calcula a idade dinamicamente
    public int calculateAge() {
        // A classe Period calcula a diferença entre duas datas.
        // LocalDate.now() pega a data exata de hoje.
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

    public static boolean validateCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }

        return cpf.matches("[0-9]+");
    }

    public boolean isActive() {
        return this.active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
