package application;

import domain.PlanType;
import domain.PaymentType;
import domain.Student;
import domain.Plan;
import domain.Enrollment;
import domain.Payment;

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

    public OperationResult<Plan> registerPlan(String name, String description, PlanType type, int minDurationMonths, double pricePerMonth) {
        return this.planService.registerPlan(name, description, type, minDurationMonths, pricePerMonth);
    }

    public OperationResult<Void> updatePlanPrice(String name, double newPrice) {
        return this.planService.updatePrice(name, newPrice);
    }

    public OperationResult<Payment> registerPayment(int enrollmentCode, double amount, PaymentType type, String description, String pixKey, String cardLastDigits, int installments, double amountReceived) {
        return this.enrollmentService.registerPayment(enrollmentCode, amount, type, description, pixKey, cardLastDigits, installments, amountReceived);
    }

    public OperationResult<Void> cancelEnrollment(int enrollmentCode) {
        return this.enrollmentService.cancel(enrollmentCode);
    }

    public OperationResult<Student> registerStudent(String name, String cpf, String contact, LocalDate birthDate) {
        return this.studentService.registerStudent(name, cpf, contact, birthDate);
    }

    public OperationResult<Student> findStudentByCpf(String cpf) {
        return this.studentService.findByCPF(cpf);
    }

    public OperationResult<Plan> findPlanByName(String name) {
        return this.planService.findByName(name);
    }

    public OperationResult<Void> removeStudent(String cpf) {
        OperationResult<Student> searchResult = this.studentService.findByCPF(cpf);

        if (!searchResult.isSuccess()) {
            return new OperationResult<>(false, searchResult.getMessage());
        }

        if (this.enrollmentService.hasActiveEnrollment(cpf)) {
            return new OperationResult<>(false, "Erro: Não é possível remover um aluno com matrícula ativa.");
        }

        return this.studentService.removeStudent(cpf);
    }

    public OperationResult<Enrollment> enrollStudent(String cpf, String planName, LocalDate startDate, int duration, double amount, PaymentType paymentType, String paymentDescription, String pixKey, String cardLastDigits, int installments, double amountReceived) {

        OperationResult<Student> studentResult = this.studentService.findByCPF(cpf);
        if (!studentResult.isSuccess()) {
            return new OperationResult<>(false, studentResult.getMessage());
        }

        OperationResult<Plan> planResult = this.planService.findByName(planName);
        if (!planResult.isSuccess()) {
            return new OperationResult<>(false, planResult.getMessage());
        }

        if (this.enrollmentService.hasActiveEnrollment(cpf)) {
            return new OperationResult<>(false, "Erro: Este aluno já possui uma matrícula ativa.");
        }

        if (amount <= 0) {
            return new OperationResult<>(false, "Erro: O pagamento inicial deve ser maior que zero.");
        }

        Student student = studentResult.getData();
        Plan plan = planResult.getData();

        return this.enrollmentService.enroll(student, plan, startDate, duration, amount, paymentType, paymentDescription, pixKey, cardLastDigits, installments, amountReceived);
    }

    public OperationResult<Void> updateStudent(String cpf, String newName, String newContact) {
        return this.studentService.updateStudent(cpf, newName, newContact);
    }

    public OperationResult<ArrayList<Student>> listStudents() {
        return new OperationResult<>(true, "Lista de alunos carregada.", this.studentService.listAll());
    }

    public OperationResult<ArrayList<Plan>> listPlans() {
        return new OperationResult<>(true, "Lista de planos carregada.", this.planService.listAll());
    }

    public OperationResult<ArrayList<Enrollment>> listEnrollments() {
        return new OperationResult<>(true, "Lista de matrículas carregada.", this.enrollmentService.listAll());
    }

    public OperationResult<Enrollment> findActiveEnrollment(String cpf) {
        return this.enrollmentService.findActiveByStudent(cpf);
    }
}