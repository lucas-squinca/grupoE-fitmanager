package domain;
import java.time.LocalDate;

public class DebitCardPayment extends Payment {
    private String cardLastDigits;

    public DebitCardPayment(LocalDate date, double amount, String description, String cardLastDigits) {
        super(date, amount, PaymentType.DEBIT_CARD, description);
        this.cardLastDigits = cardLastDigits;
    }

    @Override
    public double getProcessingFee() {
        return getAmount();
    }

    @Override
    public String getPaymentSummary() {
        return String.format("Cartão de Débito (Final %s) | Valor: R$ %.2f",
                this.cardLastDigits, getAmount());
    }
}