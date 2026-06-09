package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.io.Serializable;
import exceptions.ValidationException;

public class Enrollment implements Serializable {
    private int code;
    private Student student;
    private Plan plan;
    private LocalDate startDate;
    private LocalDate endDate;
    private int durationMonths;
    private double totalPrice;
    private EnrollmentStatus status;
    private ArrayList<Payment> payments;

    public Enrollment(int code, Student student, Plan plan, LocalDate startDate, int durationMonths) {
        if (code <= 0) {
            throw new ValidationException("Erro Crítico: O código da matrícula deve ser maior que zero.");
        }
        if (student == null) {
            throw new ValidationException("Erro Crítico: O aluno associado não pode ser nulo.");
        }
        if (plan == null) {
            throw new ValidationException("Erro Crítico: O plano associado não pode ser nulo.");
        }
        if (startDate == null) {
            throw new ValidationException("Erro Crítico: A data de início não pode ser nula.");
        }
        if (durationMonths <= 0) {
            throw new ValidationException("Erro Crítico: A duração da matrícula deve ser maior que zero.");
        }

        this.code = code;
        this.student = student;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(durationMonths);
        this.durationMonths = durationMonths;
        this.totalPrice = plan.calculateTotalPrice(durationMonths);
        this.status = EnrollmentStatus.ACTIVE;
        this.payments = new ArrayList<>();
    }

    public void registerPayment(Payment payment) {
        if (payment == null) {
            throw new ValidationException("Erro Crítico: O pagamento a ser registrado não pode ser nulo.");
        }
        this.payments.add(payment);
    }

    public double calculateTotalPaid(){
        double total = 0;

        for (Payment p : this.payments) {
            total += p.getAmount();
        }
        return total;
    }

    public double calculateBalance() {
        double totalFees = 0;
        for (Payment p : this.payments) {
            totalFees += p.getProcessingFee();
        }
        return (this.totalPrice + totalFees) - this.calculateTotalPaid();
    }

    public void cancel() {
        if (this.status == EnrollmentStatus.ACTIVE) {
            this.status = EnrollmentStatus.CANCELLED;
            // Aplica a multa ao preço total, refletindo imediatamente no Saldo Pendente
            this.totalPrice += this.plan.getCancellationFee(this);
        }
    }

    public int getCode() {
        return code;
    }

    public Student getStudent() {
        return student;
    }

    public Plan getPlan() {
        return plan;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public EnrollmentStatus getStatus() {
        return this.status;
    }

    public ArrayList<Payment> getPayments() {
        return this.payments;
    }
}
