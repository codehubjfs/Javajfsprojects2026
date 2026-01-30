package com.vserv.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.vserv.exception.BusinessLogicException;
import com.vserv.model.*;
import com.vserv.service.*;
import com.vserv.util.*;

/**
 * Handle admin workflow with full system access
 * 
 * @author Mahesh R
 */
public class AdminController {
	private User _currentUser;
	private VehicleService _vehicleService;
	private BookingService _bookingService;
	private AdminService _adminService;
	private BookingHistoryService _historyService;
	private InvoiceService _invoiceService;

	public AdminController(User user) {
	    AuthorizationUtil.requireRole(user, "ADMIN");
	    this._currentUser = user;
	    this._vehicleService = new VehicleService();
	    this._bookingService = new BookingService();
	    this._adminService = new AdminService();
	    this._historyService = new BookingHistoryService();
	    this._invoiceService = new InvoiceService();
	}

	public void showDashboard() {
	    String menu = """

	            ADMIN DASHBOARD

	            Welcome, %s

	            - User Management -
	            1. View All Users
	            2. Create New User
	            3. Update User Status
	            4. Delete User

	            - Customer Operations -
	            5. View All Vehicles
	            6. Add Vehicle (for any user)
	            7. Update Vehicle
	            8. Delete Vehicle
	            9. Browse Services
	            10. Book Service (for any user)
	            11. View All Bookings
	            12. Reschedule Booking
	            13. Cancel Booking

	            - Service Availability Management -
	            14. View Service Availability
	            15. Add Service Availability Slot
	            16. Update Service Availability Slot
	            17. Delete Service Availability Slot

	            - System Management -
	            18. View Service Catalog
	            19. View System Statistics

	            - Service Advisor & Execution -
	            20. View Service Advisors
	            21. Assign Booking to Advisor
	            22. View Completed Services
	            26. Update Advisor Status

	            - Invoice & Payment -
	            23. Generate Invoice
	            24. Process Payment
	            25. View All Invoices

	            - Audit & Logs -
	            27. View Booking History
	            28. View History by Action Type

	            0. Logout

	            """.formatted(_currentUser.getFullName());

	    while (true) {
	        System.out.print(menu);
	        int choice = InputValidator.readInt("Enter your choice: ");

	        switch (choice) {
	            case 1 -> viewAllUsers();
	            case 2 -> createNewUser();
	            case 3 -> updateUserStatus();
	            case 4 -> deleteUser();
	            case 5 -> viewAllVehicles();
	            case 6 -> addVehicleForUser();
	            case 7 -> updateVehicle();
	            case 8 -> deleteVehicle();
	            case 9 -> browseServices();
	            case 10 -> bookServiceForUser();
	            case 11 -> viewAllBookings();
	            case 12 -> rescheduleBooking();
	            case 13 -> cancelBooking();
	            case 14 -> viewServiceAvailability();
	            case 15 -> addServiceAvailability();
	            case 16 -> updateServiceAvailability();
	            case 17 -> deleteServiceAvailability();
	            case 18 -> viewServiceCatalog();
	            case 19 -> viewSystemStatistics();
	            case 20 -> viewServiceAdvisors();
	            case 21 -> assignBookingToAdvisor();
	            case 22 -> viewCompletedServices();
	            case 23 -> generateInvoice();
	            case 24 -> processPayment();
	            case 25 -> viewAllInvoices();
	            case 26 -> updateAdvisorStatus();
	            case 27 -> viewBookingHistory();
	            case 28 -> viewHistoryByActionType();
	            case 0 -> {
	                return;
	            }
	            default -> System.out.println("\nInvalid choice. Please try again.");
	        }
	    }
	}




