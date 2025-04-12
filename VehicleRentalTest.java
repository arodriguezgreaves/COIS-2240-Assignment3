import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.beans.Transient;
import java.time.LocalDate;

import org.junit.*;

public class VehicleRentalTest {
    @Test
    public void testRentAndReturnVehicle(){
        Vehicle v1 = new Car(null,null,0,0);
        Customer c1 = new Customer(0, null);
        assertTrue(vechicleHelper(v1));
        RentalSystem rentalSystem = new RentalSystem();
        rentalSystem.rentVehicle(v1, c1, LocalDate.now(), 0);
        assertTrue(vechicleHelper2(v1));
        rentalSystem.returnVehicle(v1, c1, LocalDate.now(), 0);
        assertTrue(vechicleHelper(v1));
        // Create a car with valid license plate
        Vehicle car = new Car("Toyota", "Corolla", "ABC123", 4);

        // Test valid license plates
        try {
            car.setLicensePlate("XYZ999");
        } catch (IllegalArgumentException e) {
            fail("Valid license plate threw exception");
        }

        // Test invalid license plates
        try {
            car.setLicensePlate("ABC1234");
            fail("Invalid license plate did not throw exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            car.setLicensePlate("123ABC");
            fail("Invalid license plate did not throw exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            car.setLicensePlate("");
            fail("Invalid license plate did not throw exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            car.setLicensePlate(null);
            fail("Invalid license plate did not throw exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

    }
    private Boolean vechicleHelper(Vehicle v1){
        if (v1.getStatus().equals(Vehicle.VehicleStatus.AVAILABLE)){
            return true;
        }
        else{
            return false;
        }
    }
    private Boolean vechicleHelper2(Vehicle v1){
        if (v1.getStatus().equals(Vehicle.VehicleStatus.RENTED)){
            return true;
        }
        else{
            return false;
        }
    }
    @Test
    public void testSingletonRentalSystem() {
        // Get two instances of RentalSystem
        RentalSystem rentalSystem1 = RentalSystem.getInstance();
        RentalSystem rentalSystem2 = RentalSystem.getInstance();

        // Check that both instances are the same (Singleton)
        assertSame(rentalSystem1, rentalSystem2, "Both RentalSystem instances should be the same");
    }
}
