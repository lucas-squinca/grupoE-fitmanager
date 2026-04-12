package application;

import domain.Plan;
import domain.PlanType;

import java.util.ArrayList;

public class PlanService {
    private ArrayList<Plan> plans;

    public PlanService() {
        this.plans = new ArrayList<>();
    }

    public boolean nameExists(String name) {
        for (Plan plan : this.plans) {
            if (plan.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public OperationResult registerPlan(String name, String description, PlanType type, int minDurationMonths, double pricePerMonth) {
        // 1. Validação de campos vazios/nulos
        if (name == null || name.trim().isEmpty() || description == null || description.trim().isEmpty() || type == null) {
            return new OperationResult(false, "Erro: Nome, descrição e tipo são obrigatórios.");
        }

        // 2. Validação de valores numéricos
        if (minDurationMonths <= 0) {
            return new OperationResult(false, "Erro: A duração mínima deve ser maior que zero.");
        }
        if (pricePerMonth <= 0) {
            return new OperationResult(false, "Erro: O preço por mês deve ser um valor positivo.");
        }

        // 3. Validação de unicidade do nome
        if (nameExists(name)) {
            return new OperationResult(false, "Erro: Já existe um plano cadastrado com este nome.");
        }

        // 4. Sucesso: Cria o plano e adiciona na lista
        Plan newPlan = new Plan(name, description, type, minDurationMonths, pricePerMonth);
        this.plans.add(newPlan);

        return new OperationResult(true, "Plano cadastrado com sucesso!", newPlan);
    }

    public Plan findByName(String name) {
        for (Plan plan : this.plans) {
            if (plan.getName().equalsIgnoreCase(name)) {
                return plan;
            }
        }
        return null;
    }

    public OperationResult updatePrice(String name, double newPrice) {
        Plan plan = findByName(name);
        if(plan == null) {
            return new OperationResult(false, "Erro: Plano não encontrado.");
        }

        if(newPrice <= 0) {
            return new OperationResult(false, "Erro: O novo preço deve ser um valor positivo.");
        }

        plan.updatePrice(newPrice);
        return new OperationResult(true, "Preço do plano atualizado com sucesso!");
    }

    // Listar todos os planos
    public ArrayList<Plan> listPlans() {
        return this.plans;
    }
}
