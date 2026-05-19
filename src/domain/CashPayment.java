package domain;
import java.time.LocalDate;

public class CashPayment extends Payment {
    private double amountReceived;

    public CashPayment(LocalDate date, double amount, String description, double amountReceived) {
        super(date, amount, PaymentType.CASH, description);
        this.amountReceived = amountReceived;
    }

    // Calculo do troco
    public double getChange() {
        return this.amountReceived - getAmount();
    }

    @Override
    public double getProcessingFee() {
        return 0.0;
    }

    @Override
    public String getPaymentSummary() {
        return String.format("Dinheiro | Valor Pago: R$ %.2f | Recebido: R$ %.2f | Troco: R$ %.2f",
                getAmount(), this.amountReceived, getChange());
    }
}