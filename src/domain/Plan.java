package domain;

public abstract class Plan {
    private String name;
    private String description;
    private int minDurationMonths;
    private double pricePerMonth;

    public Plan(String name, String description, int minDurationMonths, double pricePerMonth) {
        this.name = name;
        this.description = description;
        this.minDurationMonths = minDurationMonths;
        this.pricePerMonth = pricePerMonth;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getMinDurationMonths() { return minDurationMonths; }
    public double getPricePerMonth() { return pricePerMonth; }

    public void updatePrice(double newPrice) {
        if (newPrice > 0) {
            this.pricePerMonth = newPrice;
        }
    }

    public abstract double calculateTotalPrice(int months);
    public abstract double getCancellationFee(Enrollment enrollment);
    public abstract PlanType getType();
}