package domain;

public class Plan {
    private String name;
    private String description;
    private PlanType type;
    private int minDurationMonths;
    private double pricePerMonth;

    public Plan(String name, String description, PlanType type, int minDurationMonths, double pricePerMonth) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.minDurationMonths = minDurationMonths;
        this.pricePerMonth = pricePerMonth;
    }

    public double calculateTotalPrice(int months) {
        return this.pricePerMonth * months;
    }

    public void updatePrice(double newPrice) {
        this.pricePerMonth = newPrice;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlanType getType() {
        return this.type;
    }

    public void setType(PlanType type) {
        this.type = type;
    }

    public int getMinDurationMonths() {
        return this.minDurationMonths;
    }

    public void setMinDurationMonths(int minDurationMonths) {
        this.minDurationMonths = minDurationMonths;
    }

    public double getPricePerMonth() {
        return this.pricePerMonth;
    }
}
