package domain;

public class MonthlyPlan extends Plan {
    public MonthlyPlan(String name, String description, int minDurationMonths, double pricePerMonth) {
        super(name, description, minDurationMonths, pricePerMonth);
    }

    @Override
    public double calculateTotalPrice(int months) {
        return getPricePerMonth() * months;
    }

    @Override
    public double getCancellationFee(Enrollment enrollment) {
        return 0.0;
    }

    @Override
    public PlanType getType() {
        return PlanType.MONTHLY;
    }
}