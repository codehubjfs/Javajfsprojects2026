package service;
import java.util.Scanner;
import dao.InventoryMangementDao;


public class InventoryService {
	public static void updatebyUserInput() throws Exception{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter medicine ID: ");
		int medicineId = scanner.nextInt();
		System.out.println("Enter batch number: ");
		int batchNumber = scanner.nextInt();
		InventoryMangementDao.updateInventoryStock(medicineId, batchNumber);
	}
}
