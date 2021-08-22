package student;

import java.math.BigDecimal;
import java.util.List;
import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new mn170387_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new mn170387_DistrictOperations(); // Do it for all classes.
        CourierOperations courierOperations = new mn170387_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new mn170387_CourierRequestOperation();
        GeneralOperations generalOperations = new mn170387_GeneralOperations();
        UserOperations userOperations = new mn170387_UserOperations();
        VehicleOperations vehicleOperations = new mn170387_VehicleOperations();
        PackageOperations packageOperations = new mn170387_PackageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
    }
    
    

}
