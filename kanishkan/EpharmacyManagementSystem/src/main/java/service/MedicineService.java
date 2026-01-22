package service;

import java.util.Scanner;

import exception.InputMismatchException;
import dao.MedicineDao;

public class MedicineService {
	
	public static void getMedicineDetails() throws Exception, InputMismatchException{
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nEnter Medicine Details:");
		System.out.println("Enter medicine Id: ");
		int id = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter medicine name: ");
		String name = scanner.nextLine();
		System.out.println("Enter brand name: ");
		String brandName = scanner.nextLine();
		System.out.println("Enter composition: ");
		String composition = scanner.nextLine();
		System.out.println("Enter Strength: ");
		String strength = scanner.nextLine();
		System.out.println("Enter MRP: ");
		double mrp = scanner.nextDouble();
		scanner.nextLine();
		System.out.println("ISs prescription required (yes/no): ");
		String prescriptionStatus = scanner.nextLine();
		boolean prescRequired = false;
		if(prescriptionStatus.equalsIgnoreCase("yes")) {
			prescRequired = true;
		}
		else if (!prescriptionStatus.equalsIgnoreCase("no")) {
			throw new InputMismatchException("Enter valid status yes/no");
			
		}
		System.out.println("Enter category id: ");
		int categoryId = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter storage instructions: ");
		String storage = scanner.nextLine();
		System.out.println("Enter dosage: ");
		String dosage = scanner.nextLine();
		System.out.println("Enter maufacturer id: ");
		int manufacturerId = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter schedule Type: ");
		String scheduleType = scanner.nextLine();
		System.out.println("Enter tax percentage: ");
		int taxPercentage = scanner.nextInt();
		System.out.println("Enter discount percentage: ");
		int discPercentage = scanner.nextInt();
		double finalPrice = calculateFinalPrice(taxPercentage, discPercentage, mrp);
		MedicineDao.addMedicine(id, name, brandName, composition, strength, mrp, prescRequired, categoryId, storage, dosage, manufacturerId, scheduleType, taxPercentage, discPercentage, finalPrice);
		
		
		
		
	}
	public static double calculateFinalPrice(int tax, int discount,double mrp) {
		double taxAmount = (double) (mrp * (tax/100.0));
		double discountAmount = (double) (mrp * (discount/100.0));
		return (mrp+taxAmount-discountAmount);
	}

}
