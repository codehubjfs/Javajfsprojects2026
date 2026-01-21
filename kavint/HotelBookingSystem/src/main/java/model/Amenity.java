package model;

public class Amenity {

    private int amenityId;
    private String amenityName;
    private String description;

    public Amenity(int amenityId, String amenityName, String description) {
        this.amenityId = amenityId;
        this.amenityName = amenityName;
        this.description = description;
    }

    public int getAmenityId() { 
    	return amenityId; 
    }
    
    public void setAmenityId(int amenityId) { 
    	this.amenityId = amenityId; 
    }

    public String getAmenityName() { 
    	return amenityName; 
    }
    
    public void setAmenityName(String amenityName) {
        this.amenityName = amenityName;
    }

    public String getDescription() { 
    	return description; 
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}

