package application;

import domain.*;
import java.util.ArrayList;

public class PlanService {
    private ArrayList<Plan> plans;

    public PlanService() {
        this.plans = new ArrayList<>();
    }

    public OperationResult registerPlan(String name, String description, PlanType type, int minDurationMonths, double pricePerMonth) {
        if (name == null || name.trim().isEmpty() || type == null) {
            return new OperationResult(false, "Erro: Nome e tipo são obrigatórios.");
        }
        if (nameExists(name)) {
            return new OperationResult(false, "Erro: Já existe um plano com este nome.");
        }

        Plan newPlan = null;

        switch (type) {
            case MONTHLY:
                newPlan = new MonthlyPlan(name, description, minDurationMonths, pricePerMonth);
                break;
            case QUARTERLY:
                newPlan = new QuarterlyPlan(name, description, minDurationMonths, pricePerMonth);
                break;
            case SEMI_ANNUAL:
                newPlan = new SemiAnnualPlan(name, description, minDurationMonths, pricePerMonth);
                break;
            case ANNUAL:
                newPlan = new AnnualPlan(name, description, minDurationMonths, pricePerMonth);
                break;
        }

        this.plans.add(newPlan);
        return new OperationResult(true, "Plano cadastrado com sucesso!", newPlan);
    }

    public OperationResult updatePrice(String name, double newPrice) {
        Plan plan = findByName(name);
        if (plan == null) {
            return new OperationResult(false, "Erro: Plano não encontrado.");
        }
        if (newPrice <= 0) {
            return new OperationResult(false, "Erro: O novo preço deve ser maior que zero.");
        }

        plan.updatePrice(newPrice);
        return new OperationResult(true, "Preço do plano atualizado com sucesso!");
    }

    public ArrayList<Plan> listPlans() {
        return this.plans;
    }

    public Plan findByName(String name) {
        for (Plan p : plans) {
            if (p.getName().equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    private boolean nameExists(String name) {
        return findByName(name) != null;
    }
}