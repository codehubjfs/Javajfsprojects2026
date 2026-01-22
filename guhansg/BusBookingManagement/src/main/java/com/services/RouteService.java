package com.services;

import com.dao.*;
import java.util.*;
import com.modals.*;

public class RouteService {
     private static RouteDAO routeDAO = new RouteDAO();
     
     public static List<Route> getAllRoute() throws Exception{
    	 return RouteDAO.getRoute();
     }
     
     
}
