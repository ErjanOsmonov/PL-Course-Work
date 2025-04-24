import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String USERS_URL = "http://localhost:3000/users";

    public void registerUser(User user) throws IOException {
        if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Name, Email, and Password cannot be empty.");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (doesUserExist(user.getEmail())) {
            throw new IllegalArgumentException("User already exists.");
        }

        HttpURLConnection conn = (HttpURLConnection) new URL(USERS_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String json = String.format(
            "{\"name\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}",
            user.getName(), user.getEmail(), user.getPassword()
        );

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        System.out.println("Register response: " + conn.getResponseCode());
    }

    public User loginUser(String email, String password) throws IOException {
        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Email and Password cannot be empty.");
        }
    
        email = email.trim();
        password = password.trim();
    
        HttpURLConnection conn = (HttpURLConnection) new URL(USERS_URL).openConnection();
        conn.setRequestMethod("GET");
    
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            jsonBuilder.append(line);
        }
        in.close();
    
        String json = jsonBuilder.toString()
            .replaceAll("[\\[\\]{}\"]", "") // strip brackets, braces, quotes
            .replaceAll("},\\s*\\{", "\\|"); // mark entries clearly
    
        String[] userEntries = json.split("\\|");
        for (String entry : userEntries) {
            String[] fields = entry.split(",");
            int id = -1;
            String name = "", foundEmail = "", foundPassword = "";
    
            for (String field : fields) {
                String[] kv = field.split(":");
                if (kv.length == 2) {
                    String key = kv[0].trim();
                    String value = kv[1].trim();
                    switch (key) {
                        case "id": id = Integer.parseInt(value); break;
                        case "name": name = value; break;
                        case "email": foundEmail = value; break;
                        case "password": foundPassword = value; break;
                    }
                }
            }
    
            // System.out.println("Check: " + foundEmail + " / " + foundPassword);
    
            if (foundEmail.equals(email) && foundPassword.equals(password)) {
                return new User(id, name, foundEmail, foundPassword);
            }
        }
    
        throw new IllegalArgumentException("Invalid credentials.");
    }

    private boolean doesUserExist(String email) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(USERS_URL).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            if (line.contains(email)) return true;
        }
        return false;
    }

    public static List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    try {
        URL url = new URL(USERS_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String json = response.toString();
            json = json.replaceAll("[\\[\\]{}\"]", ""); // basic manual parsing

            String[] entries = json.split("},\\{");
            for (String entry : entries) {
                String[] fields = entry.split(",");
                int id = 0;
                String name = "", email = "", password = "";

                for (String field : fields) {
                    String[] kv = field.split(":");
                    if (kv.length == 2) {
                        String key = kv[0].trim();
                        String value = kv[1].trim();
                        switch (key) {
                            case "id":
                                id = Integer.parseInt(value);
                                break;
                            case "name":
                                name = value;
                                break;
                            case "email":
                                email = value;
                                break;
                            case "password":
                                password = value;
                                break;
                        }
                    }
                }
                users.add(new User(id, name, email, password));
            }
        }

    } catch (IOException e) {
        System.out.println("Error fetching users: " + e.getMessage());
    }

    return users;
}


    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }
}
