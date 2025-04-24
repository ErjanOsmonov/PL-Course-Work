import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Scanner intsc = new Scanner(System.in);

        System.out.println("Greetings! Welcome to Erjan's Brilliant Appointment Scheduler! \n Please log in.");
        System.out.println("Please choose your option: \n 1: Registration \n 2: Login");
        int option = intsc.nextInt();
        String name = "", email = "", password = "";

        if (option == 1) {
            System.out.println("Enter your name: ");
            name = sc.nextLine();
            System.out.println("Enter your email: ");
            email = sc.nextLine();
            System.out.println("Enter your password: ");
            password = sc.nextLine();
        } else if (option == 2) {
            System.out.println("Enter your email: ");
            email = sc.nextLine();
            System.out.println("Enter your password: ");
            password = sc.nextLine();            
        }

        if (option == 1) {
            try {
                // Create instances of the service classes
                UserService userService = new UserService();
                
                // Register a user
                User newUser = new User(getNextUserId(), name, email, password);
                userService.registerUser(newUser);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else if (option == 2) {
            try {
            // Create instances of the service classes
            UserService userService = new UserService();
            AppointmentService appointmentService = new AppointmentService();    
            // Log in the user (login returns a User object)            
            User loggedInUser = userService.loginUser(email, password); 

            if (loggedInUser != null) {
                System.out.println("Logged in successfully: " + loggedInUser);
            } else {
                System.out.println("Login failed: Invalid email or password.");
            }

            System.out.println(loggedInUser);
            Boolean iter = true;
            System.out.println("Welcome, ! \n What would you like to do?");           
            while(iter) {
                System.out.println("Choose your option: \n 1: Make appointment \n 2: Look at appointments \n 3: Update appointment \n 4: Delete appointment \n 5: Exit");
                int newOption = intsc.nextInt(); 
    
                // Create an appointment for the logged-in user
    
                switch (newOption) {
                    case 1:
                        System.out.println("Choose your doctor: \n 1: Dr. Smith \n 2: Dr. Zhenishbek \n 3: Dr. Salymova");
                        int opt = intsc.nextInt();
                        String doctor = "";
                        if (opt == 1) {
                            doctor = "Dr. Smith";
                        } else if (opt == 2) {
                            doctor = "Dr. Zhenishbek";
                        } else if (opt == 3) {
                            doctor = "Dr. Salymova";
                        }
    
                        System.out.println("Write the date in format YYYY-MM-DD: ");
                        String date = sc.nextLine();
    
                        System.out.println("Pick the time in 24 hour format (ex: 14:35): ");
                        String time = sc.nextLine();
    
                        Appointment newAppointment = new Appointment(loggedInUser.getId(), doctor, date, time);
                        appointmentService.createAppointment(newAppointment);
                        break;
                    case 2:
                        appointmentService.readAppointments();                      
                        break;
                    case 3:
                        System.out.println("Choose the id of the appointment you want to update: ");
                        int newId = intsc.nextInt();
                        System.out.println("Choose your doctor: \n 1: Dr. Smith \n 2: Dr. Zhenishbek \n 3: Dr. Salymova");
                        int opt1 = intsc.nextInt();
                        String newDoctor = "";
                        if (opt1 == 1) {
                            newDoctor = "Dr. Smith";
                        } else if (opt1 == 2) {
                            newDoctor = "Dr. Zhenishbek";
                        } else if (opt1 == 3) {
                            newDoctor = "Dr. Salymova";
                        }
    
                        System.out.println("Write the date in format YYYY-MM-DD: ");
                        String newDate = sc.nextLine();
    
                        System.out.println("Pick the time in 24 hour format (ex: 14:35): ");
                        String newTime = sc.nextLine();
                        newAppointment = new Appointment(loggedInUser.getId(), newDoctor, newDate, newTime);
                        appointmentService.updateAppointment(newId, newAppointment);                    
                        break;
                    case 4:
                        System.out.println("Choose the id of the appointment that you want to delete:");
                        int deleteId = intsc.nextInt();
                        appointmentService.deleteAppointment(deleteId);                    
                        break;
                    case 5: 
                        iter = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        throw new AssertionError();
                }

            }
                
        } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static int getNextUserId() {
        List<User> users = UserService.getAllUsers();
        int maxId = 0;
        for (User user : users) {
            if (user.getId() > maxId) {
                maxId = user.getId();
            }
        }
        return maxId + 1;
    }  
}


