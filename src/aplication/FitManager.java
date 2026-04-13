package application;

import domain.PlanType;
import domain.PaymentType;
import domain.Student;
import domain.Plan;
import domain.Enrollment;

import java.time.LocalDate;
import java.util.ArrayList;

public class FitManager {
    private StudentService studentService;
    private PlanService planService;
    private EnrollmentService enrollmentService;

    public FitManager() {
        this.studentService = new StudentService();
        this.planService = new PlanService();
        this.enrollmentService = new EnrollmentService();
    }

    public OperationResult registerPlan(String name, String description, PlanType type, int minDurationMonths, double pricePerMonth) {
        return this.planService.registerPlan(name, description, type, minDurationMonths, pricePerMonth);
    }

    public OperationResult updatePlanPrice(String name, double newPrice) {
        return this.planService.updatePrice(name, newPrice);
    }

    public OperationResult registerPayment(int enrollmentCode, double amount, PaymentType type, String description) {
        return this.enrollmentService.registerPayment(enrollmentCode, amount, type, description);
    }

    public OperationResult cancelEnrollment(int enrollmentCode) {
        return this.enrollmentService.cancel(enrollmentCode);
    }

    // Registra o aluno
    public OperationResult registerStudent(String name, String cpf, String contact, LocalDate birthDate) {
        return this.studentService.registerStudent(name, cpf, contact, birthDate);
    }

    // Busca estudante por CPF
    public Student findStudentByCpf(String cpf) {
        return this.studentService.findByCPF(cpf); // Usando o seu método findByCPF com maiúsculas!
    }

    // Busca plano por nome
    public Plan findPlanByName(String name) {
        return this.planService.findByName(name);
    }