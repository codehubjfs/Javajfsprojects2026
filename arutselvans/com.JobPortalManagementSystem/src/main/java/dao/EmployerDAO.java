package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Models.Job;
import Models.Notification;
import Models.UserProfile;
import util.DBConnection;

public class EmployerDAO {

    public UserProfile viewProfile(String email) {

        String sql = """
            SELECT u.name,
                   u.email,
                   u.phone_number,
                   u.date_of_birth,
                   u.address,
                   u.gender,
                   e.designation,
                   e.company_id
            FROM users u
            JOIN employer e ON u.user_id = e.user_id
            WHERE u.email = ?
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {   
                return new UserProfile(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("address"),
                        rs.getString("gender"),
                        rs.getString("designation"),
                        rs.getInt("company_id")
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
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
    
    public void postJob(int companyId , String jobTitle,String description , String location , int experience , LocalDate posted_date , LocalDate deadline
			,int pay_id) {
    	String sql =""" 
    			insert into job(company_id,job_title,job_description,location,experience_required,posted_date,deadline,pay_id)
    			value (?,?,?,?,?,curdate(),?,?)
    			""";
    	try(Connection connection = DBConnection.getConnection()){
    		PreparedStatement ps = connection.prepareStatement(sql);
    		ps.setInt(1,companyId);
    		ps.setString(2,jobTitle);
    		ps.setString(3,description);
    		ps.setString(4,location);
    		ps.setInt(5, experience);
    		ps.setDate(6, Date.valueOf(posted_date));
    		ps.setInt(7,pay_id);
    		
    		ps.executeUpdate();
    	} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    }
    
    public List<Job> viewPostedJobs(int company_id){
    	List<Job> postedJobs = new ArrayList<>();
    	
    	String sql= """
    			select j.job_id,c.company_name,j.job_title,j.job_description,c.location,j.posted_date from job j
    			join company c on j.company_id = c.company_id
    			where j.company_id=?
    			""";
    	try(Connection connection = DBConnection.getConnection()){
    		PreparedStatement ps = connection.prepareStatement(sql);
    		ps.setInt(1, company_id);
    		
    		ResultSet rs = ps.executeQuery();
    		
    		while(rs.next()) {
    			postedJobs.add(new Job(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getDate(6).toLocalDate()));
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return postedJobs;
    }
    
    
    public boolean validateEmployer(int user_id) {
    	String sql = """
    			select e.verified from users u
    	join employer e on u.user_id=e.user_id
    	where u.user_id=?;
    			""";
    	
    	try(Connection connection = DBConnection.getConnection()){
    		PreparedStatement ps = connection.prepareStatement(sql);
    		ps.setInt(1, user_id);
    		ResultSet rs = ps.executeQuery();
    		if(rs.next()) {
    			return rs.getString("e.verified").equalsIgnoreCase("Yes");
    		}
    		
    	} catch (SQLException e) {
			
			System.out.println(e.getMessage());
		}
    	return false;
    }
    
    public void requestForVerification(int user_id) {

        String selectQuery = """
            select e.employer_id as employer_id
            from employer e
            where e.user_id = ?
            """;

        String insertQuery = """
            insert into notification (message, sender_id, receiver_id, created_date)
            values (concat('Employer Request for Verification | Employer ID: ', ?),
                    ?, 12, curdate())
            """;

        try (Connection connection = DBConnection.getConnection()) {

            int employer_id;
            try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
                ps.setInt(1, user_id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("User is not an employer");
                        return;
                    }
                    employer_id = rs.getInt("employer_id");
                }
            }

            try (PreparedStatement insertPs = connection.prepareStatement(insertQuery)) {
                insertPs.setInt(1, employer_id); 
                insertPs.setInt(2, user_id);     
                insertPs.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
