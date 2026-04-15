package domain;

import java.time.LocalDate;
import java.util.ArrayList;

public class Enrollment {
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
        return this.totalPrice - this.calculateTotalPaid();
    }

    public void cancel() {
        // Só pode cancelar se estiver ativa
        if (this.status == EnrollmentStatus.ACTIVE) {
            this.status = EnrollmentStatus.CANCELLED;
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
}
