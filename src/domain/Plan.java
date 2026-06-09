package domain;

import exceptions.ValidationException;
import java.io.Serializable;

public abstract class Plan implements Serializable{
    private String name;
    private String description;
    private int minDurationMonths;
    private double pricePerMonth;

    public Plan(String name, String description, int minDurationMonths, double pricePerMonth) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Erro Crítico: O nome do plano não pode ficar em branco.");
        }
        if (minDurationMonths <= 0) {
            throw new ValidationException("Erro Crítico: A duração mínima do plano deve ser maior que zero.");
        }
        if (pricePerMonth < 0) {
            throw new ValidationException("Erro Crítico: O preço do plano não pode ser negativo.");
        }

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
        if (newPrice <= 0) {
            throw new ValidationException("Erro Crítico: O novo preço deve ser maior que zero.");
        }
        this.pricePerMonth = newPrice;
    }

    public abstract double calculateTotalPrice(int months);
    public abstract double getCancellationFee(Enrollment enrollment);
    public abstract PlanType getType();
}