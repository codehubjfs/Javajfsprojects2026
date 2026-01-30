package com.vserv.controller;

import java.util.List;

import com.vserv.exception.BusinessLogicException;
import com.vserv.model.*;
import com.vserv.service.*;
import com.vserv.util.*;

/**
 * guest workflow - browse services without login
 * 
 * @author Mahesh R
 */
public class GuestController {
    private BookingService _bookingService;

    public GuestController() {
        this._bookingService = new BookingService();
    }

    public void showDashboard() {
        String menu = """

                GUEST - SERVICE CATALOG

                Browse our services (Login required to book)

                1. View All Services
                2. Browse by Service Type
                3. Browse by Car Type
                4. Search Services
                0. Back to Login

                """;

        while (true) {
            System.out.print(menu);
	        int choice = InputValidator.readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> browseAllServices();
                case 2 -> browseByServiceType();
                case 3 -> browseByCarType();
                case 4 -> searchServices();
                case 0 -> {
                    System.out.println("\nThank you.");
                    return;
                }
                default -> System.out.println("\nInvalid choice. Please select 1-4 or 0 to exit.");
            }
        }
    }

    private void browseAllServices() {
        System.out.println("ALL SERVICES");

        try {
            List<Catalog> services = _bookingService.getAllServices();

            if (services.isEmpty()) {
                System.out.println("\nNo services available at the moment.");
            } else {
                displayServices(services);
            }

        } catch (BusinessLogicException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void browseByServiceType() {
        System.out.println("""
                BROWSE BY SERVICE TYPE

                1. SERVICING
                2. REPAIR
                3. INSPECTION
                4. MAINTENANCE
                """);

        int choice = InputValidator.readInt("Select type (1-4): ");

        String serviceType = switch (choice) {
            case 1 -> "SERVICING";
            case 2 -> "REPAIR";
            case 3 -> "INSPECTION";
            case 4 -> "MAINTENANCE";
            default -> null;
        };

        if (serviceType == null) {
            System.out.println("\nInvalid choice.");
            return;
        }

        try {
            List<Catalog> services = _bookingService.getAllServices().stream()
                .filter(s -> s.getServiceType().equals(serviceType))
                .toList();

            if (services.isEmpty()) {
                System.out.println("\nNo " + serviceType + " services available.");
            } else {
                System.out.println("\n" + serviceType + " SERVICES:");
                displayServices(services);
            }

        } catch (BusinessLogicException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void browseByCarType() {
        System.out.println("""
                BROWSE BY CAR TYPE

                1. SEDAN
                2. SUV
                3. HATCHBACK
                4. COUPE
                5. CONVERTIBLE
                6. WAGON
                7. MINIVAN
                """);

        int choice = InputValidator.readInt("Select car type (1-7): ");

        String carType = switch (choice) {
            case 1 -> "SEDAN";
            case 2 -> "SUV";
            case 3 -> "HATCHBACK";
            case 4 -> "COUPE";
            case 5 -> "CONVERTIBLE";
            case 6 -> "WAGON";
            case 7 -> "MINIVAN";
            default -> null;
        };

        if (carType == null) {
            System.out.println("\nInvalid choice.");
            return;
        }

        try {
            List<Catalog> services = _bookingService.getServicesByCarType(carType);

            if (services.isEmpty()) {
                System.out.println("\nNo services available for " + carType);
            } else {
                System.out.println("\nSERVICES FOR " + carType + ":");
                displayServices(services);
            }

        } catch (BusinessLogicException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void searchServices() {
        System.out.println("SEARCH SERVICES");

        String keyword = InputValidator.readString("\nEnter search keyword: ");

        if (keyword.trim().isEmpty()) {
            System.out.println("\nSearch keyword cannot be empty.");
            return;
        }

        try {
            List<Catalog> services = _bookingService.getAllServices().stream()
                .filter(s -> s.getServiceName().toLowerCase().contains(keyword.toLowerCase()) ||
                           s.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .toList();

            if (services.isEmpty()) {
                System.out.println("\nNo services found matching '" + keyword + "'");
            } else {
                System.out.println("\nSEARCH RESULTS FOR '" + keyword + "':");
                displayServices(services);
            }

        } catch (BusinessLogicException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayServices(List<Catalog> services) {
        System.out.println();
        services.stream()
            .forEach(service -> {
                System.out.printf("%s\n", service.toString());
                System.out.println("   Type: " + service.getServiceType());
                System.out.println("   Car Type: " + service.getCarType());
                System.out.println("   Description: " + service.getDescription());
                System.out.println("   Duration: " + service.getDurationHours() + " hours");
                System.out.println();
            });

        System.out.println("\nTo book services, please login or register as a customer.");
    }
}