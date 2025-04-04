import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            saveRecord(record);
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            saveRecord(record);
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayAvailableVehicles() {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllVehicles() {
        for (Vehicle v : vehicles) {
            System.out.println("  " + v.getInfo());
        }
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }

    public Customer findCustomerByName(String name) {
        for (Customer c : customers)
            if (c.getCustomerName().equalsIgnoreCase(name))
                return c;
        return null;
    }
    
    public void saveVehicle(Vehicle vehicle) {
    	try {
    	      File myObj = new File("vehicle.txt");
    	      
    	    } 
    	catch (IOException e) {
    	      System.out.println("A file creation error occurred.");
    	      e.printStackTrace();
    	    }
    	
    	try {
    	      FileWriter myWriter = new FileWriter("vehicle.txt");
    	      myWriter.write(vehicle.toString());
    	      myWriter.close();
    	}
    	
    	catch (IOException e) {
    	      System.out.println("A file write error occurred.");
    	      e.printStackTrace();
    	}
    }
    
    public void saveCustomer(Customer customer) {
    	try {
  	      File myObj = new File("customers.txt");
  	      
  	    } 
  	catch (IOException e) {
  	      System.out.println("A file creation error occurred.");
  	      e.printStackTrace();
  	    }
  	
  	try {
  	      FileWriter myWriter = new FileWriter("customers.txt");
  	      myWriter.write(customer.toString());
  	      myWriter.close();
  	}
  	
  	catch (IOException e) {
  	      System.out.println("A file write error occurred.");
  	      e.printStackTrace();
  	}
    }
    
    public void saveRecord(RentalRecord record) {
    	try {
    	      File myObj = new File("rental_record.txt");
    	      
    	    } 
    	catch (IOException e) {
    	      System.out.println("A file creation error occurred.");
    	      e.printStackTrace();
    	    }
    	
    	try {
  	      FileWriter myWriter = new FileWriter("rental_record.txt");
  	      myWriter.write(record.toString());
  	      myWriter.close();
  	}
  	
  	catch (IOException e) {
  	      System.out.println("A file write error occurred.");
  	      e.printStackTrace();
  	}
    }
    
}