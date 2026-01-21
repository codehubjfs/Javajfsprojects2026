# Vehicle Service Management System

## Customer Workflow Progression (Current Stage)

## Development (as of 21/01/26)

### Completed
- `pom.xml`
- `DBConnection.java`
- `AuthController.java`
- `UserDAO.java`
- All **Model** classes
- `InputUtil.java`
- `ValidationUtil.java`
- All **Exception** classes

### In Progress
- `MainApplication.java`
- `CustomerController.java`
- `BookingService.java`
- `VehicleDAO.java`
- `UserService.java`
- `VehicleService.java`

### Not Started
- `ServiceCatalogDAO.java`
- `BookingDAO.java`

---

## Architecture Overview

- **Controller Layer**  
  Handles user interaction and workflow control.

- **Service Layer**  
  Contains business logic and validation rules.

- **DAO Layer**  
  Manages database operations using JDBC.

- **Model Layer**  
  Represents core entities such as User, Vehicle, and Service.

- **Utility Layer**  
  Provides reusable input handling and validation helpers.

- **Exception Layer**  
  Centralized custom exceptions for authentication and business rules.

---

## Current Capabilities Summary

 > User registration  
 > User login  
 > Add vehicle details  
 > View added vehicles  
