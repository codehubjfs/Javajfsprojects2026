package Models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Job {
	private int job_id;
	private String companyName;
	private String jobTitle;
	private String description;
	private String location;
	private LocalDate postedDate;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	public Job(int job_id,String companyName,String jobTitle,String description,String location,LocalDate postedDate) {
		this.job_id = job_id;
		this.companyName = companyName;
		this.jobTitle = jobTitle;
		this.description = description;
		this.location = location;
		this.postedDate = postedDate;
	}
	
	public String toString() {
		return "\nJob Id       : "+job_id+" | "+
				"Company Name: "+this.companyName+" | "+
				"Job Title   : "+jobTitle+" | "+
				"Description : "+this.description+" | "+
				"Location    : "+this.location+" | "+
				"Posted Date : "+this.postedDate.format(formatter)+" | "+
				"--------------------------------";
	}
}
