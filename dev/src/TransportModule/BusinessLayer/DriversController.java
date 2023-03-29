package TransportModule.BusinessLayer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class DriversController {
    TreeMap <Integer,Driver> drivers;

    public DriversController(){
        drivers = new TreeMap<>();
    }

    public Driver createDriver(int id, String name, String licenseType){
        Driver driver = new Driver(id,name,licenseType);
        drivers.put(id,driver);
        return driver;
    }

    public  Driver getDriver(int id) throws Exception {
        if (drivers.containsKey(id) == false)
            throw new Exception("Driver not found");

        return drivers.get(id);
    }

    public Driver removeDriver(int id) throws IOException {
        if (drivers.containsKey(id) == false)
            throw new IOException("Driver not found");

        return drivers.remove(id);
    }

    public boolean updateDriver(int id, Driver newDriver) throws IOException{
        if(drivers.containsKey(id) == false)
            throw new IOException("Driver not found");

        drivers.put(id, newDriver);
        return true;
    }

    public LinkedList<Driver> getAllDrivers(){
        LinkedList<Driver> driversList = new LinkedList<>();
        for(Driver driver : drivers.values())
            driversList.add(driver);
        return driversList;

    }

}

