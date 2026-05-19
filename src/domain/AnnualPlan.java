package domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AnnualPlan extends Plan {
    public AnnualPlan(String name, String description, int minDurationMonths, double pricePerMonth) {
        super(name, description, minDurationMonths, pricePerMonth);
    }

    @Override
    public double calculateTotalPrice(int months) {
        double total = getPricePerMonth() * months;
        return total * 0.85;
    }

    @Override
    public double getCancellationFee(Enrollment enrollment) {
        LocalDate start = enrollment.getStartDate();
        LocalDate now = LocalDate.now();

        long monthsElapsed = ChronoUnit.MONTHS.between(start, now);
        double halfDuration = enrollment.getDurationMonths() / 2.0;

        if (monthsElapsed < halfDuration) {
            return enrollment.getTotalPrice() * 0.20;
        }

        return 0.0;
    }

    @Override
    public PlanType getType() {
        return PlanType.ANNUAL;
    }
}