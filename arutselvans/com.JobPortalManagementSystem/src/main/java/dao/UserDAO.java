package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import util.DBConnection;
import Models.Company;
import Models.Job;
import Models.Notification;
import Models.User;

public class UserDAO {
	
	public boolean validUserRole(String email,String role) {
		String sql = "select role from users where email = ?";
		try(Connection connection = DBConnection.getConnection()){
			PreparedStatement psSql = connection.prepareStatement(sql);
			psSql.setString(1,email);
			ResultSet rsSql = psSql.executeQuery();
			if(!rsSql.next()) return false;
			if(rsSql.getString("role").equals(role)) {
				return true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
//	public Optional<User> loginAdmin(String email, String password) {
//
//	    String sql = " SELECT user_id, name, role , phone_number FROM users  WHERE email = ? AND password = ?";
//
//	    try (Connection con = DBConnection.getConnection();
//	         PreparedStatement ps = con.prepareStatement(sql)) {
//
//	        ps.setString(1, email);
//	        ps.setString(2, password);
//
//	        ResultSet rs = ps.executeQuery();
//
//	        if (rs.next()) {
//	            return Optional.of(new User(
//	                    rs.getInt("user_id"),
//	                    rs.getString("name"),
//	                    rs.getString("role"),
//	                    rs.getString("phone_number")
//	            ));
//	        }
//
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	    return Optional.empty();
//	}
	
	public Optional<User> loginEmployer(String email, String password) {

	    String sql = " select user_id,name,role,phone_number,email,gender,date_of_birth from users WHERE email = ? AND password = ?";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, email);
	        ps.setString(2, password);

	        ResultSet rsSql = ps.executeQuery();

	        if (rsSql.next()) {
	            return Optional.of(new User(
	            		rsSql.getInt("user_id"),
	            		rsSql.getString("name"),
						rsSql.getString("role"),
						rsSql.getString("email"),
						rsSql.getString("gender"),
						rsSql.getDate("date_of_birth").toLocalDate(),
						rsSql.getString("phone_number")));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return Optional.empty();
	}


	public List<User> getAllUser(){
		List<User> users = new ArrayList<>();
		String sql = "select user_id,name,role,phone_number,email,gender,date_of_birth from users where role not in (\"ADMIN\") and active_status not in (\"blocked\");";	
		
		try(Connection connection = DBConnection.getConnection()){
			PreparedStatement psSql = connection.prepareStatement(sql);
			ResultSet rsSql = psSql.executeQuery();
			
			while(rsSql.next()) {
				users.add(new User(rsSql.getInt("user_id"),rsSql.getString("name"),
						rsSql.getString("role"),
						rsSql.getString("email"),rsSql.getString("gender"),
						rsSql.getDate("date_of_birth").toLocalDate(),rsSql.getString("phone_number")));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return users;
		
		
	}
	
	public List<Job> getAllJobs(){
		List<Job> jobs = new ArrayList<>();
		String sql = "select job_id,company_name,job_title,job_description,j.location,posted_date\r\n"
				+ "from job j \r\n"
				+ "join company c on j.company_id = c.company_id; ";
		
		try(Connection connection = DBConnection.getConnection()){
			PreparedStatement psSql = connection.prepareStatement(sql);
			ResultSet rs = psSql.executeQuery();
			
			while(rs.next()) {
				jobs.add(new Job(rs.getInt("job_id"),rs.getString("company_name"),rs.getString("job_title"),rs.getString("job_description")
						,rs.getString("j.location"),rs.getDate("posted_date").toLocalDate()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jobs;
	}
	
	
	public List<Company> viewAllCompanies(){
		List<Company> companies = new ArrayList<>();
		String sql = "select company_id,company_name,location,description from company";
		try(Connection connection = DBConnection.getConnection()){
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				companies.add(new Company(rs.getInt("company_id"),rs.getString("company_name"),rs.getString("location"),rs.getString("description")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return companies;
	}
	
	public Company viewCompany(int company_id) {
		String sql = "select company_id,company_name,location,description from company where company_id = ?";
		try(Connection connection = DBConnection.getConnection()){
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1,company_id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return new Company(rs.getInt("company_id"),rs.getString("company_name"),rs.getString("location"),rs.getString("description"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public void verifyEmployer(int employer_id) {

	    String updateQuery = """
	        update employer
	        set verified = 'Yes'
	        where employer_id = ?
	        """;

	    String selectQuery = """
	        select user_id
	        from employer
	        where employer_id = ?
	        """;

	    String deleteQuery = """
	        delete from notification
	        where sender_id = ?
	        and receiver_id = 12
	        and message like 'Employer Request%'
	        """;

	    try (Connection connection = DBConnection.getConnection()) {

	        // Start transaction
	        connection.setAutoCommit(false);

	        // 1. Verify employer
	        try (PreparedStatement updatePs = connection.prepareStatement(updateQuery)) {
	            updatePs.setInt(1, employer_id);
	            updatePs.executeUpdate();
	        }

	        int user_id;

	        // 2. Get employer's user_id
	        try (PreparedStatement selectPs = connection.prepareStatement(selectQuery)) {
	            selectPs.setInt(1, employer_id);
	            try (ResultSet rs = selectPs.executeQuery()) {
	                if (!rs.next()) {
	                    connection.rollback();
	                    return;
	                }
	                user_id = rs.getInt("user_id");
	            }
	        }

	        // 3. Delete verification request notification
	        try (PreparedStatement deletePs = connection.prepareStatement(deleteQuery)) {
	            deletePs.setInt(1, user_id);
	            deletePs.executeUpdate();
	        }

	        // Commit transaction
	        connection.commit();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	
	public void blockUser(int user_id) {
		String sql = "update  users \r\n"
				+ "set active_status = \"blocked\"\r\n"
				+ "where user_id = ?";
		
		try(Connection connection = DBConnection.getConnection()){
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
public List<Notification> viewNotification(int user_id) {
    	
    	List<Notification> notifications = new ArrayList<>();
    	String sql = """
    			select message,created_date,sender_id,receiver_id
    			from notification
    			where receiver_id = ? and created_date = curdate();
    			""";
    	
    	try(Connection connection = DBConnection.getConnection()){
    		PreparedStatement ps = connection.prepareStatement(sql);
    		ps.setInt(1, user_id);
    		ResultSet rs = ps.executeQuery();
    		while(rs.next()) {
    			notifications.add(new Notification(rs.getString("message"),rs.getDate("created_date").toLocalDate(),rs.getInt("sender_id"),
    					rs.getInt("receiver_id")));
    		}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
		}
		return notifications;
    }
	
	
	public void sendAnnouncement(String description) {
		List<User> users = new ArrayList<>();
		users = new UserDAO().getAllUser();
		
		String sql = """
					insert into notification(sender_id,receiver_id,message,created_date)
					values(12,?,?,curdate())
					""";
		
		try(Connection connection = DBConnection.getConnection()){
			PreparedStatement ps = connection.prepareStatement(sql);
			for(User user : users) {
				ps.setInt(1,user.getId());
				ps.setString(2,description);
				ps.executeUpdate();
			}
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		
			
	}
}
