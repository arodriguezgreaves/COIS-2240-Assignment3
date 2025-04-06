import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RentalSystem {
	public RentalSystem(){
        loadData();
    }
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        try {
            saveCustomer(customer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            saveRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
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
            saveRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
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
    	
    	try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("vehicle.txt"))) {
            out.writeObject(vehicle);
            out.close();
    	}
    	
    	catch (IOException e) {
    	      System.out.println("A file write error occurred.");
    	      e.printStackTrace();
    	}
    }
    
    public void saveCustomer(Customer customer) throws IOException {
  	
        try (FileWriter myWriter = new FileWriter("customers.txt");){
            myWriter.write("\n"+customer.toString());
            myWriter.close();
        }
    
        catch (IOException e) {
            System.out.println("A file write error occurred.");
            e.printStackTrace();
        }
    }
    
    public void saveRecord(RentalRecord record) {
    	
        try (FileWriter myWriter = new FileWriter("rental_record.txt");){
            myWriter.write(record.toString());
            myWriter.close();
        }
    
        catch (IOException e) {
            System.out.println("A file write error occurred.");
            e.printStackTrace();
        }
    }
    private void loadData(){
        // load from vehicles.txt
        Vehicle temp0 = null;
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream("vehicle.txt"))){
            temp0 = (Vehicle)input.readObject();
            vehicles.add(temp0);
            input.close();
        } catch (IOException e) {
            System.out.println("A file read error occurred.");
            e.printStackTrace();
        } catch (ClassNotFoundException cnf){
            System.out.println("Class not found.");
            cnf.printStackTrace();
        }


        //load from customers
        try(BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))){
            String line;
            while ((line = reader.readLine())!=null){
                String parts [] = line.split(" ");
                int ID = 0;
                try{
                    ID = Integer.parseInt(parts[2]);
                }
                catch(NumberFormatException ex){ex.printStackTrace();}
                Customer temp = new Customer(ID, parts[5]);
                customers.add(temp);
            }
        }
        catch(IOException e){
            System.out.println("A file read error occurred.");
  	          e.printStackTrace();
        }

        //load to rental record
        try(BufferedReader reader = new BufferedReader(new FileReader("rental_record.txt"))){
            String line2;
            while ((line2 = reader.readLine())!=null){
                String parts1 [] = line2.split(" ");
                String recordType = parts1[0];
                String plate = parts1[3];
                String customer = parts1[6];
                String date = parts1[9];
                String amount = parts1[12];

                Vehicle ve = new Vehicle() {};
                ve = findVehicleByPlate(plate);
                Customer cu = findCustomerByName(customer);
                LocalDate ld = LocalDate.parse(date);
                String amount1 = amount.replace("$", "");
                double am = Double.parseDouble(amount1);

                //ve,cu,da,am,status
                    rentalHistory.addRecord(new RentalRecord(ve,cu,ld,am,recordType));    
            }
        }
        catch(IOException e){
            System.out.println("A file read error occurred.");
  	          e.printStackTrace();
        }
    }
}