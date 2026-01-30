package com.vserv.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.vserv.dao.impl.*;
import com.vserv.dao.interfaces.*;
import com.vserv.exception.BusinessLogicException;
import com.vserv.model.*;
import com.vserv.util.PasswordHash;

public class AdminService {
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private VehicleDAO vehicleDAO;
    private BookingDAO bookingDAO;
    private CatalogDAO catalogDAO;
    private AvailabilityDAO availabilityDAO;
    private AdvisorDAO advisorDAO;
    private RecordDAO recordDAO;
    private InvoiceDAO invoiceDAO;

    public AdminService() {
        this.userDAO = new UserDAOImpl();
        this.roleDAO = new RoleDAOImpl();
        this.vehicleDAO = new VehicleDAOImpl();
        this.bookingDAO = new BookingDAOImpl();
        this.availabilityDAO = new AvailabilityDAOImpl();
        this.advisorDAO = new AdvisorDAOImpl();
        this.recordDAO = new RecordDAOImpl();
        this.catalogDAO = new CatalogDAOImpl();
        this.invoiceDAO = new InvoiceDAOImpl();
    }

	public List<User> getAllUsers() throws BusinessLogicException {
		try {
			return userDAO.findAll();
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching users: " + e.getMessage());
		}
	}

	public List<User> getUsersByRole(String roleName) throws BusinessLogicException {
		try {
			return userDAO.findByRole(roleName);
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching users by role: " + e.getMessage());
		}
	}

	public User createUser(String fullName, String email, String password, String phone, String gender, String roleName)
	        throws BusinessLogicException {
	    return createUserWithAdvisorDetails(fullName, email, password, phone, gender, roleName, "General",
	            new BigDecimal("500.00"), "AVAILABLE");
	}

	public User createUserWithAdvisorDetails(String fullName, String email, String password, String phone,
			String gender, String roleName, String specialization, BigDecimal overtimeRate, String availabilityStatus)
			throws BusinessLogicException {
		try {
			User existingUser = userDAO.findByEmail(email);
			if (existingUser != null) {
				throw new BusinessLogicException("Email already registered");
			}

			Role role = roleDAO.findByName(roleName);
			if (role == null) {
				throw new BusinessLogicException("Invalid role: " + roleName);
			}

			User user = new User();
			user.setFullName(fullName);
			user.setEmail(email);
	        user.setPassword(PasswordHash.hashPassword(password));
			user.setPhone(phone);
			user.setGender(gender);
			user.setRoleId(role.getRoleId());
			user.setRoleName(role.getRoleName());
			
			user = userDAO.insert(user);

			if ("ADVISOR".equals(roleName)) {
				Advisor advisor = new Advisor();
				advisor.setAdvisorId(user.getUserId());
				advisor.setFullName(user.getFullName());
				advisor.setEmail(user.getEmail());
				advisor.setSpecialization(specialization != null ? specialization : "General");
				advisor.setOvertimeRate(overtimeRate != null ? overtimeRate : new BigDecimal("500.00"));
				advisor.setAvailabilityStatus(availabilityStatus != null ? availabilityStatus : "AVAILABLE");
				advisor.setCurrentLoad(0);

				advisorDAO.insert(advisor);
			}

			return user;

		} catch (SQLException e) {
			throw new BusinessLogicException("Error creating user: " + e.getMessage());
		}
	}

	public void updateUserStatus(int userId, String status) throws BusinessLogicException {
		try {
			userDAO.updateStatus(userId, status);
		} catch (SQLException e) {
			throw new BusinessLogicException("Error updating user status: " + e.getMessage());
		}
	}

	public void deleteUser(int userId) throws BusinessLogicException {
		try {
			User user = userDAO.findById(userId);
			if (user == null) {
				throw new BusinessLogicException("User not found");
			}

			userDAO.delete(userId);

		} catch (SQLException e) {
			throw new BusinessLogicException("Error deleting user: " + e.getMessage());
		}
	}

	public List<Vehicle> getAllVehicles() throws BusinessLogicException {
		try {
			return vehicleDAO.findAll();
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching vehicles: " + e.getMessage());
		}
	}

	public List<Booking> getAllBookings() throws BusinessLogicException {
		try {
			return bookingDAO.findAll();
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching bookings: " + e.getMessage());
		}
	}

	public List<Advisor> getAvailableAdvisors() throws BusinessLogicException {
		try {
			return advisorDAO.findAvailable();
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching available advisors: " + e.getMessage());
		}
	}

