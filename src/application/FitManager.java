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
        return this.studentService.findByCPF(cpf);
    }

    // Busca plano por nome
    public Plan findPlanByName(String name) {
        return this.planService.findByName(name);
    }

    // Fluxo 6: Excluir Aluno
    public OperationResult removeStudent(String cpf) {
        // 1. Verifica se o aluno existe
        Student student = this.studentService.findByCPF(cpf);
        if (student == null) {
            return new OperationResult(false, "Erro: Aluno não encontrado.");
        }

        // 2. Verifica se o aluno tem matrícula ativa
        if (this.enrollmentService.hasActiveEnrollment(cpf)) {
            return new OperationResult(false, "Erro: Não é possível remover um aluno com matrícula ativa.");
        }

        // 3. Se tudo estiver certo, manda o serviço de aluno desativar
        return this.studentService.removeStudent(cpf);
    }

    public OperationResult enrollStudent(String cpf, String planName, LocalDate startDate, int duration, double amount, PaymentType paymentType, String paymentDescription) {

        // 1. Localiza o Aluno
        Student student = this.studentService.findByCPF(cpf);
        if (student == null) {
            return new OperationResult(false, "Erro: Aluno não encontrado.");
        }

        // 2. Localiza o Plano
        Plan plan = this.planService.findByName(planName);
        if (plan == null) {
            return new OperationResult(false, "Erro: Plano não encontrado.");
        }

        // 3. Verifica se o aluno JÁ TEM matrícula ativa
        if (this.enrollmentService.hasActiveEnrollment(cpf)) {
            return new OperationResult(false, "Erro: Este aluno já possui uma matrícula ativa.");
        }

        // 4. Valida se o pagamento inicial é positivo
        if (amount <= 0) {
            return new OperationResult(false, "Erro: O pagamento inicial deve ser maior que zero.");
        }

        // 5. Delega a criação atômica para o serviço
        return this.enrollmentService.enroll(student, plan, startDate, duration, amount, paymentType, paymentDescription);
    }

    public ArrayList<Student> listStudents() {
        return this.studentService.listStudents();
    }

    public ArrayList<Plan> listPlans() {
        return this.planService.listPlans();
    }

    public ArrayList<Enrollment> listEnrollments() {
        return this.enrollmentService.listEnrollments();
    }

    public Enrollment findActiveEnrollment(String cpf) {
        return this.enrollmentService.findActiveByStudent(cpf);
    }
}
