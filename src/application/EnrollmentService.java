package application;

import domain.*;
import persistence.Repository;
import java.time.LocalDate;
import exceptions.PersistenceException;

public class EnrollmentService extends Repository<Enrollment> {

    private static int nextCode = 1;

    public EnrollmentService() {
        super();
    }

    private int generateCode() {
        return nextCode++;
    }

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

    public OperationResult<Enrollment> enroll(Student student, Plan plan, LocalDate startDate, int duration, double amount, PaymentType paymentType, String paymentDescription, String pixKey, String cardLastDigits, int installments, double amountReceived) {

        if (duration < plan.getMinDurationMonths()) {
            return new OperationResult<>(false, "Erro: A duração informada é menor que a duração mínima do plano.");
        }

        if (paymentType == PaymentType.CASH && amountReceived < amount) {
            return new OperationResult<>(false, "Erro: O valor recebido não pode ser menor que o valor cobrado.");
        }

        Enrollment newEnrollment = new Enrollment(generateCode(), student, plan, startDate, duration);
        Payment initialPayment = instantiatePayment(amount, paymentType, paymentDescription, pixKey, cardLastDigits, installments, amountReceived);

        newEnrollment.registerPayment(initialPayment);

        this.add(newEnrollment);

        return new OperationResult<>(true, "Matrícula realizada com sucesso!", newEnrollment);
    }

    public OperationResult<Enrollment> findByCode(int code) {
        for (Enrollment e : this.elements) {
            if (e.getCode() == code) {
                return new OperationResult<>(true, "Matrícula encontrada.", e);
            }
        }
        return new OperationResult<>(false, "Erro: Matrícula não encontrada.");
    }

    public boolean hasActiveEnrollment(String cpf) {
        return findActiveByStudent(cpf).isSuccess();
    }

    public OperationResult<Enrollment> findActiveByStudent(String cpf) {
        for (Enrollment e : this.elements) {
            if (e.getStudent().getCpf().equals(cpf) && e.getStatus() == EnrollmentStatus.ACTIVE) {
                return new OperationResult<>(true, "Matrícula ativa encontrada.", e);
            }
        }
        return new OperationResult<>(false, "Erro: Nenhuma matrícula ativa encontrada para este aluno.");
    }

    public OperationResult<Payment> registerPayment(int code, double amount, PaymentType type, String description, String pixKey, String cardLastDigits, int installments, double amountReceived) {
        OperationResult<Enrollment> searchResult = findByCode(code);

        if (!searchResult.isSuccess()) {
            return new OperationResult<>(false, searchResult.getMessage());
        }

        Enrollment enrollment = searchResult.getData();

        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            return new OperationResult<>(false, "Erro: Não é possível registrar pagamento em uma matrícula cancelada.");
        }
        if (amount <= 0) {
            return new OperationResult<>(false, "Erro: O valor do pagamento deve ser positivo.");
        }

        if (type == PaymentType.CASH && amountReceived < amount) {
            return new OperationResult<>(false, "Erro: O valor recebido não pode ser menor que o valor cobrado.");
        }

        Payment payment = instantiatePayment(amount, type, description, pixKey, cardLastDigits, installments, amountReceived);
        enrollment.registerPayment(payment);

        return new OperationResult<>(true, "Pagamento registrado com sucesso!", payment);
    }

    public OperationResult<Void> cancel(int code) {
        OperationResult<Enrollment> searchResult = findByCode(code);

        if (!searchResult.isSuccess()) {
            return new OperationResult<>(false, searchResult.getMessage());
        }

        Enrollment enrollment = searchResult.getData();

        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            return new OperationResult<>(false, "Erro: Esta matrícula já está cancelada.");
        }

        enrollment.cancel();
        return new OperationResult<>(true, "Matrícula cancelada com sucesso!");
    }

    @Override
    public void save(String filePath) throws PersistenceException {
    }

    @Override
    public void load(String filePath) throws PersistenceException {
    }
}