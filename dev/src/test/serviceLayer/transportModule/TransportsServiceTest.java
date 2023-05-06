package serviceLayer.transportModule;

import businessLayer.transportModule.*;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.transportModule.DistanceBetweenSites;
import dataAccessLayer.transportModule.SitesDistancesDAO;
import dataAccessLayer.transportModule.TransportsDAO;
import objects.transportObjects.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.employeeModule.Services.EmployeesService;
import utils.JsonUtils;
import utils.Response;
import utils.transportUtils.TransportException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

class TransportsServiceTest {

    public static final String SOURCE_ADDRESS = "sourceAddress";
    public static final String DESTINATION_ADDRESS_1 = "destinationAddress1";
    public static final String DESTINATION_ADDRESS_2 = "destinationAddress2";
    public static final String DRIVER_ID = "123";
    public static final String TRUCK_ID = "123ABC";
    public static final int ITEM_LIST_ID_1 = 1;
    public static final int ITEM_LIST_ID_2 = 2;
    public static final LocalDateTime DEPARTURE_TIME = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(12, 0));
    TransportsService transportsService;
    TransportsController transportController;
    ItemListsController itemListsController;
    TrucksController trucksController;
    SitesController sitesController;
    EmployeesService employeesService;
    DriversController driversController;
    TransportsDAO transportsDAO;
    SitesDistancesDAO sitesDistancesDAO;
    Transport transport;
    Site source;
    Driver driver;
    Truck truck;
    Site destination1;
    Site destination2;
    ItemList itemList1;
    ItemList itemList2;

    @BeforeEach
    void setUp() {

        itemListsController = mock(ItemListsController.class);
        trucksController = mock(TrucksController.class);
        sitesController = mock(SitesController.class);
        driversController = mock(DriversController.class);
        transportsDAO = mock(TransportsDAO.class);
        sitesDistancesDAO = mock(SitesDistancesDAO.class);

        try {
            when(transportsDAO.selectCounter()).thenReturn(1);
        } catch (DalException e) {
            fail(e);
        }

        try {
            transportController = new TransportsController(
                    trucksController,
                    driversController,
                    sitesController,
                    itemListsController,
                    transportsDAO,
                    sitesDistancesDAO
            );
        } catch (TransportException e) {
            fail(e);
        }

        employeesService = mock(EmployeesService.class);
        transportController.injectDependencies(employeesService);
        transportsService = new TransportsService(transportController);

        source = new Site("zone1", SOURCE_ADDRESS, "0545555550", "contactNameSource", Site.SiteType.LOGISTICAL_CENTER);
        destination1 = new Site("zone1", DESTINATION_ADDRESS_1, "0545555551", "contactNameDest1", Site.SiteType.BRANCH);
        destination2 = new Site("zone1", DESTINATION_ADDRESS_2, "0545555552", "contactNameDest2", Site.SiteType.BRANCH);
        driver = new Driver(DRIVER_ID, "driverName", Driver.LicenseType.C3);
        truck = new Truck(TRUCK_ID, "model", 2000, 25000, Truck.CoolingCapacity.FROZEN);
        itemList1 = new ItemList(ITEM_LIST_ID_1,
                new HashMap<>() {{
                    put("item1", 1);
                    put("item2", 2);
                }},
                new HashMap<>() {{
                    put("item3", 3);
                    put("item4", 4);
                }}
        );
        itemList2 = new ItemList(ITEM_LIST_ID_2,
                new HashMap<>() {{
                    put("item5", 5);
                    put("item6", 6);
                }},
                new HashMap<>() {{
                    put("item7", 7);
                    put("item8", 8);
                }}
        );

        transport = new Transport(
                source.address(),
                new LinkedList<>() {{
                    add(destination1.address());
                    add(destination2.address());
                }},
                new HashMap<>() {{
                    put(destination1.address(), itemList1.id());
                    put(destination2.address(), itemList2.id());
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                15000
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addTransport() {

        try {
            initializeMocksToPassTransportValidation();
        } catch (DalException | TransportException e) {
            throw new RuntimeException(e);
        }

        String json = transportsService.addTransport(transport.toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertTrue(response.success());
        assertEquals(1, response.dataToInt());

    }

    @Test
    void addTransportPredefinedId(){
        Transport transport = new Transport(
                500,
                source.address(),
                new LinkedList<>() {{
                    add(destination1.address());
                    add(destination2.address());
                }},
                new HashMap<>() {{
                    put(destination1.address(), itemList1.id());
                    put(destination2.address(), itemList2.id());
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                15000
        );
        assertThrows(UnsupportedOperationException.class,() -> transportsService.addTransport(transport.toJson()));
    }

    @Test
    void updateTransport() {
        Transport updatedTransport = new Transport(
                1,
                destination1.address(),
                new LinkedList<>() {{
                    add(destination2.address());
                }},
                new HashMap<>() {{
                    put(destination2.address(), itemList1.id());
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                25000
        );
        try {
            initializeMocksToPassTransportValidation();
            when(transportsDAO.exists(updatedTransport)).thenReturn(true);
            when(transportsDAO.select(updatedTransport)).thenReturn(updatedTransport);
        } catch (DalException | TransportException e) {
            throw new RuntimeException(e);
        }
        String json = transportsService.updateTransport(updatedTransport.toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertTrue(response.success());
        json = transportsService.getTransport(Transport.getLookupObject(1).toJson());
        response = JsonUtils.deserialize(json, Response.class);
        assertTrue(response.success());
        Transport fetchedTransport = Transport.fromJson(response.data());
        assertDeepEquals(updatedTransport, fetchedTransport);
    }

    @Test
    void updateTransportDoesNotExist(){
        try{
            when(transportsDAO.exists(Transport.getLookupObject(transport.id()))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        String json = transportsService.updateTransport(transport.toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertFalse(response.success());
    }

    @Test
    void removeTransport() {
        try{
            when(transportsDAO.exists(Transport.getLookupObject(transport.id()))).thenReturn(true);
        } catch (DalException e) {
            fail(e);
        }
        String json = transportsService.removeTransport(Transport.getLookupObject(transport.id()).toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertTrue(response.success());
    }

    @Test
    void removeTransportDoesNotExist(){
        try{
            when(transportsDAO.exists(Transport.getLookupObject(5))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        String json = transportsService.removeTransport(Transport.getLookupObject(5).toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertFalse(response.success());
    }

    @Test
    void getTransport() {
        try{
            when(transportsDAO.exists(Transport.getLookupObject(transport.id()))).thenReturn(true);
            when(transportsDAO.select(Transport.getLookupObject(transport.id()))).thenReturn(transport);
        } catch (DalException e) {
            fail(e);
        }
        String json = transportsService.getTransport(Transport.getLookupObject(transport.id()).toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertTrue(response.success());
        Transport fetchedTransport = Transport.fromJson(response.data());
        assertDeepEquals(transport, fetchedTransport);
    }

    @Test
    void getTransportDoesNotExist(){
        try{
            when(transportsDAO.exists(Transport.getLookupObject(transport.id()))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        String json = transportsService.getTransport(Transport.getLookupObject(transport.id()).toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertFalse(response.success());
    }

    @Test
    void getAllTransports() {
        //set up
        Transport transport2 = new Transport(
                2,
                source.address(),
                new LinkedList<>() {{
                    add(destination1.address());
                    add(destination2.address());
                }},
                new HashMap<>() {{
                    put(destination1.address(), itemList1.id());
                    put(destination2.address(), itemList2.id());
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                15000
        );
        List<Transport> transports = List.of(transport, transport2);
        try{
            when(transportsDAO.selectAll()).thenReturn(transports);
        } catch (DalException e) {
            fail(e);
        }

        //test
        String json = transportsService.getAllTransports();
        Response response = JsonUtils.deserialize(json, Response.class);
        assertTrue(response.success());
        List<Transport> fetchedTransports = Transport.listFromJson(response.data());
        assertDeepEquals(transports.get(0), fetchedTransports.get(0));
        assertDeepEquals(transports.get(1), fetchedTransports.get(1));
    }

    @Test
    void createTransportWithTooMuchWeight(){
        Transport badTransport = new Transport(
                source.address(),
                new LinkedList<>() {{
                    add(destination1.address());
                    add(destination2.address());
                }},
                new HashMap<>() {{
                    put(destination1.address(), itemList1.id());
                    put(destination2.address(), itemList2.id());
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                50000
        );

        try{
            initializeMocksToPassTransportValidation();
        } catch (DalException | TransportException e) {
            throw new RuntimeException(e);
        }
        String json = transportsService.addTransport(badTransport.toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertFalse(response.success());
        assertEquals("weight",response.data());
    }

    @Test
    void createTransportWithBadLicense(){
        Driver badDriver = new Driver(driver.id(), driver.name(), Driver.LicenseType.A1);
        Transport badTransport = new Transport(
                source.address(),
                new LinkedList<>() {{
                    add(destination1.address());
                    add(destination2.address());
                }},
                new HashMap<>() {{
                    put(destination1.address(), itemList1.id());
                    put(destination2.address(), itemList2.id());
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                24000
        );
        try{
            initializeMocksToPassTransportValidation();
            when(driversController.getDriver(driver.id())).thenReturn(badDriver);
        } catch (DalException | TransportException e) {
            throw new RuntimeException(e);
        }
        String json = transportsService.addTransport(badTransport.toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertFalse(response.success());
        assertEquals("license",response.data());
    }

    @Test
    void createTransportWithBadLicenseAndTooMuchWeight(){
        Driver badDriver = new Driver(driver.id(), driver.name(), Driver.LicenseType.A1);
        Transport badTransport = new Transport(
                source.address(),
                new LinkedList<>() {{
                    add(destination1.address());
                    add(destination2.address());
                }},
                new HashMap<>() {{
                    put(destination1.address(), itemList1.id());
                    put(destination2.address(), itemList2.id());
                }},
                driver.id(),
                truck.id(),
                DEPARTURE_TIME,
                30000
        );
        try{
            initializeMocksToPassTransportValidation();
            when(driversController.getDriver(driver.id())).thenReturn(badDriver);
        } catch (DalException | TransportException e) {
            throw new RuntimeException(e);
        }
        String json = transportsService.addTransport(badTransport.toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertFalse(response.success());
        assertEquals("license,weight",response.data());
    }

    @Test
    void createTransportEverythingDoesNotExist(){
        String json = transportsService.addTransport(transport.toJson());
        Response response = JsonUtils.deserialize(json, Response.class);
        assertFalse(response.success());
        assertEquals("driver,truck,source,destination:0,itemList:0,destination:1,itemList:1",response.data());
    }

    private void initializeMocksToPassTransportValidation() throws DalException, TransportException {
        when(driversController.driverExists(driver.id())).thenReturn(true);
        when(driversController.getDriver(driver.id())).thenReturn(driver);
        Response r = new Response(true,List.of(driver.id()));
        when(employeesService.getAvailableDrivers(JsonUtils.serialize(DEPARTURE_TIME))).thenReturn(r.toJson());

        when(trucksController.truckExists(truck.id())).thenReturn(true);
        when(trucksController.getTruck(TRUCK_ID)).thenReturn(truck);

        when(sitesController.siteExists(source.address())).thenReturn(true);
        when(sitesController.siteExists(destination1.address())).thenReturn(true);
        when(sitesController.siteExists(destination2.address())).thenReturn(true);
        when(sitesController.getSite(source.address())).thenReturn(source);
        when(sitesController.getSite(destination1.address())).thenReturn(destination1);
        when(sitesController.getSite(destination2.address())).thenReturn(destination2);
        when(employeesService.checkStoreKeeperAvailability(JsonUtils.serialize(DEPARTURE_TIME), source.address())).thenReturn(new Response(true).toJson());
        when(employeesService.checkStoreKeeperAvailability(JsonUtils.serialize(DEPARTURE_TIME), destination1.address())).thenReturn(new Response(true).toJson());
        when(employeesService.checkStoreKeeperAvailability(JsonUtils.serialize(DEPARTURE_TIME), destination2.address())).thenReturn(new Response(true).toJson());

        when(itemListsController.listExists(itemList1.id())).thenReturn(true);
        when(itemListsController.listExists(itemList2.id())).thenReturn(true);

        DistanceBetweenSites distance1 = DistanceBetweenSites.getLookupObject(source.address(), destination1.address());
        DistanceBetweenSites distance2 = DistanceBetweenSites.getLookupObject(destination1.address(), destination2.address());
        when(sitesDistancesDAO.select(distance1)).thenReturn(new DistanceBetweenSites(distance1, 100));
        when(sitesDistancesDAO.select(distance2)).thenReturn(new DistanceBetweenSites(distance2, 100));

    }
    private void assertDeepEquals(Transport transport1, Transport transport2) {
        assertEquals(transport1.id(), transport2.id());
        assertEquals(transport1.source(), transport2.source());
        assertEquals(transport1.destinations(), transport2.destinations());
        assertEquals(transport1.itemLists(), transport2.itemLists());
        assertEquals(transport1.driverId(), transport2.driverId());
        assertEquals(transport1.truckId(), transport2.truckId());
        assertEquals(transport1.departureTime(), transport2.departureTime());
        assertEquals(transport1.weight(), transport2.weight());
    }
}