	public ServiceRecord assignBookingToAdvisor(int bookingId, int advisorId, int adminUserId) 
	        throws BusinessLogicException {
	    try {
	        
	        Booking booking = bookingDAO.findById(bookingId);
	        if (booking == null) {
	            throw new BusinessLogicException("❌ Booking not found (ID: " + bookingId + ")");
	        }
	        
	        if (!booking.getBookingStatus().equals("PENDING") && 
	            !booking.getBookingStatus().equals("CONFIRMED") &&
	            !booking.getBookingStatus().equals("RESCHEDULED")) {
	            throw new BusinessLogicException(
	                "Cannot assign " + booking.getBookingStatus() + " booking.\n" +
	                "   Only PENDING, CONFIRMED, or RESCHEDULED bookings can be assigned."
	            );
	        }

	        ServiceRecord existing = recordDAO.findByBookingId(bookingId);
	        if (existing != null) {
	            throw new BusinessLogicException(
	                "Booking already assigned.\n" +
	                "   Currently assigned to: " + existing.getAdvisorName() + "\n" +
	                "   Service Status: " + existing.getStatus()
	            );
	        }

	        Advisor advisor = advisorDAO.findById(advisorId);
	        if (advisor == null) {
	            throw new BusinessLogicException("Service Advisor not found (ID: " + advisorId + ")");
	        }

	        User advisorUser = userDAO.findById(advisorId);
	        if (advisorUser == null) {
	            throw new BusinessLogicException(
	                "Advisor user account not found.\n");
	        }
	        
	        if (!"ACTIVE".equals(advisorUser.getStatus())) {
	            throw new BusinessLogicException(
	                "Cannot assign to INACTIVE advisor.\n" +
	                "   Advisor: " + advisor.getFullName() + "\n" +
	                "   Account Status: " + advisorUser.getStatus() + "\n" +
	                "   Action Required: Activate advisor account before assignment."
	            );
	        }

	        String availStatus = advisor.getAvailabilityStatus();
	        
	        switch (availStatus) {
	            case "AVAILABLE":
	                System.out.println("✅ Advisor is AVAILABLE for assignment");
	                break;
	                
	            case "ASSIGNED":
	                if (advisor.getCurrentLoad() >= 5) {
	                    throw new BusinessLogicException("Advisor is at maximum capacity\n" +
	                        "   Advisor: " + advisor.getFullName());
	                }
	                System.out.println(
	                    "Advisor already has " + advisor.getCurrentLoad());
	                break;
	                
	            case "ON_LEAVE":
	                throw new BusinessLogicException(
	                    "Cannot assign to advisor on leave.");
	                
	            case "RESIGNED":
	                throw new BusinessLogicException(
	                    "Cannot assign to resigned advisor");
	            default:
	                throw new BusinessLogicException(
	                    "Unknown advisor status: " + availStatus);
	        }

	        // catlog id for duration
	        Catalog catalog = catalogDAO.findById(booking.getCatalogId());
	        if (catalog == null) {
	            throw new BusinessLogicException("❌ Service catalog entry not found");
	        }

	        ServiceRecord record = new ServiceRecord();
	        record.setBookingId(bookingId);
	        record.setAdvisorId(advisorId);
	        record.setStatus("PENDING");
	        record.setEstimatedHours(catalog.getDurationHours());

	        record = recordDAO.insert(record);

	        // update advisor status
	        advisorDAO.updateAvailability(advisorId, "ASSIGNED");
	        advisorDAO.updateLoad(advisorId, 1);
	        
	        // Update Booking Status
	        bookingDAO.updateStatus(bookingId, "CONFIRMED");

	        // Log assignment in history
	        BookingHistoryDAO historyDAO = new BookingHistoryDAOImpl();
	        historyDAO.logConfirmed(bookingId, adminUserId);

	        // Success Message
	        System.out.println("ASSIGNMENT DONE");
	        System.out.println("=".repeat(60));
	        System.out.println("Booking ID      : " + bookingId);
	        System.out.println("Vehicle         : " + booking.getVehicleInfo());
	        System.out.println("Service         : " + booking.getServiceName());
	        System.out.println("Assigned to     : " + advisor.getFullName());
	        System.out.println("Specialization  : " + advisor.getSpecialization());
	        System.out.println("Estimated Hours : " + catalog.getDurationHours());
	        System.out.println("Advisor Load    : " + (advisor.getCurrentLoad() + 1) + " active service(s)");

	        return record;

	    } catch (SQLException e) {
	        throw new BusinessLogicException("Database error during assignment: " + e.getMessage());
	    }
	}
   
