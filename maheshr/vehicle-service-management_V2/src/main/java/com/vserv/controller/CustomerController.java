package com.vserv.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.vserv.exception.BusinessLogicException;
import com.vserv.model.*;
import com.vserv.service.*;
import com.vserv.util.*;

/**
 * Handle customer workflow
 * 
 * @author Mahesh R
 */
public class CustomerController {
	
	private static final int MAX_RETRIES = 3;
	private User _currentUser;
	private VehicleService _vehicleService;
	private BookingService _bookingService;
	private BookingHistoryService _historyService;
	private InvoiceService _invoiceService;
	private FeedbackService _feedbackService;
	private NotificationService _notificationService;
	

	public CustomerController(User user) {
	    AuthorizationUtil.requireRole(user, "CUSTOMER");
	    this._currentUser = user;
	    this._vehicleService = new VehicleService();
	    this._bookingService = new BookingService();
	    this._historyService = new BookingHistoryService();
	    this._invoiceService = new InvoiceService();
	    this._feedbackService = new FeedbackService();
	    this._notificationService = new NotificationService();
	}

public void showDashboard() {
    String notificationBadge = "";
    
    try {
        int unreadCount = _notificationService.getUnreadCount(_currentUser.getUserId());
        notificationBadge = unreadCount > 0 ? " (" + unreadCount + " unread)" : "";
    } catch (Exception e) {
    }

    String menu = """

            CUSTOMER DASHBOARD

            Welcome, %s

            1. My Cars
            2. Add New Car
            3. Update Car
            4. Delete Car
            5. Browse Services
            6. Book Service
            7. My Bookings
            8. Reschedule Booking
            9. Cancel Booking
            10. View Invoice Details
            11. Submit Feedback
            12. My Feedback History
            13. View Notifications%s
            14. View Unread Notifications
            15. Mark All Notifications as Read
            16. View Booking History
            0. Logout

            """.formatted(_currentUser.getFullName(), notificationBadge);

    while (true) {
        System.out.print(menu);
        int choice = InputValidator.readInt("Enter your choice: ");

        switch (choice) {
            case 1 -> viewVehicles();
            case 2 -> addVehicle();
            case 3 -> updateVehicle();
            case 4 -> deleteVehicle();
            case 5 -> browseServices();
            case 6 -> bookService();
            case 7 -> viewBookings();
            case 8 -> rescheduleBooking();
            case 9 -> cancelBooking();
            case 10 -> viewInvoiceDetails();
            case 11 -> submitFeedback();
            case 12 -> viewMyFeedback();
            case 13 -> viewNotifications();
            case 14 -> viewUnreadNotifications();
            case 15 -> markAllNotificationsRead();
            case 16 -> viewBookingHistory();
            case 0 -> {
                return;
            }
            default -> System.out.println("\nInvalid choice. Please try again.");
        }
    }
}

