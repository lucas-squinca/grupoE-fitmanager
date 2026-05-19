package domain;
import java.time.LocalDate;

public class CreditCardPayment extends Payment {
    private int installments; // Parcelas
    private String cardLastDigits; // Últimos 4 dígitos

    public CreditCardPayment(LocalDate date, double amount, String description, int installments, String cardLastDigits) {
        super(date, amount, PaymentType.CREDIT_CARD, description);
        this.installments = installments;
        this.cardLastDigits = cardLastDigits;
    }

    @Override
    public double getProcessingFee() {
        return getAmount() * 0.05; // Simulando uma taxa de 5% da maquininha
    }

    @Override
    public String getPaymentSummary() {
        return String.format("Cartão de Crédito (Final %s) | %dx de R$ %.2f | Valor Total: R$ %.2f",
                this.cardLastDigits, this.installments, (getAmount() / this.installments), getAmount());
    }
}