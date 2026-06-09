package domain;

import java.time.LocalDate;
import java.io.Serializable;
import exceptions.ValidationException;

public abstract class Payment implements Serializable {
    private LocalDate date;
    private double amount;
    private PaymentType type;
    private String description;

    public Payment(LocalDate date, double amount, PaymentType type, String description) {
        if (date == null) {
            throw new ValidationException("Erro Crítico: A data do pagamento não pode ser nula.");
        }
        if (amount <= 0) {
            throw new ValidationException("Erro Crítico: O valor do pagamento deve ser maior que zero.");
        }
        if (type == null) {
            throw new ValidationException("Erro Crítico: O tipo de pagamento não pode ser nulo.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Erro Crítico: A descrição do pagamento não pode ficar em branco.");
        }

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