	private void viewVehicles() {

		System.out.print("MY VEHICLES");

		try {
			List<Vehicle> vehicles = _vehicleService.getVehiclesByUser(_currentUser.getUserId());

			if (vehicles.isEmpty()) {
				System.out.println("\nNo vehicles registered. Please add a vehicle.");
			} else {
				System.out.println();

				vehicles.stream().forEach(v -> {
					System.out.printf("%s\n", v.toString());
					System.out.println("   Registration: " + v.getRegistrationNumber());
					System.out.println("   Year: " + v.getManufactureYear());

					if (v.getNextServiceDue() != null) {
						System.out.println("   Next Service Due: " + v.getNextServiceDue());
					}
					System.out.println();
				});
			}

		} catch (BusinessLogicException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void addVehicle() {

		System.out.println("""
				ADD NEW VEHICLE

				Car Types:
				1. SEDAN
				2. SUV
				3. HATCHBACK
				4. COUPE
				5. CONVERTIBLE
				6. WAGON
				7. MINIVAN
				""");

		String carType = null;
		for (int i = 0; i < MAX_RETRIES; i++) {
			int typeChoice = InputValidator.readInt("\nSelect car type (1-7): ");

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

			if (carType != null) {
				break;
			}
			System.out.println("Invalid choice. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
			if (i == MAX_RETRIES - 1) {
				System.out.println("\nToo many invalid attempts. Returning to dashboard.");
				return;
			}
		}

		String brand = null;
		for (int i = 0; i < MAX_RETRIES; i++) {
			brand = InputValidator.readString("Brand: ");
			if (!brand.trim().isEmpty()) {
				break;
			}
			System.out.println("Brand cannot be empty. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
			if (i == MAX_RETRIES - 1) {
				System.out.println("\nToo many invalid attempts. Returning to dashboard.");
				InputValidator.load();
				return;
			}
		}

		String model = null;
		for (int i = 0; i < MAX_RETRIES; i++) {
			model = InputValidator.readString("Model: ");
			if (!model.trim().isEmpty()) {
				break;
			}
			System.out.println("Model cannot be empty. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
			if (i == MAX_RETRIES - 1) {
				System.out.println("\nToo many invalid attempts. Returning to dashboard.");
				InputValidator.load();
				return;
			}
		}

		String regNumber = null;
		for (int i = 0; i < MAX_RETRIES; i++) {
			regNumber = InputValidator.readString("Registration Number (TN55AK0915): ");
			if (FieldValidator.isValidRegistrationNumber(regNumber)) {
				break;
			}
			System.out.println("Invalid format. Use format: TN55AK0915. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
			if (i == MAX_RETRIES - 1) {
				System.out.println("\nToo many invalid attempts. Returning to dashboard.");
				InputValidator.load();
				return;
			}
		}

		Integer year = null;
		for (int i = 0; i < MAX_RETRIES; i++) {
			int inputYear = InputValidator.readInt("Manufacture Year: ");
			if (FieldValidator.isValidYear(inputYear)) {
				year = inputYear;
				break;
			}
			System.out.println("Invalid year. Must be between 1900 and current year. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
			if (i == MAX_RETRIES - 1) {
				System.out.println("\nToo many invalid attempts. Returning to dashboard.");
				InputValidator.load();
				return;
			}
		}

		Integer mileage = null;
		for (int i = 0; i < MAX_RETRIES; i++) {
			int inputMileage = InputValidator.readInt("Mileage (km): ");
			if (inputMileage >= 0) {
				mileage = inputMileage;
				break;
			}
			System.out.println("Mileage cannot be negative. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
			if (i == MAX_RETRIES - 1) {
				System.out.println("\nToo many invalid attempts. Returning to dashboard.");
				InputValidator.load();
				return;
			}
		}

		try {
			Vehicle vehicle = _vehicleService.addVehicle(_currentUser.getUserId(), carType, brand, 
					model, regNumber, year, mileage);
			System.out.println("\nCar added.");
			System.out.println("Vehicle ID: " + vehicle.getVehicleId());
		} catch (BusinessLogicException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private void updateVehicle() {
		System.out.println("UPDATE CAR");

		try {
			List<Vehicle> vehicles = _vehicleService.getVehiclesByUser(_currentUser.getUserId());

			if (vehicles.isEmpty()) {
				System.out.println("\nNo cars found.");
				return;
			}

			System.out.println("\nYour Cars:");
			for (int i = 0; i < vehicles.size(); i++) {
				System.out.printf("%d. %s\n", (i + 1), vehicles.get(i).toString());
			}

			Vehicle vehicle = null;
			for (int i = 0; i < MAX_RETRIES; i++) {
				int choice = InputValidator.readInt("\nSelect car to update (1-" + vehicles.size() + "): ");
				if (choice >= 1 && choice <= vehicles.size()) {
					vehicle = vehicles.get(choice - 1);
					break;
				}
				System.out.println("Invalid selection. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
				if (i == MAX_RETRIES - 1) {
					System.out.println("\nToo many invalid attempts. Returning to dashboard.");
					return;
				}
			}

			System.out.println("\nCurrent Details:");
			System.out.println("Car Type: " + vehicle.getCarType());
			System.out.println("Brand: " + vehicle.getBrand());
			System.out.println("Model: " + vehicle.getModel());
			System.out.println("Year: " + vehicle.getManufactureYear());
			System.out.println("Mileage: " + vehicle.getMileage() + " km");

			System.out.println("\nCar Types:");
			System.out.println("1. SEDAN  2. SUV  3. HATCHBACK  4. COUPE");
			System.out.println("5. CONVERTIBLE  6. WAGON  7. MINIVAN");

			String carType = null;
			for (int i = 0; i < MAX_RETRIES; i++) {
				int typeChoice = InputValidator.readInt("\nSelect car type (1-7): ");
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

				if (carType != null) {
					break;
				}
				System.out.println("Invalid choice. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
				if (i == MAX_RETRIES - 1) {
					System.out.println("\nToo many invalid attempts. Returning to dashboard.");
					return;
				}
			}

			String brand = null;
			for (int i = 0; i < MAX_RETRIES; i++) {
				brand = InputValidator.readString("Brand [" + vehicle.getBrand() + "]: ");
				if (brand.isEmpty()) {
					brand = vehicle.getBrand();
					break;
				}
				if (!brand.trim().isEmpty()) {
					break;
				}
				System.out.println("Brand cannot be empty. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
				if (i == MAX_RETRIES - 1) {
					System.out.println("\nToo many invalid attempts. Returning to dashboard.");
					return;
				}
			}

			String model = null;
			for (int i = 0; i < MAX_RETRIES; i++) {
				model = InputValidator.readString("Model [" + vehicle.getModel() + "]: ");
				if (model.isEmpty()) {
					model = vehicle.getModel();
					break;
				}
				if (!model.trim().isEmpty()) {
					break;
				}
				System.out.println("Model cannot be empty. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
				if (i == MAX_RETRIES - 1) {
					System.out.println("\nToo many invalid attempts. Returning to dashboard.");
					return;
				}
			}

			Integer year = null;
			for (int i = 0; i < MAX_RETRIES; i++) {
				int inputYear = InputValidator.readInt("Manufacture Year [" + vehicle.getManufactureYear() + "]: ");
				if (FieldValidator.isValidYear(inputYear)) {
					year = inputYear;
					break;
				}
				System.out.println("Invalid year. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
				if (i == MAX_RETRIES - 1) {
					System.out.println("\nToo many invalid attempts. Returning to dashboard.");
					return;
				}
			}

			Integer mileage = null;
			for (int i = 0; i < MAX_RETRIES; i++) {
				int inputMileage = InputValidator.readInt("Current Mileage [" + vehicle.getMileage() + "]: ");
				if (inputMileage >= 0) {
					mileage = inputMileage;
					break;
				}
				System.out.println("Mileage cannot be negative. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
				if (i == MAX_RETRIES - 1) {
					System.out.println("\nToo many invalid attempts. Returning to dashboard.");
					return;
				}
			}

			_vehicleService.updateVehicle(vehicle.getVehicleId(), _currentUser.getUserId(),
					carType, brand, model, year, mileage);

			System.out.println("\nCar updated");

		} catch (BusinessLogicException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private void deleteVehicle() {
		System.out.println("DELETE CAR");

		try {
			List<Vehicle> vehicles = _vehicleService.getVehiclesByUser(_currentUser.getUserId());

			if (vehicles.isEmpty()) {
				System.out.println("\nNo cars found.");
				return;
			}

			System.out.println("\nYour Cars:");
			for (int i = 0; i < vehicles.size(); i++) {
				System.out.printf("%d. %s\n", (i + 1), vehicles.get(i).toString());
			}

			int choice = InputValidator.readInt("\nSelect car to delete (1-" + vehicles.size() + "): ");
			if (choice < 1 || choice > vehicles.size()) {
				System.out.println("\nInvalid selection.");
				return;
			}

			Vehicle vehicle = vehicles.get(choice - 1);

			System.out.println("\n WARNING: This will delete:");
			System.out.println(vehicle.toString());

			String confirm = InputValidator.readString("\nType 'DELETE' to confirm: ");

			if (confirm.equals("DELETE")) {
				_vehicleService.deleteVehicle(vehicle.getVehicleId(), _currentUser.getUserId());
				System.out.println("\nCar deleted.");
			} else {
				System.out.println("\nDeletion cancelled.");
			}

		} catch (BusinessLogicException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

private void browseServices() {

    System.out.println("AVAILABLE SERVICES");

    try {
        List<Catalog> services = _bookingService.getAllServices();

        if (services.isEmpty()) {
            System.out.println("\nNo services available at the moment.");
        } else {
            System.out.println();
            services.stream()
                .forEach(service -> {
                    System.out.printf("%s\n", service.toString());
                    System.out.println("   Type: " + service.getServiceType());
                    System.out.println("   Description: " + service.getDescription());
                    System.out.println();
                });
        }

    } catch (BusinessLogicException e) {
        System.out.println("Error: " + e.getMessage());
    }
}


	private void bookService() {

		System.out.println("BOOK SERVICE");

		try {
			List<Vehicle> vehicles = _vehicleService.getVehiclesByUser(_currentUser.getUserId());

			if (vehicles.isEmpty()) {
				System.out.println("\nNo cars found. Please add a car first.");
				InputValidator.load();
				return;
			}

			System.out.println("\nYour Cars:");
			for (int i = 0; i < vehicles.size(); i++) {
				System.out.printf("%d. %s\n", (i + 1), vehicles.get(i).toString());
			}

			Vehicle selectedVehicle = null;
			for (int i = 0; i < MAX_RETRIES; i++) {
				int vehicleChoice = InputValidator.readInt("\nSelect car (1-" + vehicles.size() + "): ");
				if (vehicleChoice >= 1 && vehicleChoice <= vehicles.size()) {
					selectedVehicle = vehicles.get(vehicleChoice - 1);
					break;
				}
				System.out.println("Invalid selection. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
				if (i == MAX_RETRIES - 1) {
					System.out.println("\nToo many invalid attempts. Returning to dashboard.");
					return;
				}
			}

			List<Catalog> services = _bookingService.getServicesByCarType(selectedVehicle.getCarType());

			if (services.isEmpty()) {
				System.out.println("\nNo services available for " + selectedVehicle.getCarType());
				return;
			}

			System.out.println("\nAvailable Services for " + selectedVehicle.getCarType() + " Cars:");
			for (int i = 0; i < services.size(); i++) {
				System.out.printf("%d. %s\n", (i + 1), services.get(i).toString());
			}

			Catalog selectedService = null;
			for (int i = 0; i < MAX_RETRIES; i++) {
				int serviceChoice = InputValidator.readInt("\nSelect service (1-" + services.size() + "): ");
				if (serviceChoice >= 1 && serviceChoice <= services.size()) {
					selectedService = services.get(serviceChoice - 1);
					break;
				}
				System.out.println("Invalid selection. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
				if (i == MAX_RETRIES - 1) {
					System.out.println("\nToo many invalid attempts. Returning to dashboard.");
					return;
				}
			}

			LocalDate serviceDate = null;
			
			for (int i = 0; i < MAX_RETRIES; i++) {
				serviceDate = InputValidator.readDate("\nEnter service date");
				if (FieldValidator.isFutureDate(serviceDate) || serviceDate.equals(LocalDate.now())) {
					break;
				}
				System.out.println("Cannot book for past dates. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
				if (i == MAX_RETRIES - 1) {
					System.out.println("\nToo many invalid attempts. Returning to dashboard.");
					return;
				}
			}

			List<Availability> slots = _bookingService.getAvailableSlots(serviceDate);
			LocalDate finalDate = serviceDate;
			List<Availability> slotsForDate = slots.stream()
					.filter(s -> s.getServiceDate().equals(finalDate))
					.toList();

			if (slotsForDate.isEmpty()) {
				System.out.println("\nNo slots available for " + serviceDate);
				return;
			}

			System.out.println("\nAvailable Time Slots:");
			for (int i = 0; i < slotsForDate.size(); i++) {
				Availability slot = slotsForDate.get(i);
				System.out.printf("%d. %s (Slots remaining: %d)\n", (i + 1), slot.getTimeSlot(), slot.getSlotsRemaining());
			}

			Availability selectedSlot = null;
			for (int i = 0; i < MAX_RETRIES; i++) {
				int slotChoice = InputValidator.readInt("\nSelect time slot (1-" + slotsForDate.size() + "): ");
				if (slotChoice >= 1 && slotChoice <= slotsForDate.size()) {
					selectedSlot = slotsForDate.get(slotChoice - 1);
					break;
				}
				System.out.println("Invalid selection. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
				if (i == MAX_RETRIES - 1) {
					System.out.println("\nToo many invalid attempts. Returning to dashboard.");
					return;
				}
			}

			String notes = InputValidator.readString("\nAdd any notes (optional): ");

			System.out.println();
			System.out.println("BOOKING SUMMARY");
			System.out.println();
			System.out.println("Vehicle: " + selectedVehicle.toString());
			System.out.println("Service: " + selectedService.getServiceName());
			System.out.println("Price: Rs. " + selectedService.getBasePrice());
			System.out.println("Date: " + serviceDate);
			System.out.println("Time: " + selectedSlot.getTimeSlot());
			System.out.println("Duration: " + selectedService.getDurationHours() + " hours");
			System.out.println();

			String confirm = InputValidator.readString("\nConfirm booking? (yes/no): ");

			if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
				Booking booking = _bookingService.createBooking(
						selectedVehicle.getVehicleId(),
						selectedService.getCatalogId(),
						serviceDate,
						selectedSlot.getTimeSlot(),
						notes);

				System.out.println("\nBooking confirmed");
				System.out.println("Booking ID: " + booking.getBookingId());
				System.out.println("Status: " + booking.getBookingStatus());
			} else {
				System.out.println("\nBooking cancelled.");
			}

		} catch (BusinessLogicException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

private void viewBookings() {

	System.out.println("MY BOOKINGS");

    try {
        List<Booking> bookings = _bookingService.getBookingsByUser(_currentUser.getUserId());

        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings found.");
        } else {
            System.out.println("""
                    \nSort by:
                    1. Date
                    2. Status
                    """);

            int sortChoice = InputValidator.readInt("Choice (default 1): ");

            if (sortChoice == 2) {
                bookings.sort(BookingComparator.BY_STATUS);
            } else {
                bookings.sort(BookingComparator.BY_DATE);
            }

            System.out.println();
            bookings.stream()
                .forEach(b -> {
                    System.out.printf("%s\n", b.getDisplayInfo());
                    if (b.getBookingNotes() != null && !b.getBookingNotes().isEmpty()) {
                        System.out.println("   Notes: " + b.getBookingNotes());
                    }
                    System.out.println("   Upcoming: " + (b.isUpcoming() ? "Yes" : "No"));
                    System.out.println();
                });
        }
    } catch (BusinessLogicException e) {
        System.out.println("Error: " + e.getMessage());
    }
}


private void viewBookingHistory() {
    System.out.println("BOOKING HISTORY");

    try {
        List<Booking> bookings = _bookingService.getBookingsByUser(_currentUser.getUserId());

        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings found.");
            return;
        }

        System.out.println("\nYour Bookings:");
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
        
        List<BookingHistory> history = _historyService.getBookingHistory(
            selectedBooking.getBookingId(), 
            _currentUser.getUserId(),
            _currentUser.getRoleName());

        if (history.isEmpty()) {
            System.out.println("\nNo history records found for this booking.");
        } else {
            System.out.println("\nBOOKING HISTORY");
            System.out.println("Booking ID: " + selectedBooking.getBookingId());
            System.out.println("Vehicle: " + selectedBooking.getVehicleInfo());
            System.out.println();
            
            history.forEach(h -> {
                System.out.printf("%s\n", h.toString());
                System.out.println();
            });
        }

    } catch (BusinessLogicException e) {
        System.out.println("Error: " + e.getMessage());
    }
}


	private void rescheduleBooking() {
		System.out.println("RESCHEDULE BOOKING");

		try {
			List<Booking> bookings = _bookingService.getBookingsByUser(_currentUser.getUserId());

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

			int choice = InputValidator.readInt("\nSelect booking (1-" + activeBookings.size() + "): ");
			if (choice < 1 || choice > activeBookings.size()) {
				System.out.println("\nInvalid selection.");
				return;
			}

			Booking booking = activeBookings.get(choice - 1);

			LocalDate newDate = InputValidator.readDate("\nEnter new service date");

			List<Availability> slots = _bookingService.getAvailableSlots(newDate);

			List<Availability> slotsForDate = slots.stream().filter(s -> s.getServiceDate().equals(newDate))
					.toList();

			if (slotsForDate.isEmpty()) {
				System.out.println("\nNo slots available for " + newDate);
				return;
			}

			System.out.println("\nAvailable Time Slots:");
			for (int i = 0; i < slotsForDate.size(); i++) {
				Availability slot = slotsForDate.get(i);
				System.out.printf("%d. %s (Slots remaining: %d)\n", (i + 1), slot.getTimeSlot(),
						slot.getSlotsRemaining());
			}

			int slotChoice = InputValidator.readInt("\nSelect time slot (1-" + slotsForDate.size() + "): ");
			if (slotChoice < 1 || slotChoice > slotsForDate.size()) {
				System.out.println("\nInvalid selection.");
				return;
			}

			Availability selectedSlot = slotsForDate.get(slotChoice - 1);

			System.out.println("\nOld: " + booking.getServiceDate() + " at " + booking.getTimeSlot());
			System.out.println("New: " + newDate + " at " + selectedSlot.getTimeSlot());

			String confirm = InputValidator.readString("\nConfirm reschedule? (yes/no): ");

			if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
				_bookingService.rescheduleBooking(booking.getBookingId(), newDate, selectedSlot.getTimeSlot());
				System.out.println("\nBooking rescheduled.");
			} else {
				System.out.println("\nReschedule cancelled.");
			}

		} catch (BusinessLogicException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void cancelBooking() {
		System.out.println("CANCEL BOOKING");

		try {
			List<Booking> bookings = _bookingService.getBookingsByUser(_currentUser.getUserId());

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
				System.out.printf("%d. [ID:%d] %s - %s on %s at %s\n", (i + 1), b.getBookingId(), b.getVehicleInfo(),
						b.getServiceName(), b.getServiceDate(), b.getTimeSlot());
			}

			int choice = InputValidator.readInt("\nSelect booking to cancel (1-" + activeBookings.size() + "): ");
			if (choice < 1 || choice > activeBookings.size()) {
				System.out.println("\nInvalid selection.");
				return;
			}

			Booking booking = activeBookings.get(choice - 1);

			System.out.println("\nWARNING: This will cancel:");
			System.out.println(booking.getVehicleInfo() + " - " + booking.getServiceName());
			System.out.println(booking.getServiceDate() + " at " + booking.getTimeSlot());

			String confirm = InputValidator.readString("\nType 'CANCEL' to confirm: ");

			if (confirm.equals("CANCEL")) {
				_bookingService.cancelBooking(booking.getBookingId());
				System.out.println("\nBooking cancelled.");
			} else {
				System.out.println("\nCancellation aborted.");
			}

		} catch (BusinessLogicException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
//	private void viewInvoices() {
//	    System.out.println("MY INVOICES");
//
//	    try {
//	        List<Invoice> invoices = _invoiceService.getCustomerInvoices(_currentUser.getUserId());
//
//	        if (invoices.isEmpty()) {
//	            System.out.println("\nNo invoices found.");
//	        } else {
//	            System.out.println();
//	            invoices.stream()
//	                .forEach(inv -> {
//	                    System.out.printf("%s\n", inv.toString());
//	                    System.out.println("   Date: " + inv.getInvoiceDate());
//	                    System.out.println();
//	                });
//	        }
//	    } catch (BusinessLogicException e) {
//	        System.out.println("Error: " + e.getMessage());
//	    }
//	}

	private void viewInvoiceDetails() {
	    System.out.println("INVOICE DETAILS");

	    try {
	        List<Invoice> invoices = _invoiceService.getCustomerInvoices(_currentUser.getUserId());

	        if (invoices.isEmpty()) {
	            System.out.println("\nNo invoices found.");
	            return;
	        }

	        System.out.println("\nYour Invoices:");
	        for (int i = 0; i < invoices.size(); i++) {
	            System.out.printf("%d. Invoice #%d - %s [%s]\n", (i + 1), 
	                invoices.get(i).getInvoiceId(), 
	                invoices.get(i).getVehicleInfo(),
	                invoices.get(i).getPaymentStatus());
	        }

	        int choice = InputValidator.readInt("\nSelect invoice (1-" + invoices.size() + "): ");
	        if (choice < 1 || choice > invoices.size()) {
	            System.out.println("\nInvalid selection.");
	            return;
	        }

	        Invoice invoice = invoices.get(choice - 1);

	        System.out.println("\nINVOICE #" + invoice.getInvoiceId());
	        System.out.println("Vehicle: " + invoice.getVehicleInfo());
	        System.out.println("Service: " + invoice.getServiceName());
	        System.out.println("Date: " + invoice.getInvoiceDate());

	        System.out.println("\n-- Billing Breakdown --");
	        System.out.println("Service Items: Rs. " + invoice.getItemsTotal());

	        if (invoice.getOvertimeCharge().compareTo(BigDecimal.ZERO) > 0) {
	            System.out.println("Overtime Charges: Rs. " + invoice.getOvertimeCharge());
	        }

	        System.out.println("-------------------------");
	        System.out.println("Total Amount: Rs. " + invoice.getTotalAmount());
	        System.out.println("Payment Status: " + invoice.getPaymentStatus());

	        List<Payment> payments = _invoiceService.getInvoicePayments(invoice.getInvoiceId());
	        if (!payments.isEmpty()) {
	            System.out.println("\nPayments:");
	            payments.forEach(p -> System.out.println("  " + p.toString()));
	        }

	    } catch (BusinessLogicException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}

	private void submitFeedback() {
	    System.out.println("SUBMIT FEEDBACK");

	    try {
	        List<Invoice> invoices = _invoiceService.getCustomerInvoices(_currentUser.getUserId());
	        List<Invoice> paid = invoices.stream()
	            .filter(inv -> inv.getPaymentStatus().equals("PAID"))
	            .toList();

	        if (paid.isEmpty()) {
	            System.out.println("\nNo completed services available for feedback.");
	            return;
	        }

	        System.out.println("\nCompleted Services:");
	        for (int i = 0; i < paid.size(); i++) {
	            System.out.printf("%d. %s - %s\n", (i + 1), 
	                paid.get(i).getVehicleInfo(), paid.get(i).getServiceName());
	        }

	        int choice = InputValidator.readInt("\nSelect service (1-" + paid.size() + "): ");
	        if (choice < 1 || choice > paid.size()) {
	            System.out.println("\nInvalid selection.");
	            return;
	        }

	        Invoice selected = paid.get(choice - 1);

	        System.out.println("\nRating (1-5 stars): ");
	        int rating = InputValidator.readInt("Rating: ");
	        if (rating < 1 || rating > 5) {
	            System.out.println("\nInvalid rating.");
	            return;
	        }

	        String feedbackText = InputValidator.readString("Feedback (optional): ");

	        _feedbackService.submitFeedback(selected.getServiceId(), 
	            _currentUser.getUserId(), rating, feedbackText);
	        System.out.println("\nFeedback submitted.");

	    } catch (BusinessLogicException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}

	private void viewMyFeedback() {
	    System.out.println("MY FEEDBACK HISTORY");

	    try {
	        List<Feedback> feedbacks = _feedbackService.getCustomerFeedback(_currentUser.getUserId());

	        if (feedbacks.isEmpty()) {
	            System.out.println("\nNo feedback submitted yet.");
	        } else {
	            System.out.println();
	            feedbacks.stream()
	                .forEach(f -> {
	                    System.out.printf("%s\n", f.toString());
	                    System.out.println("   Submitted: " + f.getSubmittedAt());
	                    System.out.println();
	                });
	        }
	    } catch (BusinessLogicException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}
	
	private void viewNotifications() {
	    System.out.println("MY NOTIFICATIONS");

	    try {
	        List<Notification> notifications = _notificationService.getUserNotifications(_currentUser.getUserId());

	        if (notifications.isEmpty()) {
	            System.out.println("\nNo notifications.");
	        } else {
	            System.out.println();
	            for (int i = 0; i < notifications.size(); i++) {
	                Notification n = notifications.get(i);
	                System.out.printf("%d. %s\n", (i + 1), n.toString());
	                System.out.println("   " + n.getMessage());
	                System.out.println();
	            }

	            String viewDetails = InputValidator.readString("\nMark notification as read? (enter number or 'no'): ");
	            if (!viewDetails.equalsIgnoreCase("no") && !viewDetails.isEmpty()) {
	                try {
	                    int choice = Integer.parseInt(viewDetails);
	                    if (choice >= 1 && choice <= notifications.size()) {
	                        Notification selected = notifications.get(choice - 1);
	                        _notificationService.markAsRead(selected.getNotificationId());
	                        System.out.println("\nMarked as read.");
	                    }
	                } catch (NumberFormatException e) {
	                }
	            }
	        }
	    } catch (BusinessLogicException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}

	private void viewUnreadNotifications() {
	    System.out.println("UNREAD NOTIFICATIONS");

	    try {
	        List<Notification> notifications = _notificationService.getUnreadNotifications(_currentUser.getUserId());

	        if (notifications.isEmpty()) {
	            System.out.println("\nNo unread notifications.");
	        } else {
	            System.out.println();
	            notifications.forEach(n -> {
	                System.out.printf("%s\n", n.toString());
	                System.out.println("   " + n.getMessage());
	                System.out.println();
	            });
	        }
	    } catch (BusinessLogicException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}

	private void markAllNotificationsRead() {
	    try {
	        _notificationService.markAllAsRead(_currentUser.getUserId());
	        System.out.println("\nAll notifications marked as read.");
	    } catch (BusinessLogicException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}
}