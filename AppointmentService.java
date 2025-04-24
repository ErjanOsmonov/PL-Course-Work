import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class AppointmentService {
    private static final String APPOINTMENTS_URL = "http://localhost:3000/appointments";

    public void createAppointment(Appointment appointment) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(APPOINTMENTS_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String json = String.format(
            "{\"userId\":%d, \"doctor\":\"%s\", \"date\":\"%s\", \"time\":\"%s\"}",
            appointment.getUserId(), appointment.getDoctor(), appointment.getDate(), appointment.getTime()
        );

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        System.out.println("Create response: " + conn.getResponseCode());
    }

    public void readAppointments() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(APPOINTMENTS_URL).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    public void updateAppointment(int id, Appointment appointment) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(APPOINTMENTS_URL + "/" + id).openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
    
        String json = String.format(
            "{\"userId\":%d, \"doctor\":\"%s\", \"date\":\"%s\", \"time\":\"%s\"}",
            appointment.getUserId(), appointment.getDoctor(), appointment.getDate(), appointment.getTime()
        );

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        System.out.println("Update response: " + conn.getResponseCode());
    }

    public void deleteAppointment(int id) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(APPOINTMENTS_URL + "/" + id).openConnection();
        conn.setRequestMethod("DELETE");

        System.out.println("Delete response: " + conn.getResponseCode());
    }
}
