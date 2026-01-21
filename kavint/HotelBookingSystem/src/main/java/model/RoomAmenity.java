package model;

public class RoomAmenity {

    private int roomAmenityId;
    private int roomId;
    private int amenityId;

    public RoomAmenity(int roomAmenityId, int roomId, int amenityId) {
        this.roomAmenityId = roomAmenityId;
        this.roomId = roomId;
        this.amenityId = amenityId;
    }

    public int getRoomAmenityId() { 
    	return roomAmenityId; 
    }
    
    public void setRoomAmenityId(int roomAmenityId) {
        this.roomAmenityId = roomAmenityId;
    }

    public int getRoomId() { 
    	return roomId; 
    }
    
    public void setRoomId(int roomId) { 
    	this.roomId = roomId; 
    }

    public int getAmenityId() { 
    	return amenityId; 
    }
    
    public void setAmenityId(int amenityId) { 
    	this.amenityId = amenityId; 
    }
}
