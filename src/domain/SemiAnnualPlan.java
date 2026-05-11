package domain;

public class SemiAnnualPlan extends Plan {
    public SemiAnnualPlan(String name, String description, int minDurationMonths, double pricePerMonth) {
        super(name, description, minDurationMonths, pricePerMonth);
    }

    @Override
    public double calculateTotalPrice(int months) {
        double total = getPricePerMonth() * months;
        return total * 0.90;
    }

    @Override
    public double getCancellationFee(Enrollment enrollment) {
        return 0.0;
    }

    @Override
    public PlanType getType() {
        return PlanType.SEMI_ANNUAL;
    }
}