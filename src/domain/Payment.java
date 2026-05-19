package domain;

import java.time.LocalDate;

public abstract class Payment {
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

    public LocalDate getDate() { return date; }
    public double getAmount() { return amount; }
    public PaymentType getType() { return type; }
    public String getDescription() { return description; }

    // MÉTODOS ABSTRATOS
    // 1. Calcula a taxa de processamento
    public abstract double getProcessingFee();

    // 2. Monta o resumo formatado com os dados específicos do pagamento
    public abstract String getPaymentSummary();
}