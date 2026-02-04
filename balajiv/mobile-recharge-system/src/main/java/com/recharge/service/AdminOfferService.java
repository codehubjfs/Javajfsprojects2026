package com.recharge.service;

import com.recharge.dao.AuditLogDAO;
import com.recharge.dao.OfferDAO;
import com.recharge.dao.PlanOfferDAO;
import com.recharge.dao.RechargePlanDAO;
import com.recharge.model.Offer;

public class AdminOfferService {
	
	private final OfferDAO offerDAO = new OfferDAO();
    private final PlanOfferDAO planOfferDAO = new PlanOfferDAO();
    private final RechargePlanDAO planDAO = new RechargePlanDAO();
    private final AuditLogDAO auditDAO = new AuditLogDAO();

    /**
     * used to create offer
     * @param offer
     * @param adminUserId
     */
    public void createOfferByTitle(Offer offer, int adminUserId) {
    	
    	// check offer title existence
    	if(offerDAO.offerExistsByTitle(offer.getTitle())) {
    		throw new RuntimeException("Offer title already exists");
    	}

        // date expirty validation 
        if (offer.getEndDate().isBefore(offer.getStartDate())) {
            throw new RuntimeException("Offer end date cannot be before start date");
        }

        int offerId = offerDAO.createOffer(offer);

        auditDAO.log(adminUserId,"OFFER", offerId, "CREATE", null, offer.getTitle() + " [" + 
        		offer.getStartDate() + " to " + offer.getEndDate() + "]");

        System.out.println("Offer created successfully");
    }
    
    
    // used to change offer status
    public void changeOfferStatusByTitle(String title, boolean active, int adminUserId) {

        offerDAO.updateOfferStatusByTitle(title, active);

        auditDAO.log(adminUserId, "OFFER", null, active ? "ACTIVATE" : "DEACTIVATE", null,
        		String.valueOf(active));

        System.out.println("Offer status updated");
    }
    
    // used to attach offer to plan
    public void attachOfferToPlanByNames(String operatorName, String planName, String offerTitle,
    		int priority, int adminUserId) {

        if (priority < 1) {
            throw new RuntimeException("Priority must be >= 1");
        }

        int planId = planDAO.getPlanIdByOperatorAndName(operatorName, planName);

        int offerId = offerDAO.getOfferIdByTitle(offerTitle);

        if (!offerDAO.isOfferActive(offerId)) {
            throw new RuntimeException("Offer is not active");
        }

        planOfferDAO.attach(planId, offerId, priority);

        auditDAO.log(adminUserId, "OFFER_PLAN", null, "MAP",null, offerTitle + " -> " + planName);

        System.out.println("Offer mapped to plan successfully");
    }
    
    // used to remove the offer from the plan
    public void removeOfferFromPlanByNames(String operatorName, String planName, String offerTitle,
    			int adminUserId) {

        int planId = planDAO.getPlanIdByOperatorAndName(operatorName, planName);

        int offerId = offerDAO.getOfferIdByTitle(offerTitle);

        planOfferDAO.detach(planId, offerId);

        auditDAO.log(adminUserId, "OFFER_PLAN", null, "UNMAP",null, offerTitle + " -> " + planName);

        System.out.println("Offer removed from plan successfully");
    }
}
