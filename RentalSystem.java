import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
    	try (FileWriter myWriter = new FileWriter("vehicle.txt",true)){
            myWriter.write(vehicle.getInfo()+"\n");
            myWriter.close();
        }
    
        catch (IOException e) {
            System.out.println("A file write error occurred.");
            e.printStackTrace();
        }
    }
    
    public void saveCustomer(Customer customer) throws IOException {
  	
        try (FileWriter myWriter = new FileWriter("customers.txt",true)){
            myWriter.write(customer.toString()+"\n");
            myWriter.close();
        }
    
        catch (IOException e) {
            System.out.println("A file write error occurred.");
            e.printStackTrace();
        }
    }
    
    public void saveRecord(RentalRecord record) {
    	
        try (FileWriter myWriter = new FileWriter("rental_record.txt",true)){
            myWriter.write(record.toString()+"\n");
            myWriter.close();
        }
    
        catch (IOException e) {
            System.out.println("A file write error occurred.");
            e.printStackTrace();
        }
    }

    private void loadData(){
        // load from vehicles.txt
        try(BufferedReader reader = new BufferedReader(new FileReader("vehicle.txt"))){
            String line1;
            while ((line1 = reader.readLine())!=null){
                String parts2 [] = line1.split(" ");
                String plate = parts2[1];
                String make = parts2[3];
                String model = parts2[5];
                String year = parts2[7];
                String status = parts2[9];
                String determ = parts2[12];
                String typeS = parts2[13];

                int yr = Integer.parseInt(year);

                Vehicle.VehicleStatus stat = Vehicle.VehicleStatus.valueOf(status);

                if (determ.equals("Seats:")){
                    int se = Integer.parseInt(typeS);
                    Car temp = new Car(make, model, yr, se);
                    temp.setLicensePlate(plate);
                    temp.setStatus(stat);
                    vehicles.add(temp);
                }
                else if (determ.equals("Sidecar:")) {
                    boolean sidecar; 
                    if (typeS == "true"){
                        sidecar=true;
                    }
                    else{
                        sidecar=false;
                    }
                    Motorcycle temp = new Motorcycle(make, model, yr, sidecar);
                    temp.setLicensePlate(plate);
                    temp.setStatus(stat);
                    vehicles.add(temp);
                }
                else if (determ.equals("CargoCapacity:")){
                    Double cap = Double.parseDouble(typeS);
                    Truck temp = new Truck(make, model, yr, cap);
                    temp.setLicensePlate(plate);
                    temp.setStatus(stat);
                    vehicles.add(temp);
                }
            }
        }
        catch(IOException e){
            System.out.println("A file read error occurred.");
  	          e.printStackTrace();
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