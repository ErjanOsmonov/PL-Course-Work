public class Appointment {
    private int userId;
    private String doctor;
    private String date;
    private String time;

    // Constructor for Appointment with 4 parameters
    public Appointment(int userId, String doctor, String date, String time) {
        this.userId = userId;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }

    // Getter methods for all fields
    public int getUserId() {
        return userId;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
