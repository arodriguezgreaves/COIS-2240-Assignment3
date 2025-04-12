import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}