	/**
	 * View all registered users
	 */
	private void viewAllUsers() {
		System.out.println("ALL USERS");

		try {
			List<User> users = _adminService.getAllUsers();

			if (users.isEmpty()) {
				System.out.println("\nNo users found.");
			} else {
				System.out.println();
				
				users.forEach(u -> {
					System.out.printf("[ID:%d] %s (%s)\n",u.getUserId(), u.getFullName(),
							u.getEmail());
					System.out.println("   Role: " + u.getRoleName());
					System.out.println("   Phone: " + (u.getPhone() != null ? u.getPhone() : "N/A"));
					System.out.println("   Gender: " + (u.getGender() != null ? u.getGender() : "N/A"));
					System.out.println("   Status: " + u.getStatus());
					System.out.println("   Created: " + u.getCreatedAt());
					System.out.println();
				});
				
				
			}
		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

/**
 * Create new user
 */
private void createNewUser() {
    System.out.println("CREATE NEW USER");
    System.out.println("(Enter 'e' or '0' to exit)\n");

    String fullName;
    while (true) {
        fullName = InputValidator.readString("Full Name: ");
        
        if (fullName.trim().equalsIgnoreCase("e") || fullName.trim().equals("0")) {
            System.out.println("\nUser creation cancelled. Returning to dashboard.");
            return;
        }
        
        if (fullName.trim().isEmpty()) {
            System.out.println("Full name cannot be empty. Please try again.");
            continue;
        }
        
        if (FieldValidator.isValidFullName(fullName)) {
            break;
        }
        System.out.println("Invalid name. Use only letters, spaces, hyphens (2-100 chars). Please try again.");
    }

    String email;
    while (true) {
        email = InputValidator.readString("Email: ");
        
        if (email.trim().equalsIgnoreCase("e") || email.trim().equals("0")) {
            System.out.println("\nUser creation cancelled. Returning to dashboard.");
            return;
        }
        
        if (email.trim().isEmpty()) {
            System.out.println("Email cannot be empty. Please try again.");
            continue;
        }
        
        if (FieldValidator.isValidEmail(email)) {
            break;
        }
        System.out.println("Invalid email format (e.g. user@domain.com). Please try again.");
    }

    String password;
    while (true) {
        password = InputValidator.readString("Password: ");
        
        if (password.trim().equalsIgnoreCase("e") || password.trim().equals("0")) {
            System.out.println("\nUser creation cancelled. Returning to dashboard.");
            return;
        }
        
        if (password.trim().isEmpty()) {
            System.out.println("Password cannot be empty. Please try again.");
            continue;
        }
        
        if (FieldValidator.isValidPassword(password)) {
            break;
        }
        System.out.println("Password must be at least 8 characters with uppercase, lowercase, digit, and special character.");
    }

    String phone;
    while (true) {
        phone = InputValidator.readString("Phone (10 digits): ");
        
        if (phone.trim().equalsIgnoreCase("e") || phone.trim().equals("0")) {
            System.out.println("\nUser creation cancelled. Returning to dashboard.");
            return;
        }
        
        if (phone.trim().isEmpty()) {
            System.out.println("Phone cannot be empty. Please try again.");
            continue;
        }
        
        if (FieldValidator.isValidPhone(phone)) {
            break;
        }
        System.out.println("Invalid phone number. Must be 10 digits. Please try again.");
    }

    String gender;
    while (true) {
        System.out.println("\nGender:");
        System.out.println("1. MALE");
        System.out.println("2. FEMALE");
        System.out.println("3. PREFER NOT TO SAY");
        System.out.println("0. Cancel");

        int genderChoice = InputValidator.readInt("Select gender (1-3, 0 to cancel): ");

        if (genderChoice == 0) {
            System.out.println("\nUser creation cancelled. Returning to dashboard.");
            return;
        }

        gender = switch (genderChoice) {
            case 1 -> "MALE";
            case 2 -> "FEMALE";
            case 3 -> "PREFER_NOT_TO_SAY";
            default -> null;
        };

        if (gender != null) {
            break;
        }
        System.out.println("Invalid choice. Please select 1-3.");
    }

    String roleName;
    while (true) {
        System.out.println("""
                \nSelect Role:
                1. ADMIN
                2. CUSTOMER
                3. ADVISOR
                0. Cancel
                """);

        int roleChoice = InputValidator.readInt("Enter role (1-3, 0 to cancel): ");

        if (roleChoice == 0) {
            System.out.println("\nUser creation cancelled. Returning to dashboard.");
            return;
        }

        roleName = switch (roleChoice) {
            case 1 -> "ADMIN";
            case 2 -> "CUSTOMER";
            case 3 -> "ADVISOR";
            default -> null;
        };
        
        if (roleName != null) {
            break;
        }
        System.out.println("Invalid choice. Please select 1-3.");
    }

    String specialization = null;
    java.math.BigDecimal overtimeRate = null;
    String availabilityStatus = null;

    if ("ADVISOR".equals(roleName)) {
        System.out.println("\nSERVICE ADVISOR DETAILS");
        
        while (true) {
            System.out.println("\nSpecialization:");
            System.out.println("1. General");
            System.out.println("2. Engine Specialist");
            System.out.println("3. Electrical Systems");
            System.out.println("4. Brake Systems");
            System.out.println("5. Transmission");
            System.out.println("6. Custom");
            System.out.println("0. Cancel");
            
            int specChoice = InputValidator.readInt("Select specialization (1-6, 0 to cancel): ");
            
            if (specChoice == 0) {
                System.out.println("\nUser creation cancelled. Returning to dashboard.");
                return;
            }
            
            if (specChoice == 6) {
                String custom = InputValidator.readString("Enter custom specialization: ");
                if (custom.trim().equalsIgnoreCase("e") || custom.trim().equals("0")) {
                    System.out.println("\nUser creation cancelled. Returning to dashboard.");
                    return;
                }
                specialization = custom.trim().isEmpty() ? "General" : custom;
                break;
            }
            
            specialization = switch (specChoice) {
                case 1 -> "General";
                case 2 -> "Engine Specialist";
                case 3 -> "Electrical Systems";
                case 4 -> "Brake Systems";
                case 5 -> "Transmission";
                default -> null;
            };
            
            if (specialization != null) {
                break;
            }
            System.out.println("Invalid choice. Please select 1-6.");
        }
        
        while (true) {
            try {
                String rateInput = InputValidator.readString("Overtime Rate (Rs./hour) [Default: 500]: ");
                
                if (rateInput.trim().equalsIgnoreCase("e") || rateInput.trim().equals("0")) {
                    System.out.println("\nUser creation cancelled. Returning to dashboard.");
                    return;
                }
                
                if (rateInput.trim().isEmpty()) {
                    overtimeRate = new java.math.BigDecimal("500.00");
                    break;
                }
                
                double rate = Double.parseDouble(rateInput);
                
                if (rate < 0) {
                    System.out.println("Rate cannot be negative. Please try again.");
                    continue;
                }
                
                overtimeRate = new java.math.BigDecimal(rate);
                break;
                
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        
        while (true) {
            System.out.println("\nInitial Availability Status:");
            System.out.println("1. AVAILABLE");
            System.out.println("2. ON_LEAVE");
            System.out.println("0. Cancel");
            
            int statusChoice = InputValidator.readInt("Select status (1-2, 0 to cancel): ");
            
            if (statusChoice == 0) {
                System.out.println("\nUser creation cancelled. Returning to dashboard.");
                return;
            }
            
            availabilityStatus = switch (statusChoice) {
                case 1 -> "AVAILABLE";
                case 2 -> "ON_LEAVE";
                default -> null;
            };
            
            if (availabilityStatus != null) {
                break;
            }
            System.out.println("Invalid choice. Please select 1 or 2.");
        }
    }

    try {
        User user;
        
        if ("ADVISOR".equals(roleName)) {
            user = _adminService.createUserWithAdvisorDetails(
                fullName, email, password, phone, gender, roleName,
                specialization, overtimeRate, availabilityStatus
            );
        } else {
            user = _adminService.createUser(fullName, email, password, phone, gender, roleName);
        }
        
        System.out.println("\nUser created.");
        System.out.println("User ID: " + user.getUserId());
        System.out.println("Role: " + user.getRoleName());
        
        if ("ADVISOR".equals(roleName)) {
            System.out.println("\nService Advisor Details:");
            System.out.println("Specialization: " + specialization);
            System.out.println("Overtime Rate: Rs." + overtimeRate + "/hour");
            System.out.println("Status: " + availabilityStatus);
        }
        
    } catch (BusinessLogicException e) {
        System.out.println("\nError: " + e.getMessage());
    }
}
	
	/**
	 * Update user status
	 */
	private void updateUserStatus() {
		System.out.println("UPDATE USER STATUS");

		try {
			List<User> users = _adminService.getAllUsers();

			if (users.isEmpty()) {
				System.out.println("\nNo users found.");
				return;
			}

			System.out.println("\nUsers:");
			for (int i = 0; i < users.size(); i++) {
				User u = users.get(i);
				System.out.printf("%d. [ID:%d] %s - %s (Status: %s)\n", (i + 1), u.getUserId(), u.getFullName(),
						u.getRoleName(), u.getStatus());
			}

			int choice;
			while (true) {
				choice = InputValidator.readInt("\nSelect user (1-" + users.size() + "): ");
				if (choice < 1 || choice > users.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			User selectedUser = users.get(choice - 1);

			System.out.println("\nCurrent Status: " + selectedUser.getStatus());
			System.out.println("""
					\nSelect New Status:
					1. ACTIVE
					2. INACTIVE
					""");

			int statusChoice;
			while (true) {
				statusChoice = InputValidator.readInt("Enter choice (1-2): ");
				if (statusChoice < 1 || statusChoice > 2) {
					System.out.println("Invalid choice. Please select 1 or 2.");
					continue;
				}
				break;
			}

			String newStatus = statusChoice == 1 ? "ACTIVE" : "INACTIVE";

			_adminService.updateUserStatus(selectedUser.getUserId(), newStatus);
			System.out.println("\nUser status updated to: " + newStatus);

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}
	
	/**
	 * Delete user after checking status
	 */
	private void deleteUser() {
		System.out.println("DELETE USER");

		try {
			List<User> users = _adminService.getAllUsers();

			if (users.isEmpty()) {
				System.out.println("\nNo users found.");
				return;
			}

			System.out.println("\nUsers:");
			for (int i = 0; i < users.size(); i++) {
				User u = users.get(i);
				System.out.printf("%d. [ID:%d] %s - %s (Status: %s)\n", (i + 1), u.getUserId(), 
						u.getFullName(), u.getRoleName(), u.getStatus());
			}

			int choice;
			while (true) {
				choice = InputValidator.readInt("\nSelect user to delete (1-" + users.size() + "): ");
				if (choice < 1 || choice > users.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			User selectedUser = users.get(choice - 1);

			if (selectedUser.getUserId() == _currentUser.getUserId()) {
				System.out.println("\nError: Cannot delete your own account.");
				return;
			}

			System.out.println("\nUser Details:");
			System.out.println("ID: " + selectedUser.getUserId());
			System.out.println("Name: " + selectedUser.getFullName());
			System.out.println("Email: " + selectedUser.getEmail());
			System.out.println("Role: " + selectedUser.getRoleName());
			System.out.println("Status: " + selectedUser.getStatus());

			if (selectedUser.getStatus().equals("ACTIVE")) {
				System.out.println("\nWARNING: This user is currently ACTIVE.");
				System.out.println("set the user to INACTIVE before deletion.");
				
				String proceed;
				while (true) {
					proceed = InputValidator.readString("\nDo you want to proceed anyway? (yes/no): ");
					if (proceed.trim().isEmpty()) {
						System.out.println("Please enter 'yes' or 'no'.");
						continue;
					}
					break;
				}

				if (!proceed.equalsIgnoreCase("yes") && !proceed.equalsIgnoreCase("y")) {
					System.out.println("\nDeletion cancelled.");
					return;
				}
			} else {
				System.out.println("\nUser is INACTIVE and can be safely deleted.");
			}

			System.out.println("\nWARNING: This action will permanently delete:");
			System.out.println("User: " + selectedUser.getFullName() + " (" + selectedUser.getEmail() + ")"
					+ " with all associated vehicles and bookings.");

			String confirm;
			while (true) {
				confirm = InputValidator.readString("\nType 'DELETE' to confirm: ");
				if (confirm.trim().isEmpty()) {
					System.out.println("Confirmation cannot be empty. Type 'DELETE' or any other text to cancel.");
					continue;
				}
				break;
			}

			if (confirm.equals("DELETE")) {
				_adminService.deleteUser(selectedUser.getUserId());
				System.out.println("\nUser deleted.");
			} else {
				System.out.println("\nDeletion cancelled.");
			}

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * View all vehicles in system
	 */
	private void viewAllVehicles() {
		System.out.println("ALL VEHICLES");

		try {
			List<Vehicle> vehicles = _adminService.getAllVehicles();

			if (vehicles.isEmpty()) {
				System.out.println("\nNo vehicles registered.");
			} else {
				System.out.println();
				AtomicInteger index = new AtomicInteger(1);

				vehicles.forEach(v -> {
					System.out.printf("%d. [ID:%d] %s\n", index.getAndIncrement(), v.getVehicleId(), v.toString());
					System.out.println("   Owner User ID: " + v.getUserId());
					System.out.println("   Registration: " + v.getRegistrationNumber());
					System.out.println("   Year: " + v.getManufactureYear());
					if (v.getNextServiceDue() != null) {
						System.out.println("   Next Service Due: " + v.getNextServiceDue());
					}
					System.out.println();
				});
			}
		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * Add vehicle for any user
	 */
	private void addVehicleForUser() {
		System.out.println("ADD VEHICLE");

		try {
			List<User> customers = _adminService.getUsersByRole("CUSTOMER");

			if (customers.isEmpty()) {
				System.out.println("\nNo customers found. Please create a customer first.");
				return;
			}

			System.out.println("\nCustomers:");
			for (int i = 0; i < customers.size(); i++) {
				User u = customers.get(i);
				System.out.printf("%d. [ID:%d] %s (%s)\n", (i + 1), u.getUserId(), u.getFullName(), u.getEmail());
			}

			int userChoice;
			while (true) {
				userChoice = InputValidator.readInt("\nSelect customer (1-" + customers.size() + "): ");
				if (userChoice < 1 || userChoice > customers.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			int userId = customers.get(userChoice - 1).getUserId();

			System.out.println("""
					\nCar Types:
					1. SEDAN
					2. SUV
					3. HATCHBACK
					4. COUPE
					5. CONVERTIBLE
					6. WAGON
					7. MINIVAN
					""");

			int typeChoice;
			String carType;
			while (true) {
				typeChoice = InputValidator.readInt("Select car type (1-7): ");
				if (typeChoice < 1 || typeChoice > 7) {
					System.out.println("Invalid choice. Please select 1-7.");
					continue;
				}

				carType = switch (typeChoice) {
				case 1 -> "SEDAN";
				case 2 -> "SUV";
				case 3 -> "HATCHBACK";
				case 4 -> "COUPE";
				case 5 -> "CONVERTIBLE";
				case 6 -> "WAGON";
				case 7 -> "MINIVAN";
				default -> null;
				};
				break;
			}

			String brand;
			while (true) {
				brand = InputValidator.readString("Brand: ");
				if (brand.trim().isEmpty()) {
					System.out.println("Brand cannot be empty. Please try again.");
					continue;
				}
				break;
			}

			String model;
			while (true) {
				model = InputValidator.readString("Model: ");
				if (model.trim().isEmpty()) {
					System.out.println("Model cannot be empty. Please try again.");
					continue;
				}
				break;
			}

			String regNumber;
			while (true) {
				regNumber = InputValidator.readString("Registration Number (TN55AK0915): ");
				if (regNumber.trim().isEmpty()) {
					System.out.println("Registration number cannot be empty. Please try again.");
					continue;
				}
				if (!FieldValidator.isValidRegistrationNumber(regNumber)) {
					System.out.println("Invalid format. Use format: TN55AK0915");
					continue;
				}
				break;
			}

			int year;
			while (true) {
				year = InputValidator.readInt("Manufacture Year: ");
				if (!FieldValidator.isValidYear(year)) {
					System.out.println("Invalid year. Must be between 1900 and current year.");
					continue;
				}
				break;
			}

			int mileage;
			while (true) {
				mileage = InputValidator.readInt("Mileage (km): ");
				if (mileage < 0) {
					System.out.println("Mileage cannot be negative. Please try again.");
					continue;
				}
				break;
			}

			Vehicle vehicle = _vehicleService.addVehicle(userId, carType, brand, model, regNumber, year, mileage);
			System.out.println("\nVehicle added ");
			System.out.println("Vehicle ID: " + vehicle.getVehicleId());

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * Update vehicle details
	 */
	private void updateVehicle() {
		System.out.println("UPDATE VEHICLE");

		try {
			List<Vehicle> vehicles = _adminService.getAllVehicles();

			if (vehicles.isEmpty()) {
				System.out.println("\nNo vehicles found.");
				return;
			}

			System.out.println("\nVehicles:");
			for (int i = 0; i < vehicles.size(); i++) {
				Vehicle v = vehicles.get(i);
				System.out.printf("%d. [ID:%d] %s (Owner: %d)\n", (i + 1), v.getVehicleId(), v.toString(),
						v.getUserId());
			}
			

			int choice;
			while (true) {
				choice = InputValidator.readInt("\nSelect vehicle (1-" + vehicles.size() + "): ");
				if (choice < 1 || choice > vehicles.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			Vehicle vehicle = vehicles.get(choice - 1);

			System.out.println("\nCurrent Details:");
			System.out.println("Car Type: " + vehicle.getCarType());
			System.out.println("Brand: " + vehicle.getBrand());
			System.out.println("Model: " + vehicle.getModel());
			System.out.println("Year: " + vehicle.getManufactureYear());
			System.out.println("Mileage: " + vehicle.getMileage() + " km");

			System.out.println("""
					\nCar Types:
					1. SEDAN
					2. SUV
					3. HATCHBACK
					4. COUPE
					5. CONVERTIBLE
					6. WAGON
					7. MINIVAN
					""");

			int typeChoice;
			String carType;
			while (true) {
				typeChoice = InputValidator.readInt("Select car type (1-7): ");
				if (typeChoice < 1 || typeChoice > 7) {
					System.out.println("Invalid choice. Please select 1-7.");
					continue;
				}

				carType = switch (typeChoice) {
				case 1 -> "SEDAN";
				case 2 -> "SUV";
				case 3 -> "HATCHBACK";
				case 4 -> "COUPE";
				case 5 -> "CONVERTIBLE";
				case 6 -> "WAGON";
				case 7 -> "MINIVAN";
				default -> null;
				};
				break;
			}

			String brand;
			while (true) {
				brand = InputValidator.readString("Brand [" + vehicle.getBrand() + "]: ");
				if (brand.trim().isEmpty()) {
					brand = vehicle.getBrand();
				}
				break;
			}

			String model;
			while (true) {
				model = InputValidator.readString("Model [" + vehicle.getModel() + "]: ");
				if (model.trim().isEmpty()) {
					model = vehicle.getModel();
				}
				break;
			}

			int year = InputValidator.readInt("Manufacture Year [" + vehicle.getManufactureYear() + "]: ");
			int mileage = InputValidator.readInt("Mileage [" + vehicle.getMileage() + "]: ");

			_vehicleService.updateVehicle(vehicle.getVehicleId(), vehicle.getUserId(), carType, brand, model, year,
					mileage);
			System.out.println("\nVehicle updated.");

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * Delete vehicle
	 */
	private void deleteVehicle() {
		System.out.println("DELETE VEHICLE");

		try {
			List<Vehicle> vehicles = _adminService.getAllVehicles();

			if (vehicles.isEmpty()) {
				System.out.println("\nNo vehicles found.");
				return;
			}

			System.out.println("\nVehicles:");
			for (int i = 0; i < vehicles.size(); i++) {
				Vehicle v = vehicles.get(i);
				System.out.printf("%d. [ID:%d] %s\n", (i + 1), v.getVehicleId(), v.toString());
			}

			int choice;
			while (true) {
				choice = InputValidator.readInt("\nSelect vehicle to delete (1-" + vehicles.size() + "): ");
				if (choice < 1 || choice > vehicles.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			Vehicle vehicle = vehicles.get(choice - 1);

			System.out.println("\nWARNING: This will delete:");
			System.out.println(vehicle.toString());

			String confirm;
			while (true) {
				confirm = InputValidator.readString("\nType 'DELETE' to confirm: ");
				if (confirm.trim().isEmpty()) {
					System.out.println("Confirmation cannot be empty. Type 'DELETE' or any other text to cancel.");
					continue;
				}
				break;
			}

			if (confirm.equals("DELETE")) {
				_vehicleService.deleteVehicle(vehicle.getVehicleId(), vehicle.getUserId());
				System.out.println("\nVehicle deleted.");
			} else {
				System.out.println("\nDeletion cancelled.");
			}

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * Browse service catalog
	 */
	private void browseServices() {
		System.out.println("SERVICE CATALOG");

		try {
			List<Catalog> services = _bookingService.getAllServices();

			if (services.isEmpty()) {
				System.out.println("\nNo services available.");
			} else {
				System.out.println();
				for (Catalog service : services) {
					System.out.println(service.toString());
					System.out.println("   Type: " + service.getServiceType());
					System.out.println("   Car Type: " + service.getCarType());
					System.out.println("   Description: " + service.getDescription());
					System.out.println();
				}
			}
		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * Book service for any user
	 */
	private void bookServiceForUser() {
		System.out.println("BOOK SERVICE");

		try {
			List<Vehicle> vehicles = _adminService.getAllVehicles();

			if (vehicles.isEmpty()) {
				System.out.println("\nNo vehicles found. Please add a vehicle first.");
				return;
			}

			System.out.println("\nVehicles:");
			for (int i = 0; i < vehicles.size(); i++) {
				Vehicle v = vehicles.get(i);
				System.out.printf("%d. [ID:%d] %s (Owner: %d)\n", (i + 1), v.getVehicleId(), v.toString(),
						v.getUserId());
			}

			int vehicleChoice;
			while (true) {
				vehicleChoice = InputValidator.readInt("\nSelect vehicle (1-" + vehicles.size() + "): ");
				if (vehicleChoice < 1 || vehicleChoice > vehicles.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			Vehicle selectedVehicle = vehicles.get(vehicleChoice - 1);

			List<Catalog> services = _bookingService.getServicesByCarType(selectedVehicle.getCarType());

			if (services.isEmpty()) {
				System.out.println("\nNo services available for " + selectedVehicle.getCarType());
				return;
			}

			System.out.println("\nAvailable Services:");
			for (int i = 0; i < services.size(); i++) {
				System.out.printf("%d. %s\n", (i + 1), services.get(i));
			}

			int serviceChoice;
			while (true) {
				serviceChoice = InputValidator.readInt("\nSelect service (1-" + services.size() + "): ");
				if (serviceChoice < 1 || serviceChoice > services.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			Catalog selectedService = services.get(serviceChoice - 1);

			LocalDate serviceDate;
			while (true) {
				viewServiceAvailability();
				serviceDate = InputValidator.readDate("\nEnter service date");
				if (!FieldValidator.isFutureDate(serviceDate) && !serviceDate.equals(LocalDate.now())) {
					System.out.println("Cannot book for past dates. Please try again.");
					continue;
				}
				break;
			}

			LocalDate selectedDate = serviceDate;

			List<Availability> slots = _bookingService.getAvailableSlots(selectedDate);

			List<Availability> slotsForDate = slots.stream().filter(s -> s.getServiceDate().equals(selectedDate))
					.toList();

			if (slotsForDate.isEmpty()) {
				System.out.println("\nNo slots available for " + selectedDate);
				return;
			}

			System.out.println("\nAvailable Time Slots:");
			for (int i = 0; i < slotsForDate.size(); i++) {
				Availability slot = slotsForDate.get(i);
				System.out.printf("%d. %s (Slots remaining: %d)\n", (i + 1), slot.getTimeSlot(),
						slot.getSlotsRemaining());
			}
			
			int slotChoice;
			while (true) {
				slotChoice = InputValidator.readInt("\nSelect time slot (1-" + slotsForDate.size() + "): ");
				if (slotChoice < 1 || slotChoice > slotsForDate.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			Availability selectedSlot = slotsForDate.get(slotChoice - 1);

			String notes = InputValidator.readString("\nAdd notes (optional): ");

			System.out.println("\nBOOKING SUMMARY");
			System.out.println("Vehicle: " + selectedVehicle);
			System.out.println("Service: " + selectedService.getServiceName());
			System.out.println("Price: Rs." + selectedService.getBasePrice());
			System.out.println("Date: " + selectedDate);
			System.out.println("Time: " + selectedSlot.getTimeSlot());

			String confirm;
			while (true) {
				confirm = InputValidator.readString("\nConfirm booking? (yes/no): ");
				if (confirm.trim().isEmpty()) {
					System.out.println("Please enter 'yes' or 'no'.");
					continue;
				}
				break;
			}

			if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
				Booking booking = _bookingService.createBooking(selectedVehicle.getVehicleId(),
						selectedService.getCatalogId(), selectedDate, selectedSlot.getTimeSlot(), notes);

				System.out.println("\nBooking confirmed.");
				System.out.println("Booking ID: " + booking.getBookingId());
			} else {
				System.out.println("\nBooking cancelled.");
			}

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}
	
	/**
	 * View all bookings in system
	 */
	private void viewAllBookings() {
		System.out.println("ALL BOOKINGS");

		try {
			List<Booking> bookings = _adminService.getAllBookings();

			if (bookings.isEmpty()) {
				System.out.println("\nNo bookings found.");
			} else {
				System.out.println();
				for (int i = 0; i < bookings.size(); i++) {
					Booking b = bookings.get(i);
					System.out.printf("%d. [ID:%d] %s\n", (i + 1), b.getBookingId(), b.getVehicleInfo());
					System.out.println("   Service: " + b.getServiceName());
					System.out.println("   Date: " + b.getServiceDate() + " at " + b.getTimeSlot());
					System.out.println("   Status: " + b.getBookingStatus());
					if (b.getBookingNotes() != null && !b.getBookingNotes().isEmpty()) {
						System.out.println("   Notes: " + b.getBookingNotes());
					}
					System.out.println();
				}
			}
		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}
	
	/**
	 * Reschedule any booking
	 */
	private void rescheduleBooking() {
		System.out.println("RESCHEDULE BOOKING");

		try {
			List<Booking> bookings = _adminService.getAllBookings();

			List<Booking> activeBookings = bookings.stream()
					.filter(b -> !b.getBookingStatus().equals("CANCELLED") && !b.getBookingStatus().equals("COMPLETED"))
					.toList();

			if (activeBookings.isEmpty()) {
				System.out.println("\nNo active bookings to reschedule.");
				return;
			}

			System.out.println("\nActive Bookings:");
			for (int i = 0; i < activeBookings.size(); i++) {
				Booking b = activeBookings.get(i);
				System.out.printf("%d. [ID:%d] %s - %s on %s at %s\n", (i + 1), b.getBookingId(), b.getVehicleInfo(),
						b.getServiceName(), b.getServiceDate(), b.getTimeSlot());
			}

			int choice;
			while (true) {
				choice = InputValidator.readInt("\nSelect booking (1-" + activeBookings.size() + "): ");
				if (choice < 1 || choice > activeBookings.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			Booking booking = activeBookings.get(choice - 1);

			LocalDate newDate;
			while (true) {
				newDate = InputValidator.readDate("\nEnter new service date");
				if (!FieldValidator.isFutureDate(newDate) && !newDate.equals(LocalDate.now())) {
					System.out.println("Cannot reschedule to past dates. Please try again.");
					continue;
				}
				break;
			}

			LocalDate selectedDate = newDate;

			List<Availability> slots = _bookingService.getAvailableSlots(selectedDate);

			List<Availability> slotsForDate = slots.stream().filter(s -> s.getServiceDate().equals(selectedDate))
					.toList();

			if (slotsForDate.isEmpty()) {
				System.out.println("\nNo slots available for " + selectedDate);
				return;
			}

			System.out.println("\nAvailable Time Slots:");
			for (int i = 0; i < slotsForDate.size(); i++) {
				Availability slot = slotsForDate.get(i);
				System.out.printf("%d. %s (Slots remaining: %d)\n", (i + 1), slot.getTimeSlot(),
						slot.getSlotsRemaining());
			}

			int slotChoice;
			while (true) {
				slotChoice = InputValidator.readInt("\nSelect time slot (1-" + slotsForDate.size() + "): ");
				if (slotChoice < 1 || slotChoice > slotsForDate.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			Availability selectedSlot = slotsForDate.get(slotChoice - 1);

			System.out.println("\nOld: " + booking.getServiceDate() + " at " + booking.getTimeSlot());
			System.out.println("New: " + selectedDate + " at " + selectedSlot.getTimeSlot());

			String confirm;
			while (true) {
				confirm = InputValidator.readString("\nConfirm reschedule? (yes/no): ");
				if (confirm.trim().isEmpty()) {
					System.out.println("Please enter 'yes' or 'no'.");
					continue;
				}
				break;
			}

			if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
				_bookingService.rescheduleBooking(booking.getBookingId(), selectedDate, selectedSlot.getTimeSlot());
				System.out.println("\nBooking rescheduled.");
			} else {
				System.out.println("\nReschedule cancelled.");
			}

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * Cancel any booking
	 */
	private void cancelBooking() {
		System.out.println("CANCEL BOOKING");

		try {
			List<Booking> bookings = _adminService.getAllBookings();
			List<Booking> activeBookings = bookings.stream()
					.filter(b -> !b.getBookingStatus().equals("CANCELLED") && !b.getBookingStatus().equals("COMPLETED"))
					.toList();

			if (activeBookings.isEmpty()) {
				System.out.println("\nNo active bookings to cancel.");
				return;
			}

			System.out.println("\nActive Bookings:");
			for (int i = 0; i < activeBookings.size(); i++) {
				Booking b = activeBookings.get(i);
				System.out.printf("%d. [ID:%d] %s - %s on %s\n", (i + 1), b.getBookingId(), b.getVehicleInfo(),
						b.getServiceName(), b.getServiceDate());
			}

			int choice;
			while (true) {
				choice = InputValidator.readInt("\nSelect booking to cancel (1-" + activeBookings.size() + "): ");
				if (choice < 1 || choice > activeBookings.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			Booking booking = activeBookings.get(choice - 1);

			System.out.println("\nWARNING: This will cancel:");
			System.out.println(booking.getVehicleInfo() + " - " + booking.getServiceName());
			System.out.println(booking.getServiceDate() + " at " + booking.getTimeSlot());

			String confirm;
			while (true) {
				confirm = InputValidator.readString("\nType 'CANCEL' to confirm: ");
				if (confirm.trim().isEmpty()) {
					System.out.println("Confirmation cannot be empty. Type 'CANCEL' or any other text to abort.");
					continue;
				}
				break;
			}

			if (confirm.equals("CANCEL")) {
				_bookingService.cancelBooking(booking.getBookingId());
				System.out.println("\nBooking cancelled.");
			} else {
				System.out.println("\nCancellation aborted.");
			}

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * View service availability slots
	 */
	private void viewServiceAvailability() {
		System.out.println("SERVICE AVAILABILITY");

		try {
			List<Availability> slots = _adminService.getAllAvailabilitySlots();

			if (slots.isEmpty()) {
				System.out.println("\nNo availability slots found.");
			} else {
				System.out.println();

				slots.forEach(slot -> {
					System.out.printf("[ID:%d] %s | %s\n", slot.getAvailabilityId(),
							slot.getServiceDate(), slot.getTimeSlot());
					System.out.println("   Max Bookings: " + slot.getMaxBookings());
					System.out.println("   Current Bookings: " + slot.getCurrentBookings());
					System.out.println("   Slots Remaining: " + slot.getSlotsRemaining());
					System.out.println("   Available: " + (slot.isAvailable() ? "Yes" : "No"));
					System.out.println();
				});
			}
		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * Add new service availability slot
	 */
	private void addServiceAvailability() {
		System.out.println("ADD SERVICE AVAILABILITY SLOT");

		LocalDate serviceDate;
		while (true) {
			serviceDate = InputValidator.readDate("\nService Date");
			if (serviceDate.isBefore(LocalDate.now())) {
				System.out.println("Cannot create slots for past dates. Please try again.");
				continue;
			}
			break;
		}

		String timeSlot;
		while (true) {
			System.out.println("""
					\nTime Slots:
					1. 09:00-11:00
					2. 11:00-13:00
					3. 13:00-15:00
					4. 15:00-17:00
					5. 17:00-19:00
					""");

			int slotChoice = InputValidator.readInt("Select time slot (1-5): ");

			timeSlot = switch (slotChoice) {
			case 1 -> "09:00-11:00";
			case 2 -> "11:00-13:00";
			case 3 -> "13:00-15:00";
			case 4 -> "15:00-17:00";
			case 5 -> "17:00-19:00";
			default -> null;
			};

			if (timeSlot == null) {
				System.out.println("Invalid choice. Please select 1-5.");
				continue;
			}
			break;
		}

		int maxBookings;
		while (true) {
			maxBookings = InputValidator.readInt("Maximum Bookings: ");
			if (maxBookings < 1) {
				System.out.println("Maximum bookings must be at least 1. Please try again.");
				continue;
			}
			break;
		}

		try {
			Availability slot = _adminService.addServiceAvailability(serviceDate, timeSlot, maxBookings);
			System.out.println("\nService availability slot added.!");
			System.out.println("Slot ID: " + slot.getAvailabilityId());
			System.out.println("Date: " + slot.getServiceDate());
			System.out.println("Time: " + slot.getTimeSlot());

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * Update service availability slot
	 */
	private void updateServiceAvailability() {
		System.out.println("UPDATE SERVICE AVAILABILITY SLOT");

		try {
			List<Availability> slots = _adminService.getAllAvailabilitySlots();

			if (slots.isEmpty()) {
				System.out.println("\nNo availability slots found.");
				return;
			}

			System.out.println("\nAvailability Slots:");
			for (int i = 0; i < slots.size(); i++) {
				Availability slot = slots.get(i);
				System.out.printf("%d. [ID:%d] %s | %s (Current: %d/%d)\n", (i + 1), slot.getAvailabilityId(),
						slot.getServiceDate(), slot.getTimeSlot(), slot.getCurrentBookings(), slot.getMaxBookings());
			}

			int choice;
			while (true) {
				choice = InputValidator.readInt("\nSelect slot to update (1-" + slots.size() + "): ");
				if (choice < 1 || choice > slots.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			Availability slot = slots.get(choice - 1);

			System.out.println("\nCurrent Details:");
			System.out.println("Date: " + slot.getServiceDate());
			System.out.println("Time Slot: " + slot.getTimeSlot());
			System.out.println("Max Bookings: " + slot.getMaxBookings());
			System.out.println("Current Bookings: " + slot.getCurrentBookings());
			System.out.println("Available: " + (slot.isAvailable() ? "Yes" : "No"));

			int maxBookings;
			while (true) {
				maxBookings = InputValidator.readInt("\nNew Maximum Bookings [" + slot.getMaxBookings() + "]: ");
				if (maxBookings < slot.getCurrentBookings()) {
					System.out.println("Cannot set max bookings below current bookings (" + slot.getCurrentBookings()
							+ "). Please try again.");
					continue;
				}
				if (maxBookings < 1) {
					System.out.println("Maximum bookings must be at least 1. Please try again.");
					continue;
				}
				break;
			}

			System.out.println("""
					\nAvailability Status:
					1. Available
					2. Not Available
					""");

			int availChoice;
			boolean isAvailable;
			while (true) {
				availChoice = InputValidator.readInt("Select status (1-2): ");
				if (availChoice < 1 || availChoice > 2) {
					System.out.println("Invalid choice. Please select 1 or 2.");
					continue;
				}
				isAvailable = availChoice == 1;
				break;
			}

			_adminService.updateServiceAvailability(slot.getAvailabilityId(), maxBookings, isAvailable);
			System.out.println("\nService availability slot updated.");

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * Delete service availability slot
	 */
	private void deleteServiceAvailability() {
		System.out.println("DELETE SERVICE AVAILABILITY SLOT");

		try {
			List<Availability> slots = _adminService.getAllAvailabilitySlots();

			if (slots.isEmpty()) {
				System.out.println("\nNo availability slots found.");
				return;
			}

			System.out.println("\nAvailability Slots:");
			for (int i = 0; i < slots.size(); i++) {
				Availability slot = slots.get(i);
				System.out.printf("%d. [ID:%d] %s | %s (Current Bookings: %d)\n", (i + 1), slot.getAvailabilityId(),
						slot.getServiceDate(), slot.getTimeSlot(), slot.getCurrentBookings());
			}

			int choice;
			while (true) {
				choice = InputValidator.readInt("\nSelect slot to delete (1-" + slots.size() + "): ");
				if (choice < 1 || choice > slots.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				break;
			}

			Availability slot = slots.get(choice - 1);

			if (slot.getCurrentBookings() > 0) {
				System.out.println("\nWARNING: This slot has " + slot.getCurrentBookings() + " active booking(s).");
			}

			System.out.println("\nWARNING: This will delete:");
			System.out.println(slot.getServiceDate() + " | " + slot.getTimeSlot());

			String confirm;
			while (true) {
				confirm = InputValidator.readString("\nType 'DELETE' to confirm: ");
				if (confirm.trim().isEmpty()) {
					System.out.println("Confirmation cannot be empty. Type 'DELETE' or any other text to cancel.");
					continue;
				}
				break;
			}

			if (confirm.equals("DELETE")) {
				_adminService.deleteServiceAvailability(slot.getAvailabilityId());
				System.out.println("\nService availability slot deleted.");
			} else {
				System.out.println("\nDeletion cancelled.");
			}

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}

	/**
	 * View service catalog
	 */
	private void viewServiceCatalog() {
		System.out.println("SERVICE CATALOG");

		try {
			List<Catalog> services = _bookingService.getAllServices();

			if (services.isEmpty()) {
				System.out.println("\nNo services in catalog.");
			} else {
				System.out.println();
				AtomicInteger index = new AtomicInteger(1);

				services.forEach(s -> {
					System.out.printf("%d. [ID:%d] %s\n", index.getAndIncrement(), s.getCatalogId(),
							s.getServiceName());
					System.out.println("   Type: " + s.getServiceType());
					System.out.println("   Car Type: " + s.getCarType());
					System.out.println("   Price: Rs. " + s.getBasePrice());
					System.out.println("   Duration: " + s.getDurationHours() + " hours");
					System.out.println("   Active: " + (s.isActive() ? "Yes" : "No"));
					System.out.println();
				});
			}
		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}
	
	private void viewServiceAdvisors() {
	    System.out.println("SERVICE ADVISORS");

	    try {
	        List<Advisor> advisors = _adminService.getAvailableAdvisors();

	        if (advisors.isEmpty()) {
	            System.out.println("\nNo service advisors found.");
	        } else {
	            System.out.println();
	            AtomicInteger index = new AtomicInteger(1);

	            advisors.forEach(a -> {
	                System.out.printf("%d. %s\n", index.getAndIncrement(), a.toString());
	                System.out.println("   Status: " + a.getAvailabilityStatus());
	                System.out.println("   Email: " + a.getEmail());
	                if (a.getLastAssignedAt() != null) {
	                    System.out.println("   Last Assigned: " + a.getLastAssignedAt());
	                }
	                System.out.println();
	            });
	        }
	    } catch (BusinessLogicException e) {
	        System.out.println("\nError: " + e.getMessage());
	    }
	}

	private void assignBookingToAdvisor() {
	    System.out.println("ASSIGN BOOKING TO ADVISOR");

	    try {
	        // Get confirmed bookings without service records
	        List<Booking> allBookings = _adminService.getAllBookings();
	        List<Booking> confirmedBookings = allBookings.stream()
	        	    .filter(Booking::isAssignable)
	        	    .toList();
	        

	        if (confirmedBookings.isEmpty()) {
	            System.out.println("\nNo bookings available for assignment.");
	            return;
	        }

	        System.out.println("\nBookings:");
	        for (int i = 0; i < confirmedBookings.size(); i++) {
	            Booking b = confirmedBookings.get(i);
	            System.out.printf("%d. [ID:%d] %s - %s on %s\n", 
	                (i + 1), b.getBookingId(), b.getVehicleInfo(), 
	                b.getServiceName(), b.getServiceDate());
	        }

	        int bookingChoice = InputValidator.readInt("\nSelect booking (1-" + 
	            confirmedBookings.size() + "): ");
	        if (bookingChoice < 1 || bookingChoice > confirmedBookings.size()) {
	            System.out.println("\nInvalid selection.");
	            return;
	        }

	        Booking selectedBooking = confirmedBookings.get(bookingChoice - 1);

	        // Get available advisors
	        List<Advisor> advisors = _adminService.getAvailableAdvisors();
	        if (advisors.isEmpty()) {
	            System.out.println("\nNo available advisors.");
	            return;
	        }

	        System.out.println("\nAvailable Advisors:");
	        for (int i = 0; i < advisors.size(); i++) {
	            Advisor a = advisors.get(i);
	            System.out.printf("%d. %s (Load: %d)\n", (i + 1), a.toString(), a.getCurrentLoad());
	        }

	        int advisorChoice = InputValidator.readInt("\nSelect advisor (1-" + 
	            advisors.size() + "): ");
	        if (advisorChoice < 1 || advisorChoice > advisors.size()) {
	            System.out.println("\nInvalid selection.");
	            return;
	        }

	        Advisor selectedAdvisor = advisors.get(advisorChoice - 1);

	        _adminService.assignBookingToAdvisor(selectedBooking.getBookingId(), 
	        	    selectedAdvisor.getAdvisorId(), _currentUser.getUserId());
	        System.out.println("\nBooking assigned. to " + selectedAdvisor.getFullName());

	    } catch (BusinessLogicException e) {
	        System.out.println("\nError: " + e.getMessage());
	    }
	}

	private void viewCompletedServices() {
	    System.out.println("COMPLETED SERVICES");

	    try {
	        List<ServiceRecord> records = _adminService.getCompletedServices();

	        if (records.isEmpty()) {
	            System.out.println("\nNo completed services.");
	        } else {
	            System.out.println();
	            AtomicInteger index = new AtomicInteger(1);

	            records.forEach(r -> {
	                System.out.printf("%d. [ID:%d] %s\n", index.getAndIncrement(), 
	                    r.getServiceId(), r.toString());
	                System.out.println("   Advisor: " + r.getAdvisorName());
	                if (r.getServiceEndDate() != null) {
	                    System.out.println("   Completed: " + r.getServiceEndDate());
	                }
	                System.out.println();
	            });
	        }
	    } catch (BusinessLogicException e) {
	        System.out.println("\nError: " + e.getMessage());
	    }
	}
	

	 /* Update Service Advisor availability status
	 */
	private void updateAdvisorStatus() {
	    System.out.println("UPDATE SERVICE ADVISOR STATUS");

	    try {
	        List<Advisor> advisors = _adminService.getAllAdvisors();

	        if (advisors.isEmpty()) {
	            System.out.println("\nNo service advisors found.");
	            return;
	        }

	        System.out.println("\nService Advisors:");
	        for (int i = 0; i < advisors.size(); i++) {
	            Advisor a = advisors.get(i);
	            System.out.printf("%d. [ID:%d] %s - %s (Status: %s, Load: %d)\n", 
	                (i + 1), a.getAdvisorId(), a.getFullName(), 
	                a.getSpecialization(), a.getAvailabilityStatus(), a.getCurrentLoad());
	        }

	        int choice;
	        while (true) {
	            choice = InputValidator.readInt("\nSelect advisor (1-" + advisors.size() + "): ");
	            if (choice < 1 || choice > advisors.size()) {
	                System.out.println("Invalid selection. Please try again.");
	                continue;
	            }
	            break;
	        }

	        Advisor selectedAdvisor = advisors.get(choice - 1);

	        System.out.println("\nCurrent Status: " + selectedAdvisor.getAvailabilityStatus());
	        System.out.println("""
	                \nSelect New Status:
	                1. AVAILABLE
	                2. ASSIGNED
	                3. ON_LEAVE
	                4. RESIGNED
	                """);

	        int statusChoice;
	        while (true) {
	            statusChoice = InputValidator.readInt("Enter choice (1-4): ");
	            if (statusChoice < 1 || statusChoice > 4) {
	                System.out.println("Invalid choice. Please select 1-4.");
	                continue;
	            }
	            break;
	        }

	        String newStatus = switch (statusChoice) {
	            case 1 -> "AVAILABLE";
	            case 2 -> "ASSIGNED";
	            case 3 -> "ON_LEAVE";
	            case 4 -> "RESIGNED";
	            default -> null;
	        };

	        if ("ASSIGNED".equals(newStatus) && selectedAdvisor.getCurrentLoad() == 0) {
	            System.out.println("\nWARNING: Setting status to ASSIGNED but advisor has no current load.");
	            String confirm = InputValidator.readString("Proceed anyway? (yes/no): ");
	            if (!confirm.equalsIgnoreCase("yes") && !confirm.equalsIgnoreCase("y")) {
	                System.out.println("\nStatus update cancelled.");
	                return;
	            }
	        }

	        if ("AVAILABLE".equals(newStatus) && selectedAdvisor.getCurrentLoad() > 0) {
	            System.out.println("\nWARNING: Advisor has " + selectedAdvisor.getCurrentLoad() + " active assignment(s).");
	            String confirm = InputValidator.readString("Proceed anyway? (yes/no): ");
	            if (!confirm.equalsIgnoreCase("yes") && !confirm.equalsIgnoreCase("y")) {
	                System.out.println("\nStatus update cancelled.");
	                return;
	            }
	        }

	        _adminService.updateAdvisorStatus(selectedAdvisor.getAdvisorId(), newStatus);
	        System.out.println("\nAdvisor status updated to: " + newStatus);

	    } catch (BusinessLogicException e) {
	        System.out.println("\nError: " + e.getMessage());
	    }
	}

private void generateInvoice() {
    System.out.println("GENERATE INVOICE");

    try {
        List<ServiceRecord> records = _adminService.getCompletedServices();

        List<ServiceRecord> recordsWithoutInvoice = new ArrayList<>();
        for (ServiceRecord record : records) {
            try {
                Invoice existing = _invoiceService.getInvoiceByServiceId(record.getServiceId());
                if (existing == null) {
                    recordsWithoutInvoice.add(record);
                }
            } catch (BusinessLogicException e) {
                // If invoice doesn't exist, add to list
                recordsWithoutInvoice.add(record);
            }
        }

        if (recordsWithoutInvoice.isEmpty()) {
            System.out.println("\nNo completed services without invoices.");
            return;
        }

        System.out.println("\nCompleted Services:");
        for (int i = 0; i < recordsWithoutInvoice.size(); i++) {
            System.out.printf("%d. %s - %s\n", (i + 1), 
                recordsWithoutInvoice.get(i).getVehicleInfo(), 
                recordsWithoutInvoice.get(i).getServiceName());
        }

        int choice = InputValidator.readInt("\nSelect service (1-" + recordsWithoutInvoice.size() + "): ");
        if (choice < 1 || choice > recordsWithoutInvoice.size()) {
            System.out.println("\nInvalid selection.");
            return;
        }

        ServiceRecord selected = recordsWithoutInvoice.get(choice - 1);

        Invoice invoice = _invoiceService.generateInvoice(selected.getServiceId());
        System.out.println("\nInvoice generated successfully.");
        System.out.println("Invoice ID: " + invoice.getInvoiceId());
        System.out.println("Total Amount: Rs. " + invoice.getTotalAmount());

    } catch (BusinessLogicException e) {
        System.out.println("\nError: " + e.getMessage());
    }
}

	private void processPayment() {
	    System.out.println("PROCESS PAYMENT");

	    try {
	        List<Invoice> invoices = _adminService.getAllInvoices();
	        List<Invoice> pending = invoices.stream()
	            .filter(inv -> !inv.getPaymentStatus().equals("PAID"))
	            .toList();

	        if (pending.isEmpty()) {
	            System.out.println("\nNo pending invoices.");
	            return;
	        }

	        System.out.println("\nPending Invoices:");
	        for (int i = 0; i < pending.size(); i++) {
	            System.out.printf("%d. %s\n", (i + 1), pending.get(i).toString());
	        }

	        int choice = InputValidator.readInt("\nSelect invoice (1-" + pending.size() + "): ");
	        if (choice < 1 || choice > pending.size()) {
	            System.out.println("\nInvalid selection.");
	            return;
	        }

	        Invoice selected = pending.get(choice - 1);

	        System.out.println("\n" + selected.toString());
	        System.out.println("Customer: " + selected.getCustomerName());

	        System.out.println("""
	            
	            Payment Method:
	            1. UPI
	            2. CARD
	            3. NET_BANKING
	            4. CASH
	            """);

	        int methodChoice = InputValidator.readInt("Select method (1-4): ");
	        String paymentMethod = switch (methodChoice) {
	            case 1 -> "UPI";
	            case 2 -> "CARD";
	            case 3 -> "NET_BANKING";
	            case 4 -> "CASH";
	            default -> null;
	        };

	        if (paymentMethod == null) {
	            System.out.println("\nInvalid payment method.");
	            return;
	        }

	        String transactionRef = InputValidator.readString("Transaction Reference (optional): ");
	        
	        _invoiceService.processPayment(selected.getInvoiceId(), paymentMethod, 
	            selected.getTotalAmount(), transactionRef);
	        System.out.println("\nPayment processed");

	    } catch (BusinessLogicException e) {
	        System.out.println("\nError: " + e.getMessage());
	    }
	}

	private void viewAllInvoices() {
	    System.out.println("ALL INVOICES");

	    try {
	        List<Invoice> invoices = _adminService.getAllInvoices();

	        if (invoices.isEmpty()) {
	            System.out.println("\nNo invoices found.");
	        } else {
	            System.out.println();

	            invoices.forEach(inv -> {
	                System.out.printf("%s\n", inv.toString());
	                System.out.println("   Customer: " + inv.getCustomerName());
	                System.out.println("   Date: " + inv.getInvoiceDate());
	                System.out.println();
	            });
	        }
	    } catch (BusinessLogicException e) {
	        System.out.println("\nError: " + e.getMessage());
	    }
	}

	/**
	 * View system statistics
	 */
	private void viewSystemStatistics() {
		System.out.println("SYSTEM STATISTICS");

		try {
			System.out.println("\nTotal Users: " + _adminService.getTotalUsers());
			System.out.println("Total Vehicles: " + _adminService.getTotalVehicles());
			System.out.println("Total Bookings: " + _adminService.getTotalBookings());
			System.out.println("Active Bookings: " + _adminService.getActiveBookings());
			System.out.println("Completed Bookings: " + _adminService.getCompletedBookings());
			System.out.println("Cancelled Bookings: " + _adminService.getCancelledBookings());

		} catch (BusinessLogicException e) {
			System.out.println("\nError: " + e.getMessage());
		}
	}
	
	/**
	 * View booking history
	 */
	private void viewBookingHistory() {
	    System.out.println("BOOKING HISTORY (AUDIT TRAIL)");

	    try {
	        List<Booking> bookings = _adminService.getAllBookings();

	        if (bookings.isEmpty()) {
	            System.out.println("\nNo bookings found.");
	            return;
	        }

	        System.out.println("\nAll Bookings:");
	        for (int i = 0; i < bookings.size(); i++) {
	            System.out.printf("%d. [ID:%d] %s\n", (i + 1), 
	                bookings.get(i).getBookingId(), 
	                bookings.get(i).getDisplayInfo());
	        }

	        int choice = InputValidator.readInt("\nSelect booking to view history (1-" + 
	                                           bookings.size() + "): ");
	        if (choice < 1 || choice > bookings.size()) {
	            System.out.println("\nInvalid selection.");
	            return;
	        }

	        Booking selectedBooking = bookings.get(choice - 1);
	        
	        // Admin can view any booking history
	        List<BookingHistory> history = _historyService.getBookingHistory(
	            selectedBooking.getBookingId(), 
	            _currentUser.getUserId(),
	            _currentUser.getRoleName());

	        if (history.isEmpty()) {
	            System.out.println("\nNo history records found for this booking.");
	        } else {
	            System.out.println("\nBOOKING AUDIT TRAIL");
	            System.out.println("Booking ID: " + selectedBooking.getBookingId());
	            System.out.println("Vehicle: " + selectedBooking.getVehicleInfo());
	            System.out.println("Service: " + selectedBooking.getServiceName());
	            System.out.println("Status: " + selectedBooking.getBookingStatus());
	            System.out.println();
	            
	            AtomicInteger index = new AtomicInteger(1);
	            history.forEach(h -> {
	                System.out.printf("%d. %s\n", index.getAndIncrement(), h.toString());
	                System.out.println();
	            });
	        }

	    } catch (BusinessLogicException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}
	
	/**
	 * View all history by action type
	 */
	private void viewHistoryByActionType() {
	    System.out.println("VIEW HISTORY BY ACTION TYPE");

	    System.out.println("""
	            \nAction Types:
	            1. CREATED
	            2. RESCHEDULED
	            3. CANCELLED
	            4. CONFIRMED
	            """);

	    int choice = InputValidator.readInt("Select action type (1-4): ");
	    
	    String actionType = switch (choice) {
	        case 1 -> "CREATED";
	        case 2 -> "RESCHEDULED";
	        case 3 -> "CANCELLED";
	        case 4 -> "CONFIRMED";
	        default -> null;
	    };

	    if (actionType == null) {
	        System.out.println("\nInvalid choice.");
	        return;
	    }

	    try {
	        List<BookingHistory> history = _historyService.getHistoryByActionType(
	            actionType, _currentUser.getRoleName());

	        if (history.isEmpty()) {
	            System.out.println("\nNo " + actionType + " actions found.");
	        } else {
	            System.out.println("\n" + actionType + " ACTIONS:");
	            System.out.println();
	            
	            AtomicInteger index = new AtomicInteger(1);
	            history.forEach(h -> {
	                System.out.printf("%d. Booking #%d - %s\n", 
	                    index.getAndIncrement(), h.getBookingId(), h.toString());
	                System.out.println();
	            });
	        }

	    } catch (BusinessLogicException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}
	
}