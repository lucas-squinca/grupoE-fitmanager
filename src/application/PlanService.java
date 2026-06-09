package application;

import domain.*;
import persistence.Repository;
import java.util.ArrayList;
import exceptions.PersistenceException;

public class PlanService extends Repository<Plan> {

    public PlanService() {
        super(Plan.class);
    }

    public OperationResult<Plan> registerPlan(String name, String description, PlanType type, int minDurationMonths, double pricePerMonth) {
        if (name == null || name.trim().isEmpty() || type == null) {
            return new OperationResult<>(false, "Erro: Nome e tipo são obrigatórios.");
        }
        if (nameExists(name)) {
            return new OperationResult<>(false, "Erro: Já existe um plano com este nome.");
        }

        Plan newPlan = null;

        try {
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

            this.add(newPlan);
            return new OperationResult<>(true, "Plano cadastrado com sucesso!", newPlan);

        } catch (exceptions.ValidationException e) {
            return new OperationResult<>(false, e.getMessage());
        }
    }

    public OperationResult<Void> updatePrice(String name, double newPrice) {
        OperationResult<Plan> searchResult = findByName(name);

        if (!searchResult.isSuccess()) {
            return new OperationResult<>(false, searchResult.getMessage());
        }

        if (newPrice <= 0) {
            return new OperationResult<>(false, "Erro: O novo preço deve ser maior que zero.");
        }

        Plan plan = searchResult.getData();
        plan.updatePrice(newPrice);
        return new OperationResult<>(true, "Preço do plano atualizado com sucesso!");
    }

    public OperationResult<Plan> findByName(String name) {
        for (Plan p : this.elements) {
            if (p.getName().equalsIgnoreCase(name)) {
                return new OperationResult<>(true, "Plano encontrado.", p);
            }
        }
        return new OperationResult<>(false, "Erro: Plano não encontrado.");
    }

    private boolean nameExists(String name) {
        return findByName(name).isSuccess();
    }

}