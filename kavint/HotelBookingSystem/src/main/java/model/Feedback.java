package model;

public class Feedback {

    private int feedbackId;
    private int bookingId;
    private int rating;
    private String comments;
    private String submittedAt;

    public Feedback(int feedbackId, int bookingId, int rating,
                    String comments, String submittedAt) {
        this.feedbackId = feedbackId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.comments = comments;
        this.submittedAt = submittedAt;
    }

	public int getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(int feedbackId) {
		this.feedbackId = feedbackId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(String submittedAt) {
		this.submittedAt = submittedAt;
	}
    
}
