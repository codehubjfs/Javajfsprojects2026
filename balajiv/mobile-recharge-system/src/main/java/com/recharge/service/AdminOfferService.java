package com.recharge.service;

import com.recharge.dao.AuditLogDAO;
import com.recharge.dao.OfferDAO;
import com.recharge.dao.PlanOfferDAO;
import com.recharge.model.Offer;

public class AdminOfferService {
	
	private final OfferDAO offerDAO = new OfferDAO();
    private final PlanOfferDAO planOfferDAO = new PlanOfferDAO();
    private final AuditLogDAO auditDAO = new AuditLogDAO();

    public void createOffer(Offer offer, int adminUserId) {

        // date expirty validation 
        if (offer.getEndDate().isBefore(offer.getStartDate())) {
            throw new RuntimeException("Offer end date cannot be before start date");
        }

        int offerId = offerDAO.createOffer(offer);

        auditDAO.log(adminUserId,"OFFER",offerId,"CREATE",null,offer.getTitle() + " [" + 
        		offer.getStartDate() + " to " + offer.getEndDate() + "]");

        System.out.println("Offer created successfully");
    }
}
