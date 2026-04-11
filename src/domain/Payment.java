package domain;

import java.time.LocalDate;

public class Payment {
    private LocalDate date;
    private double amount;
    private PaymentType type;
    private String description;

    public Payment(LocalDate date, double amount, PaymentType type, String description) {
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }


    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
