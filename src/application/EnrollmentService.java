package application;

import domain.Enrollment;
import domain.EnrollmentStatus;
import domain.Payment;
import domain.PaymentType;
import domain.Plan;
import domain.Student;

import java.time.LocalDate;
import java.util.ArrayList;

public class EnrollmentService {
    private ArrayList<Enrollment> enrollments;

    // Variável estática geradora de códigos
    private static int nextCode = 1;

    public EnrollmentService() {
        this.enrollments = new ArrayList<>();
    }

    private int generateCode() {
        return nextCode++;
    }

    // Fluxo 3: Realizar Matrícula
    public OperationResult enroll(Student student, Plan plan, LocalDate startDate, int duration, double amount, PaymentType paymentType, String paymentDescription) {

        // 1. Validação: A duração contratada não pode ser menor que a mínima do plano
        if (duration < plan.getMinDurationMonths()) {
            return new OperationResult(false, "Erro: A duração informada é menor que a duração mínima do plano.");
        }

        // 2. Cria a matrícula (Status, endDate e totalPrice são resolvidos lá dentro do construtor)
        Enrollment newEnrollment = new Enrollment(generateCode(), student, plan, startDate, duration);

        // 3. Cria o pagamento inicial
        Payment initialPayment = new Payment(LocalDate.now(), amount, paymentType, paymentDescription);

        // 4. Registra o pagamento na matrícula
        newEnrollment.registerPayment(initialPayment);

        // 5. Salva na lista do sistema
        this.enrollments.add(newEnrollment);

        return new OperationResult(true, "Matrícula realizada com sucesso!", newEnrollment);
    }

    // Busca uma matrícula pelo código
    public Enrollment findByCode(int code) {
        for (Enrollment e : this.enrollments) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

    // Verifica se um aluno já tem uma matrícula ativa
    public boolean hasActiveEnrollment(String cpf) {
        for (Enrollment e : this.enrollments) {
            if (e.getStudent().getCpf().equals(cpf) && e.getStatus() == EnrollmentStatus.ACTIVE) {
                return true;
            }
        }
        return false;
    }

    // Busca a matrícula ativa de um aluno específico
    public Enrollment findActiveByStudent(String cpf) {
        for (Enrollment e : this.enrollments) {
            if (e.getStudent().getCpf().equals(cpf) && e.getStatus() == EnrollmentStatus.ACTIVE) {
                return e;
            }
        }
        return null;
    }

    // Fluxo 4: Registrar um novo pagamento em uma matrícula existente
    public OperationResult registerPayment(int code, double amount, PaymentType type, String description) {
        Enrollment enrollment = findByCode(code);

        if (enrollment == null) {
            return new OperationResult(false, "Erro: Matrícula não encontrada.");
        }

        // Não pode pagar se a matrícula estiver cancelada
        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            return new OperationResult(false, "Erro: Não é possível registrar pagamento em uma matrícula cancelada.");
        }

        if (amount <= 0) {
            return new OperationResult(false, "Erro: O valor do pagamento deve ser positivo.");
        }

        Payment payment = new Payment(LocalDate.now(), amount, type, description);
        enrollment.registerPayment(payment);

        return new OperationResult(true, "Pagamento registrado com sucesso!");
    }

    // Fluxo 5: Cancelar Matrícula
    public OperationResult cancel(int code) {
        Enrollment enrollment = findByCode(code);

        if (enrollment == null) {
            return new OperationResult(false, "Erro: Matrícula não encontrada.");
        }

        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            return new OperationResult(false, "Erro: Esta matrícula já está cancelada.");
        }

        // Chama o método da classe de domínio para trocar o status de forma segura
        enrollment.cancel();
        return new OperationResult(true, "Matrícula cancelada com sucesso!");
    }

    // Listar todas as matrículas
    public ArrayList<Enrollment> listEnrollments() {
        return this.enrollments;
    }
}