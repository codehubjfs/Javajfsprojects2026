package com.services;

import com.dao.*;
import com.modals.*;
import java.util.*;

public class BusService {
	
	private static BusDAO busDAO = new BusDAO();
	
	public static List<Buses> getAllBuses() throws Exception{
		return busDAO.getBuses();
	}
	

}
