package domain;
import java.time.LocalDate;

public class PixPayment extends Payment {
    private String pixKey;

    public PixPayment(LocalDate date, double amount, String description, String pixKey) {
        super(date, amount, PaymentType.PIX, description);
        this.pixKey = pixKey;
    }

    @Override
    public double getProcessingFee() {
        return 0.0;
    }

    @Override
    public String getPaymentSummary() {
        return String.format("PIX | Valor: R$ %.2f | Chave: %s | Data: %s",
                getAmount(), this.pixKey, getDate());
    }
}