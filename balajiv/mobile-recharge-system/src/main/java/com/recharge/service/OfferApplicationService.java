package com.recharge.service;

import com.recharge.dao.PlanOfferDAO;
import com.recharge.dao.PlanOfferDAO.OfferData;

public class OfferApplicationService {

    private final PlanOfferDAO planOfferDAO = new PlanOfferDAO();

    /**
     * used to apply offer to the plan 
     * @param planId
     * @param originalAmount
     * @return discounted_amount 
     */
    public double applyOfferIfAny(int planId, double originalAmount) {

        OfferData offer = planOfferDAO.getBestOfferForPlan(planId);

        if (offer == null) {
            return originalAmount;
        }

        double discountedAmount;

        if ("FLAT".equalsIgnoreCase(offer.type)) {
            discountedAmount = originalAmount - offer.value;
        }
        else if ("PERCENTAGE".equalsIgnoreCase(offer.type)) {
            discountedAmount = originalAmount - (originalAmount * offer.value / 100);
        }
        else {
            return originalAmount;
        }

        return Math.max(discountedAmount, 0);
    }
}
