package controller;

import exception.BusinessException;
import model.*;
import service.*;
import util.InputUtil;
import java.util.List;

public class CustomerController {
    private User currentUser;
    private VehicleService vehicleService;
    
    public CustomerController(User user) {
        this.currentUser = user;
        this.vehicleService = new VehicleService();
    }
    
    public void showDashboard() {
        while (true) {
            System.out.print("CUSTOMER DASHBOARD");
            System.out.println("\nWelcome, " + currentUser.getFullName());
            System.out.println("\n1. My Vehicles");
            System.out.println("2. Add New Vehicle");
            System.out.println("0. Logout");
            
            int choice = InputUtil.readInt("\nEnter choice: ");
            
            switch (choice) {
                case 1: 
                	viewVehicles(); 
                	break;
                case 2:
                	addVehicle();
                	break;
                case 3: 
                	// TODO: browseVehicles()
                	break;
                case 4: 
                	// TODO: bookService();
                case 5: 
                	// TODO: myBookings();
                	break;
                case 0: 
                    System.out.println("\nLogged out");
                    return;
                default:
                    System.out.println("\nInvalid choice");
                    InputUtil.pause();
            }
        }
    }
    
    private void viewVehicles() {
    	System.out.print("MY VEHICLES");
        
        try {
            List<Vehicle> vehicles = vehicleService.getVehiclesByUser(currentUser.getUserId());
            
            if (vehicles.isEmpty()) {
                System.out.println("\nNo vehicles registered. Please add a vehicle first.");
            } else {
                System.out.println();
                for (int i = 0; i < vehicles.size(); i++) {
                    Vehicle v = vehicles.get(i);
                    System.out.printf("%d. %s\n", (i + 1), v.toString());
                    System.out.println("   Registration: " + v.getRegistrationNumber());
                    System.out.println("   Year: " + v.getManufactureYear());
                    
                    if (v.getNextServiceDue() != null) {
                        System.out.println("   Next Service Due: " + v.getNextServiceDue());
                    }
                    System.out.println();
                }
            }
            
        } catch (BusinessException e) {
            System.out.println(e.getMessage());
        }
        
        InputUtil.pause();
    }
   
    private void addVehicle() {
    	System.out.print("ADD NEW VEHICLE");
    	
    	System.out.println("\nCar Types: ");
    	System.out.println("1. SEDAN");
    	System.out.println("2. SUV");
    	System.out.println("3. HATCHBACK");
    	System.out.println("4. COUPE");
    	System.out.println("5. CONVERTIBLE");
    	System.out.println("6. WAGON");
    	System.out.println("7. MINIVAN");
    	
    	int typeChoice = InputUtil.readInt("\nSelect car type (1-7): ");
    	
    	String carType;
    	switch(typeChoice) {
    	case 1:
    		carType = "SEDAN";
    		break;
    	case 2:
    		carType = "SUV";
    		break;
    	case 3:
    		carType = "HATCHBACK";
    		break;
    	case 4:
    		carType = "COUPE";
    		break;
    	case 5:
    		carType = "CONVERTIBLE";
    		break;
    	case 6:
    		carType = "WAGON";
    		break;
    	case 7:
    		carType = "MINIVAN";
    		break;
    	default:
    		System.out.println("\nInvalid Choice");
    		InputUtil.pause();
    		return;
    	}
    	
    	String brand = InputUtil.readString("Brand: ");
    	String model = InputUtil.readString("Model: ");
    	String regNumber = InputUtil.readString("Registration Number(TN55AK0915): ");
    	int year = InputUtil.readInt("Manufacture Year: ");
    	int mileage = InputUtil.readInt("Mileage (km): ");
    	
    	try {
    		Vehicle vehicle = vehicleService.addVehicle(currentUser.getUserId(),
    				carType, brand, model, regNumber, year, mileage);
    		
    		System.out.println("\nCar added");
    		System.out.println("Vehicle ID: " + vehicle.getVehicleId());
    	}
    	catch(BusinessException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	InputUtil.pause();
    }
}