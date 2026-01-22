package com.modals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.util.BusDBUtil;

public class Admin{
	String adminName;
	String adminPassword;
	Admin(String regName,String regPassword)throws Exception{
         adminName = regName;
         adminPassword = regPassword;
	}
	
	
}