   public void updateAdvisorStatus(int advisorId, String status) throws BusinessLogicException {
       try {
           Advisor advisor = advisorDAO.findById(advisorId);
           if (advisor == null) {
               throw new BusinessLogicException("Service Advisor not found");
           }
           
           // Validation before status change
           if ("AVAILABLE".equals(status) && advisor.getCurrentLoad() > 0) {
               System.out.println(
                   "\nWARNING: Setting advisor to AVAILABLE with active assignments.\n" +
                   "   Advisor: " + advisor.getFullName() + "\n" +
                   "   Current Load: " + advisor.getCurrentLoad() + " active service(s)\n");
           }
           
           if ("ON_LEAVE".equals(status) && advisor.getCurrentLoad() > 0) {
               throw new BusinessLogicException(
                   "Cannot set advisor to ON_LEAVE with active assignments.\n");
           }
           
           if ("RESIGNED".equals(status) && advisor.getCurrentLoad() > 0) {
               throw new BusinessLogicException(
                   "Cannot set advisor to RESIGNED with active assignments.\n");
           }
           
           advisorDAO.updateAdvisorStatus(advisorId, status);
           
           System.out.println(
               "\nAdvisor status updated.\n" +
               "   Advisor: " + advisor.getFullName() + "\n" +
               "   New Status: " + status);
           
       } catch (SQLException e) {
           throw new BusinessLogicException("Error updating advisor status: " + e.getMessage());
       }
   }

   public List<Advisor> getAllAdvisors() throws BusinessLogicException {
       try {
           return advisorDAO.findAll();
       } catch (SQLException e) {
           throw new BusinessLogicException("Error fetching advisors: " + e.getMessage());
       }
   }

	public List<ServiceRecord> getCompletedServices() throws BusinessLogicException {
		try {
			return recordDAO.findByStatus("COMPLETED");
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching completed services: " + e.getMessage());
		}
	}

	public List<ServiceRecord> getInProgressServices() throws BusinessLogicException {
		try {
			return recordDAO.findByStatus("IN_PROGRESS");
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching in-progress services: " + e.getMessage());
		}
	}

	public List<Invoice> getAllInvoices() throws BusinessLogicException {
		try {
			return invoiceDAO.findAll();
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching invoices: " + e.getMessage());
		}
	}

	public List<Availability> getAllAvailabilitySlots() throws BusinessLogicException {
		try {
			return availabilityDAO.findAll();
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching availability slots: " + e.getMessage());
		}
	}

	public Availability addServiceAvailability(LocalDate serviceDate, String timeSlot, int maxBookings)
			throws BusinessLogicException {
		try {
			Availability existing = availabilityDAO.findByDateAndSlot(serviceDate, timeSlot);
			if (existing != null) {
				throw new BusinessLogicException("Slot already exists for " + serviceDate + " at " + timeSlot);
			}

			Availability slot = new Availability();
			slot.setServiceDate(serviceDate);
			slot.setTimeSlot(timeSlot);
			slot.setMaxBookings(maxBookings);
			slot.setCurrentBookings(0);
			slot.setAvailable(true);

			return availabilityDAO.insert(slot);
		} catch (SQLException e) {
			throw new BusinessLogicException("Error adding availability slot: " + e.getMessage());
		}
	}

	public void updateServiceAvailability(int availabilityId, int maxBookings, boolean isAvailable)
			throws BusinessLogicException {
		try {
			availabilityDAO.update(availabilityId, maxBookings, isAvailable);
		} catch (SQLException e) {
			throw new BusinessLogicException("Error updating availability slot: " + e.getMessage());
		}
	}

	public void deleteServiceAvailability(int availabilityId) throws BusinessLogicException {
		try {
			availabilityDAO.delete(availabilityId);
		} catch (SQLException e) {
			throw new BusinessLogicException("Error deleting availability slot: " + e.getMessage());
		}
	}

	public int getTotalUsers() throws BusinessLogicException {
		try {
			return userDAO.getTotalCount();
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching user count: " + e.getMessage());
		}
	}

	public int getTotalVehicles() throws BusinessLogicException {
		try {
			return vehicleDAO.getTotalCount();
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching vehicle count: " + e.getMessage());
		}
	}

	public int getTotalBookings() throws BusinessLogicException {
		try {
			return bookingDAO.getTotalCount();
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching booking count: " + e.getMessage());
		}
	}

	public int getActiveBookings() throws BusinessLogicException {
		try {
			return bookingDAO.getCountByStatus("PENDING", "CONFIRMED");
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching active bookings: " + e.getMessage());
		}
	}

	public int getCompletedBookings() throws BusinessLogicException {
		try {
			return bookingDAO.getCountByStatus("COMPLETED");
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching completed bookings: " + e.getMessage());
		}
	}

	public int getCancelledBookings() throws BusinessLogicException {
		try {
			return bookingDAO.getCountByStatus("CANCELLED");
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching cancelled bookings: " + e.getMessage());
		}
	}
}