package dev.BusinessLayer.Employees;
import dev.BusinessLayer.Employees.Shift.ShiftType;
import dev.ServiceLayer.Objects.Response;
import dev.ServiceLayer.Objects.SEmployee;
import dev.ServiceLayer.Objects.SShift;
import dev.ServiceLayer.Objects.SShiftType;
import dev.ServiceLayer.Services.EmployeesService;
import dev.ServiceLayer.Services.UserService;
import dev.Utils.DateUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class SchedulingTests {
    private UserService userService;
    private EmployeesService empService;
    private User admin;
    private String adminUsername = "admin123";
    private String password = "123";
    private User[] users = new User[30];
    private String[] usernames = new String[30];
    private String[] passwords = new String[30];
    private String[] fullnames = new String[30];
    private String[] branches = new String[30];
    private String[] bankDetails = new String[30];
    private double[] hourlyRates = new double[30];
    private LocalDate[] employmentDates = new LocalDate[30];
    private String[] employmentConditions = new String[30];
    private String[] details = new String[30];

    @Before
    public void setUp() throws Exception {
        userService = UserService.getInstance();
        empService = EmployeesService.getInstance();
        userService.loadData(); // Loads the HR Manager user: "admin123" "123", clears the data in each test
        empService.loadData();
        admin = userService.getUser(adminUsername).getReturnValue();
        users = new User[30];
        String usernamer = "0";
        String passworder = "0";
        String namer = "abc";
        String brancher = "1";
        String bankDetailer = "Hapoalim 00 000";
        double hourlyRater = 30;
        LocalDate employmentDater = LocalDate.of(2023, 1, 1);
        String employmentConditioner = "slave";
        String detailer = "nothing";
        int counter = 0;
        for(int i=0; i<30;i++){
        usernames[i] = usernamer;
        passwords[i] = passworder;
        fullnames[i] = namer;
        branches[i] = brancher;
        bankDetails[i] = bankDetailer;
        hourlyRates[i] = hourlyRater;
        employmentDates[i] = employmentDater;
        employmentConditions[i] = employmentConditioner;
        details[i] = detailer;

        if(userService.getUser(usernames[i]).errorOccurred())
            userService.createUser(admin.getUsername(), usernames[i], passwords[i]);
        if(empService.getEmployee(usernames[i]).errorOccurred())
            empService.recruitEmployee(admin.getUsername(),fullnames[i], branches[i], usernames[i],bankDetails[i], hourlyRates[i], employmentDates[i],employmentConditions[i], details[i]);
        users[i] = userService.getUser(usernames[i]).getReturnValue();
        users[i].login(passwords[i]);

        counter++;        
        usernamer = Integer.toString(counter);
        passworder = Integer.toString(counter);
      
        if(i>=10)
            brancher = "1";
        if(i>=20)
            brancher = "1";
        Response<Boolean> ans = null;
        if(i<10){
            ans = userService.authorizeUser(adminUsername, usernames[i], Authorization.ShiftManager.name());
            empService.certifyEmployee(adminUsername, usernames[i], Role.ShiftManager.name());
        }
        else if(i>=10 && i<20){
            userService.authorizeUser(adminUsername, usernames[i], Authorization.Cashier.name());
            empService.certifyEmployee(adminUsername, usernames[i], Role.Cashier.name());
        }
        else if(i>=20 && i<=25){
            userService.authorizeUser(adminUsername, usernames[i], Authorization.Storekeeper.name());
            empService.certifyEmployee(adminUsername, usernames[i], Role.Storekeeper.name());
        }
        
    }
        
    }
    // 0-9 shiftmanager, 10-19 cashier, 20-25 storekeeper
    @Test
    public void test_scheduling() {
        try {
            
            LocalDate[] week = DateUtils.getWeekDates(LocalDate.of(2023,4, 9));
            Response<Boolean> ans = empService.createWeekShifts(adminUsername, "1", week[0]);
            assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            ans = empService.setShiftNeededAmount(adminUsername, "1", week[0], SShiftType.Morning, Role.Cashier.name(), 2);
            assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            ans =empService.setShiftNeededAmount(adminUsername, "1", week[0], SShiftType.Morning, Role.Storekeeper.name(), 1);
            assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            ans = empService.requestShift(usernames[0],"1" , week[0], SShiftType.Morning, Role.ShiftManager.name());
            assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            ans = empService.requestShift(usernames[10],"1" , week[0], SShiftType.Morning, Role.Cashier.name());
            assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            ans = empService.requestShift(usernames[11],"1" , week[0], SShiftType.Morning, Role.Cashier.name());
            assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            ans = empService.requestShift(usernames[11],"1" , week[0], SShiftType.Morning, Role.Cashier.name()); // done twice intentionally
           // assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            ans = empService.requestShift(usernames[20],"1" , week[0], SShiftType.Morning, Role.Storekeeper.name());
            assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            ans = empService.requestShift(usernames[21],"1" , week[0], SShiftType.Morning, Role.GeneralWorker.name());
            assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            Response<List<SShift[]>> ans2 = empService.getWeekShifts(adminUsername, "1",week[0]);
            boolean foundShift = false;
            SShift theShift = null;
            for(SShift[] shifts : ans2.getReturnValue()){
                for(SShift shift: shifts){
                    if(shift.getShiftDate().isEqual(week[0]) &&  shift.getShiftType() == SShiftType.Morning){
                        theShift = shift;
                        foundShift = true;
                        boolean foundEmployee1 = false, foundEmployee2 = false;
                        for(SEmployee se: shift.getShiftRequestsEmployees(Role.Cashier.name()) ){
                            if(se.getId().equals( empService.getEmployee(usernames[10]).getReturnValue().getId()))
                                foundEmployee1 = true;
                            if(se.getId().equals( empService.getEmployee(usernames[11]).getReturnValue().getId()))
                                foundEmployee2 = true;
                        }
                        assertTrue(foundEmployee1 && foundEmployee2);
                        
                        assertTrue( shift.getShiftRequestsEmployees(Role.ShiftManager.name()).get(0).getId().equals(empService.getEmployee(usernames[0]).getReturnValue().getId()));
                        assertTrue( shift.getShiftRequestsEmployees(Role.Storekeeper.name()).get(0).getId().equals(empService.getEmployee(usernames[20]).getReturnValue().getId()));
                        assertTrue( shift.getShiftRequestsEmployees(Role.GeneralWorker.name()).get(0).getId().equals(empService.getEmployee(usernames[21]).getReturnValue().getId()));

                    }
                }
            }
            assertTrue(foundShift);
            ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.Cashier.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.Cashier.name())));
            ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.ShiftManager.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.ShiftManager.name())));
            ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.Storekeeper.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.Storekeeper.name())));
            ans = empService.setShiftEmployees(adminUsername, "1", week[0], SShiftType.Morning,Role.GeneralWorker.name(),employeesToIds(theShift.getShiftRequestsEmployees(Role.GeneralWorker.name())));
            ans = empService.approveShift(adminUsername, "1", week[0], SShiftType.Morning);
            assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            ans = empService.setShiftNeededAmount(adminUsername, "1", week[0], SShiftType.Morning, "Cashier", 3);
            assertFalse(ans.getErrorMessage(), ans.errorOccurred());
            ans = empService.approveShift(adminUsername, "1", week[0], SShiftType.Morning);
            assertTrue(ans.getErrorMessage(), ans.errorOccurred());
        } catch (Exception ignore) { ignore.printStackTrace(); Assert.fail("failed");}
    }
    private List<String> employeesToIds(List<SEmployee> emps){
        List<String> li = new LinkedList<String>();
        for(SEmployee emp : emps){
            li.add(emp.getId());
        }
        return li;
    }

}
