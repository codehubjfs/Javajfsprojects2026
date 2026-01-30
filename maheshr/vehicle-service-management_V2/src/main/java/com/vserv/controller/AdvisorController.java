package com.vserv.controller;

import java.util.List;

import com.vserv.exception.BusinessLogicException;
import com.vserv.model.*;
import com.vserv.service.*;
import com.vserv.util.*;

public class AdvisorController {
    private User _currentUser;
    private AdvisorService _advisorService;

    public AdvisorController(User user) {
        AuthorizationUtil.requireRole(user, "ADVISOR");
        this._currentUser = user;
        this._advisorService = new AdvisorService();
    }

    public void showDashboard() {
        String menu = """

                SERVICE ADVISOR DASHBOARD

                Welcome, %s

                1. View Assigned Services
                2. Start Service
                3. Manage Service Items (BOM)
                4. Update Service Remarks
                5. Complete Service
                0. Logout

                """.formatted(_currentUser.getFullName());

        while (true) {
            System.out.print(menu);
	        int choice = InputValidator.readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> viewAssignedServices();
                case 2 -> startService();
                case 3 -> manageServiceItems();
                case 4 -> updateRemarks();
                case 5 -> completeService();
                case 0 -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Please try again.");
            }
        }
    }

    private void viewAssignedServices() {
        System.out.println("ASSIGNED SERVICES");

        try {
            List<ServiceRecord> records = _advisorService.getAssignedServices(_currentUser.getUserId());

            if (records.isEmpty()) {
                System.out.println("\nNo services assigned.");
            } else {
                System.out.println();

                records.forEach(r -> {
                    System.out.printf("[ID:%d] %s\n", 
                        r.getServiceId(), r.toString());
                    System.out.println("   Status: " + r.getStatus());
                    if (r.getServiceStartDate() != null) {
                        System.out.println("   Started: " + r.getServiceStartDate());
                    }
                    if (r.getRemarks() != null && !r.getRemarks().isEmpty()) {
                        System.out.println("   Remarks: " + r.getRemarks());
                    }
                    System.out.println();
                });
            }
        } catch (BusinessLogicException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void startService() {
        System.out.println("START SERVICE");

        try {
            List<ServiceRecord> records = _advisorService.getAssignedServices(_currentUser.getUserId());
            List<ServiceRecord> pending = records.stream()
                .filter(r -> r.getStatus().equals("PENDING"))
                .toList();

            if (pending.isEmpty()) {
                System.out.println("\nNo pending services to start.");
                return;
            }

            System.out.println("\nPending Services:");
            for (int i = 0; i < pending.size(); i++) {
                System.out.printf("%d. %s\n", (i + 1), pending.get(i).toString());
            }

            int choice = InputValidator.readInt("\nSelect service (1-" + pending.size() + "): ");
            if (choice < 1 || choice > pending.size()) {
                System.out.println("\nInvalid selection.");
                return;
            }

            ServiceRecord selected = pending.get(choice - 1);
            _advisorService.startService(selected.getServiceId());
            System.out.println("\nService started.");

        } catch (BusinessLogicException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void manageServiceItems() {
        System.out.println("MANAGE SERVICE ITEMS");

        try {
            List<ServiceRecord> records = _advisorService.getAssignedServices(_currentUser.getUserId());
            List<ServiceRecord> active = records.stream()
                .filter(r -> !r.getStatus().equals("COMPLETED"))
                .toList();

            if (active.isEmpty()) {
                System.out.println("\nNo active services.");
                return;
            }

            System.out.println("\nActive Services:");
            for (int i = 0; i < active.size(); i++) {
                System.out.printf("%d. %s [%s]\n", (i + 1), 
                    active.get(i).toString(), active.get(i).getStatus());
            }

            int choice = InputValidator.readInt("\nSelect service (1-" + active.size() + "): ");
            if (choice < 1 || choice > active.size()) {
                System.out.println("\nInvalid selection.");
                return;
            }

            ServiceRecord selected = active.get(choice - 1);
            manageItemsForService(selected);

        } catch (BusinessLogicException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void manageItemsForService(ServiceRecord record) throws BusinessLogicException {
        while (true) {
            System.out.println("\nSERVICE: " + record.getVehicleInfo());
            
            List<ServiceItem> items = _advisorService.getServiceItems(record.getServiceId());
            
            if (!items.isEmpty()) {
                System.out.println("\nCurrent Items:");
                items.forEach(item -> System.out.println(item.toString()));
                System.out.println("\nTotal: Rs. " + 
                    _advisorService.calculateServiceTotal(record.getServiceId()));
            } else {
                System.out.println("\nNo items added yet.");
            }

            System.out.println("""
                
                1. Add Item
                2. Remove Item
                0. Back
                """);

            int action = InputValidator.readInt("Choice: ");

            switch (action) {
                case 1 -> addItemToService(record);
                case 2 -> removeItemFromService(items);
                case 0 -> { 
                	return; 
                	}
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addItemToService(ServiceRecord record) throws BusinessLogicException {
        System.out.println("\nAVAILABLE WORK ITEMS");
        
        List<WorkItem> workItems = _advisorService.getWorkItems("ALL");
        
        if (workItems.isEmpty()) {
            System.out.println("\nNo work items available.");
            return;
        }

        System.out.println();
        for (int i = 0; i < workItems.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), workItems.get(i).toString());
        }

        int choice = InputValidator.readInt("\nSelect item (1-" + workItems.size() + "): ");
        if (choice < 1 || choice > workItems.size()) {
            System.out.println("\nInvalid selection.");
            return;
        }

        WorkItem selected = workItems.get(choice - 1);
        
        int quantity = InputValidator.readInt("Quantity: ");
        if (quantity < 1) {
            System.out.println("\nInvalid quantity.");
            return;
        }

        _advisorService.addServiceItem(record.getServiceId(), selected.getWorkItemId(), quantity);
        System.out.println("\nItem added.");
    }

    private void removeItemFromService(List<ServiceItem> items) throws BusinessLogicException {
        if (items.isEmpty()) {
            System.out.println("\nNo items to remove.");
            return;
        }

        int choice = InputValidator.readInt("\nSelect item to remove (1-" + items.size() + "): ");
        if (choice < 1 || choice > items.size()) {
            System.out.println("\nInvalid selection.");
            return;
        }

        ServiceItem selected = items.get(choice - 1);
        _advisorService.deleteServiceItem(selected.getItemId());
        System.out.println("\nItem removed.");
    }

    private void updateRemarks() {
        System.out.println("UPDATE SERVICE REMARKS");

        try {
            List<ServiceRecord> records = _advisorService.getAssignedServices(_currentUser.getUserId());
            List<ServiceRecord> active = records.stream()
                .filter(r -> !r.getStatus().equals("COMPLETED"))
                .toList();

            if (active.isEmpty()) {
                System.out.println("\nNo active services.");
                return;
            }

            System.out.println("\nActive Services:");
            for (int i = 0; i < active.size(); i++) {
                System.out.printf("%d. %s\n", (i + 1), active.get(i).toString());
            }

            int choice = InputValidator.readInt("\nSelect service (1-" + active.size() + "): ");
            if (choice < 1 || choice > active.size()) {
                System.out.println("\nInvalid selection.");
                return;
            }

            ServiceRecord selected = active.get(choice - 1);
            
            if (selected.getRemarks() != null && !selected.getRemarks().isEmpty()) {
                System.out.println("\nCurrent Remarks: " + selected.getRemarks());
            }

            String remarks = InputValidator.readString("\nNew Remarks: ");
            _advisorService.updateRemarks(selected.getServiceId(), remarks);
            System.out.println("\nRemarks updated.");

        } catch (BusinessLogicException e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

private void completeService() {
    System.out.println("COMPLETE SERVICE");

    try {
        List<ServiceRecord> records = _advisorService.getAssignedServices(_currentUser.getUserId());
        List<ServiceRecord> active = records.stream()
            .filter(r -> r.getStatus().equals("IN_PROGRESS"))
            .toList();

        if (active.isEmpty()) {
            System.out.println("\nNo services in progress.");
            return;
        }

        System.out.println("\nServices In Progress:");
        for (int i = 0; i < active.size(); i++) {
            ServiceRecord r = active.get(i);
            System.out.printf("%d. %s\n", (i + 1), r.toString());
            if (r.getEstimatedHours() != null) {
                System.out.println("   Estimated Hours: " + r.getEstimatedHours());
            }
        }

        int choice = InputValidator.readInt("\nSelect service (1-" + active.size() + "): ");
        if (choice < 1 || choice > active.size()) {
            System.out.println("\nInvalid selection.");
            return;
        }

        ServiceRecord selected = active.get(choice - 1);

        List<ServiceItem> items = _advisorService.getServiceItems(selected.getServiceId());
        if (items.isEmpty()) {
            System.out.println("\nCannot complete service without any items added.");
            return;
        }

        System.out.println("\nService Summary:");
        System.out.println("Vehicle: " + selected.getVehicleInfo());
        System.out.println("Service: " + selected.getServiceName());
        if (selected.getEstimatedHours() != null) {
            System.out.println("Estimated Hours: " + selected.getEstimatedHours());
        }
        
        System.out.println("\nItems:");
        items.forEach(item -> System.out.println("  " + item.toString()));
        System.out.println("\nItems Total: Rs. " + 
            _advisorService.calculateServiceTotal(selected.getServiceId()));

        double actualHours;
        while (true) {
            try {
                actualHours = InputValidator.readDouble("\nActual hours worked: ");
                if (actualHours <= 0) {
                    System.out.println("Hours must be greater than 0. Please try again.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        if (selected.getEstimatedHours() != null && actualHours > selected.getEstimatedHours()) {
            double overtimeHours = actualHours - selected.getEstimatedHours();
            System.out.println("\nOVERTIME: " + String.format("%.2f", overtimeHours) + " hours");
            System.out.println("Overtime will be billed.");
        }

        String confirm = InputValidator.readString("\nMark as completed? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            _advisorService.completeService(selected.getServiceId(), _currentUser.getUserId(), actualHours);
            System.out.println("\nService marked as completed.");
            System.out.println("Actual hours recorded: " + actualHours);
            System.out.println("Admin can now generate invoice and process payment.");
        } else {
            System.out.println("\nCancelled.");
        }

    } catch (BusinessLogicException e) {
        System.out.println("\nError: " + e.getMessage());
    }
}
}