package domain;

public class SemiAnnualPlan extends Plan {
    public SemiAnnualPlan(String name, String description, int minDurationMonths, double pricePerMonth) {
        super(name, description, minDurationMonths, pricePerMonth);
    }

    @Override
    public double calculateTotalPrice(int months) {
        double total = getPricePerMonth() * months;
        if (months >= getMinDurationMonths()) {
            return total * 0.90;
        }
        return total;
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