package com.services;

import java.util.*;

import com.exception.*;
import com.util.ValidationUtil;
import com.dao.*;
	
public class OperatorService {
	public static void manageBus(int operatorID) throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.println(
                "1.Add Bus\n" +
                "2.Remove Bus\n" +
                "3.View All Buses"
        );
		int busOption;
		while(true) {
			try {
				System.out.print("Enter Your Option : ");
				busOption = ValidationUtil.menuOptionCheck(sc.nextLine());
				break;
			}catch(InvalidNumberFormat e) {
				System.out.println(e.getMessage());
			}
		}
		
		String busNo;int totalSeat;String busType;String busAvail;
		
		switch(busOption) {
		case 1:
			sc.nextLine();
			while(true){
				try {
					System.out.print("Enter Bus Number (like :TNXXGUXXXX) : ");
					busNo = sc.nextLine();
					ValidationUtil.checkBusNo(busNo);
					break;
				}catch(InvalidBusNumberException e) {
					System.out.println(e.getMessage());
				}
			}
			while(true) {
				try {
					System.out.print("Enter Total no of Seats : ");
					totalSeat = sc.nextInt();
					break;
				}catch(IllegalFormatException e) {
					System.out.println(e.getMessage());
				}
			}
			while(true) {
				try {
					sc.nextLine();	
					System.out.print("Enter Bus Type : ");
					busType = sc.nextLine();
					break;
				}catch(IllegalFormatException e) {
					System.out.println(e.getMessage());
				}
			}
			while(true) {
				try {
					System.out.print("Enter Bus Status (AVAILABLE,ON TRIP,NOT AVAILABLE) : ");
					busAvail = sc.nextLine().toUpperCase();
					ValidationUtil.checkBusAvail(busAvail);
					break;
				}catch(InvalidBusAvailException e) {
					System.out.println(e.getMessage());
				}
			}
	        BusDAO.addNewBus(busNo, totalSeat, busType, busAvail, operatorID);
	        System.out.println("Bus "+busNo+" added Successfully..!");
	        System.out.println("====================================");
	        break;
	       
		case 2:
			sc.nextLine();
			while(true){
				try {
					System.out.print("Enter Bus Number to remove (like :TNXXGUXXXX): ");
					busNo = sc.nextLine();
					ValidationUtil.checkBusNo(busNo);
					break;
				}catch(InvalidBusNumberException e) {
					System.out.println(e.getMessage());
				}
			}
			BusDAO.removeExistingBus(busNo, operatorID);
			System.out.println("Bus "+busNo+" removed Successfully..!");
	        System.out.println("====================================");
			break;
			
		case 3:
			System.out.println("Bus List -> ");
			System.out.println(BusService.getAllBuses());
			System.out.println("==========================================");
			break;
			
		default :
			System.out.println("Please Enter the valid Option");
		}
		sc.close();
	}

}
