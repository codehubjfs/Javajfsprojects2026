package com.vserv.service;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

import com.vserv.dao.impl.*;
import com.vserv.dao.interfaces.*;
import com.vserv.exception.BusinessLogicException;
import com.vserv.model.*;
import com.vserv.config.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class InvoiceService {
	private InvoiceDAO invoiceDAO;
	private PaymentDAO paymentDAO;
	private BookingDAO bookingDAO;
	private AdvisorDAO advisorDAO;
	private ItemDAO serviceItemDAO;
	private RecordDAO recordDAO;
	private NotificationService notificationService;
	private VehicleDAO vehicleDAO;

	public InvoiceService() {
		this.invoiceDAO = new InvoiceDAOImpl();
		this.paymentDAO = new PaymentDAOImpl();
		this.bookingDAO = new BookingDAOImpl();
		this.serviceItemDAO = new ItemDAOImpl();
		this.recordDAO = new RecordDAOImpl();
		this.notificationService = new NotificationService();
		this.vehicleDAO = new VehicleDAOImpl();
		this.advisorDAO = new AdvisorDAOImpl();
	}

	public Invoice generateInvoice(int serviceId) throws BusinessLogicException {
		try {
			// Check exists
			Invoice existing = invoiceDAO.findByServiceId(serviceId);
			if (existing != null) {
				throw new BusinessLogicException("Invoice already exists for this service");
			}

			// Verify completed
			ServiceRecord record = recordDAO.findById(serviceId);
			if (record == null) {
				throw new BusinessLogicException("Service record not found");
			}
			if (!record.getStatus().equals("COMPLETED")) {
				throw new BusinessLogicException("Cannot generate invoice for incomplete service");
			}

			// service items total
			List<ServiceItem> items = serviceItemDAO.findByServiceId(serviceId);
			BigDecimal itemsTotal = items.stream().map(ServiceItem::getTotalPrice).reduce(BigDecimal.ZERO,
					BigDecimal::add);

			if (itemsTotal.compareTo(BigDecimal.ZERO) == 0) {
				throw new BusinessLogicException("No service items found. Cannot generate invoice.");
			}

			// overtime
			BigDecimal overtimeCharge = BigDecimal.ZERO;

			if (record.getActualHours() != null && record.getEstimatedHours() != null) {
				double overtimeHours = Math.max(0, record.getActualHours() - record.getEstimatedHours());

				if (overtimeHours > 0) {
					Advisor advisor = advisorDAO.findById(record.getAdvisorId());
					overtimeCharge = advisor.getOvertimeRate().multiply(BigDecimal.valueOf(overtimeHours)).setScale(2,
							java.math.RoundingMode.HALF_UP);

					System.out.println("\n💰 Overtime Detected:");
					System.out.println("   Estimated: " + record.getEstimatedHours() + " hours");
					System.out.println("   Actual: " + record.getActualHours() + " hours");
					System.out.println("   Overtime: " + String.format("%.2f", overtimeHours) + " hours");
					System.out.println("   Rate: Rs. " + advisor.getOvertimeRate() + "/hour");
					System.out.println("   Overtime Charge: Rs. " + overtimeCharge);
				}
			}

			// total
			BigDecimal totalAmount = itemsTotal.add(overtimeCharge);

			Invoice invoice = new Invoice();
			invoice.setServiceId(serviceId);
			invoice.setItemsTotal(itemsTotal);
			invoice.setOvertimeCharge(overtimeCharge);
			invoice.setTotalAmount(totalAmount);
			invoice.setPaymentStatus("PENDING");

			invoice = invoiceDAO.insert(invoice);

			updateBookingStatus(record.getBookingId(), "COMPLETED");

			Booking booking = bookingDAO.findById(record.getBookingId());
			Vehicle vehicle = vehicleDAO.findById(booking.getVehicleId());
			String vehicleInfo = String.format("%s %s (%s)", vehicle.getBrand(), vehicle.getModel(),
					vehicle.getRegistrationNumber());

			notificationService.sendPaymentReminder(vehicle.getUserId(), booking.getBookingId(), vehicleInfo,
					totalAmount.toString());

			return invoice;

		} catch (SQLException e) {
			throw new BusinessLogicException("Error generating invoice: " + e.getMessage());
		}
	}

	public Invoice getInvoiceByServiceId(int serviceId) throws BusinessLogicException {
		try {
			return invoiceDAO.findByServiceId(serviceId);
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching invoice: " + e.getMessage());
		}
	}

	private void updateBookingStatus(int bookingId, String status) throws SQLException {
		String sql = "UPDATE service_booking SET booking_status = ? WHERE booking_id = ?";
		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, status);
			stmt.setInt(2, bookingId);
			stmt.executeUpdate();
		}
	}

	public List<Invoice> getCustomerInvoices(int customerId) throws BusinessLogicException {
		try {
			return invoiceDAO.findByCustomerId(customerId);
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching invoices: " + e.getMessage());
		}
	}

	public Invoice getInvoiceById(int invoiceId) throws BusinessLogicException {
		try {
			Invoice invoice = invoiceDAO.findById(invoiceId);
			if (invoice == null) {
				throw new BusinessLogicException("Invoice not found");
			}
			return invoice;
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching invoice: " + e.getMessage());
		}
	}

	public Payment processPayment(int invoiceId, String paymentMethod, BigDecimal amount, String transactionRef)
			throws BusinessLogicException {
		try {
			Invoice invoice = invoiceDAO.findById(invoiceId);
			if (invoice == null) {
				throw new BusinessLogicException("Invoice not found");
			}

			if (invoice.getPaymentStatus().equals("PAID")) {
				throw new BusinessLogicException("Invoice already paid");
			}

			// Create payment record
			Payment payment = new Payment();
			payment.setInvoiceId(invoiceId);
			payment.setPaymentMethod(paymentMethod);
			payment.setAmount(amount);
			payment.setTransactionReference(transactionRef);
			payment.setPaymentStatus("SUCCESS");

			payment = paymentDAO.insert(payment);

			// Calculate total paid
			List<Payment> payments = paymentDAO.findByInvoiceId(invoiceId);
			BigDecimal totalPaid = payments.stream().filter(p -> p.getPaymentStatus().equals("SUCCESS"))
					.map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

			// Update invoice payment status
			String newStatus;
			if (totalPaid.compareTo(invoice.getTotalAmount()) >= 0) {
				newStatus = "PAID";
			} else {
				newStatus = "PARTIALLY_PAID";
			}
			invoiceDAO.updatePaymentStatus(invoiceId, newStatus);

			return payment;

		} catch (SQLException e) {
			throw new BusinessLogicException("Error processing payment: " + e.getMessage());
		}
	}

	public List<Payment> getInvoicePayments(int invoiceId) throws BusinessLogicException {
		try {
			return paymentDAO.findByInvoiceId(invoiceId);
		} catch (SQLException e) {
			throw new BusinessLogicException("Error fetching payments: " + e.getMessage());
		}
	}
}