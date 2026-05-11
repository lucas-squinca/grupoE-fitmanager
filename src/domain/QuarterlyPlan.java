package domain;

public class QuarterlyPlan extends Plan {
    public QuarterlyPlan(String name, String description, int minDurationMonths, double pricePerMonth) {
        super(name, description, minDurationMonths, pricePerMonth);
    }

    @Override
    public double calculateTotalPrice(int months) {
        double total = getPricePerMonth() * months;
        return total * 0.95;
    }

    @Override
    public double getCancellationFee(Enrollment enrollment) {
        return 0.0;
    }
}