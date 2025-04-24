## $ json-server --host 0.0.0.0 --port 3000 --watch db.json
## Server start, without it's bugged

# Project Documentation: Java Appointment Booking System with JSON-Server

## Overview
This project implements a simple Appointment Booking System using Java and a mock REST API powered by `json-server`. The application allows user registration, login, and CRUD operations on appointments. Data persistence is handled through a local `db.json` file, simulating a database.

## Technologies Used
- **Java** (No Maven/Gradle)
- **json-server** for RESTful API emulation
- **VS Code** as the IDE
- **HTTPURLConnection** for making API requests
- Manual JSON parsing with String operations (due to not using external libraries)

## Data Structures

### User
- `id` (int)
- `name` (String)
- `email` (String)
- `password` (String)

### Appointment
- `id` (int, auto-incremented)
- `userId` (int, links to a User)
- `doctor` (String)
- `date` (String, format: `YYYY-MM-DD`)
- `time` (String, format: `HH:MM`)

## Modules & Functions

### 1. UserService.java

- **registerUser(User user)**  
  Validates input, ensures unique email, and sends a POST request to `/users`.
  
- **loginUser(String email, String password)**  
  Fetches `/users` via GET, parses the response manually to match credentials, and returns a valid User object or throws an error.
  
- **doesUserExist(String email)**  
  Checks if the email already exists in `/users`.

- **getAllUsers()**  
  Returns a list of all users parsed from `/users`.

### 2. AppointmentService.java

- **createAppointment(Appointment appointment)**  
  Auto-generates ID and sends a POST request to `/appointments`.
  
- **readAppointments(int userId)**  
  Sends a GET request to `/appointments?userId=x` and filters results for a given user.

- **updateAppointment(Appointment appointment)**  
  Sends a PUT request to `/appointments/{id}`.

- **deleteAppointment(int id)**  
  Sends a DELETE request to `/appointments/{id}`.

- **exportAppointmentsToJson(String filePath)**  
  Saves fetched appointments into a `.json` file.

## Algorithms & Logic

### Auto-Increment Logic for Appointments
On each `createAppointment` call:
- Fetch all existing appointments.
- Set `newId = max(existingIds) + 1`. If none exist, `newId = 1`.

### Manual JSON Parsing
Due to no external library (like `org.json`), responses are parsed with:
- `.replaceAll()` to strip brackets and quotes.
- `.split()` to divide into records and key-value pairs.

### Input Validation
- Simple regex for email.
- Check for empty strings before POSTing data.

## Challenges Faced

### Lack of JSON parsing libraries
- **Problem**: Parsing strings manually was error-prone and hard to debug.
- **Solution**: Relied on simple string operations for parsing, but it could be improved with libraries like `Gson` or `Jackson`.

### HTTP Connection Management
- **Problem**: Repetitive boilerplate for every API call, and debugging issues with "Connection refused" due to incorrect base URL.
- **Solution**: Streamlined the connection setup but could be improved with a client library.

### Whitespace Issues
- **Problem**: JSON strings returned from the API sometimes had leading/trailing spaces, breaking parsing and ID checks.
- **Solution**: Trimmed strings manually before processing.

### VS Code and Compilation
- **Problem**: Confusion between compiling via terminal and using the “Run” button in VS Code.
- **Solution**: Managed `.class` files separately without a build system.
