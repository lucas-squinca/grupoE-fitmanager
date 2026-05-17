package application;

import domain.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EnrollmentService {
    private ArrayList<Enrollment> enrollments;
    private static int nextCode = 1;

    public EnrollmentService() {
        this.enrollments = new ArrayList<>();
    }

    private int generateCode() {
        return nextCode++;
    }

    // 1. A FÁBRICA DE PAGAMENTOS: Cria a subclasse correta
    private Payment instantiatePayment(double amount, PaymentType type, String description, String pixKey, String cardLastDigits, int installments, double amountReceived) {
        LocalDate today = LocalDate.now();
        switch (type) {
            case PIX:
                return new PixPayment(today, amount, description, pixKey);
            case CREDIT_CARD:
                return new CreditCardPayment(today, amount, description, installments, cardLastDigits);
            case DEBIT_CARD:
                return new DebitCardPayment(today, amount, description, cardLastDigits);
            case CASH:
                return new CashPayment(today, amount, description, amountReceived);
            default:
                return null;
        }
    }

    // Fluxo 3: Realizar Matrícula
    public OperationResult enroll(Student student, Plan plan, LocalDate startDate, int duration, double amount, PaymentType paymentType, String paymentDescription, String pixKey, String cardLastDigits, int installments, double amountReceived) {

        if (duration < plan.getMinDurationMonths()) {
            return new OperationResult(false, "Erro: A duração informada é menor que a duração mínima do plano.");
        }

        if (paymentType == PaymentType.CASH && amountReceived < amount) {
            return new OperationResult(false, "Erro: O valor recebido não pode ser menor que o valor cobrado.");
        }

        Enrollment newEnrollment = new Enrollment(generateCode(), student, plan, startDate, duration);

        Payment initialPayment = instantiatePayment(amount, paymentType, paymentDescription, pixKey, cardLastDigits, installments, amountReceived);

        newEnrollment.registerPayment(initialPayment);
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
    public OperationResult registerPayment(int code, double amount, PaymentType type, String description, String pixKey, String cardLastDigits, int installments, double amountReceived) {
        Enrollment enrollment = findByCode(code);

        if (enrollment == null) {
            return new OperationResult(false, "Erro: Matrícula não encontrada.");
        }
        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            return new OperationResult(false, "Erro: Não é possível registrar pagamento em uma matrícula cancelada.");
        }
        if (amount <= 0) {
            return new OperationResult(false, "Erro: O valor do pagamento deve ser positivo.");
        }

        if (type == PaymentType.CASH && amountReceived < amount) {
            return new OperationResult(false, "Erro: O valor recebido não pode ser menor que o valor cobrado.");
        }

        Payment payment = instantiatePayment(amount, type, description, pixKey, cardLastDigits, installments, amountReceived);
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