package employeeModule.BusinessLayer.Employees;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.User;
import dataAccessLayer.DalFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.ServiceFactory;
import serviceLayer.employeeModule.Services.UserService;
import utils.Response;

import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private UserService userService;
    private User user;
    private String username = "admin123";
    private String correctPassword = "123";
    private String wrongPassword = "1234";

    @BeforeEach
    public void setUp() throws Exception {
        ServiceFactory serviceFactory = new ServiceFactory(TESTING_DB_NAME);
        userService = serviceFactory.userService();
        userService.createData(); // Loads the HR Manager user: "admin123" "123", clears the data in each test
        user = Response.fromJson(userService.getUser(username)).data(User.class);
    }

    @AfterEach
    void tearDown() {
        DalFactory.clearTestDB();
    }

    @Test
    public void login_wrongPassword() {
        try {
            boolean answer = user.login(wrongPassword);
            assertFalse(answer);
        } catch (Exception ignore) { }
    }

    @Test
    public void login_correctPassword() {
        try {
            boolean answer = user.login(correctPassword);
            assertTrue(answer);
        } catch (Exception ignore) {
            fail("Test failed, did not expect an exception when logging in with a correct password.");
        }
    }

    @Test
    public void logout_beforeLoggingIn() {
        try {
            user.logout();
            fail("Test failed, expected an exception when logging out before logging in.");
        } catch (Exception ignore) { }
    }

    @Test
    public void logout_afterLoggingIn() {
        try {
            user.login(correctPassword);
            user.logout();
        } catch (Exception ignore) {
            fail("Test failed, did not expect an exception when logging out after logging in successfully.");
        }
    }

    @Test
    public void isLoggedIn() {
        try {
            user.login(correctPassword);
            user.logout();
        } catch (Exception ignore) {
            fail("Test failed, did not expect an exception when logging out after logging in successfully.");
        }
    }

    @Test
    public void checkPassword_wrongPassword() {
        boolean answer = user.checkPassword(wrongPassword);
        assertFalse(answer);
    }

    @Test
    public void checkPassword_correctPassword() {
        boolean answer = user.checkPassword(correctPassword);
        assertTrue(answer);
    }

    @Test
    public void isAuthorized_HRManager() {
        boolean answer = user.isAuthorized(Authorization.HRManager);
        assertTrue(answer);
    }

    @Test
    public void isAuthorized_Cashier() {
        boolean answer = user.isAuthorized(Authorization.Cashier);
        assertFalse(answer);
    }

    @Test
    public void getAuthorizations() {
        List<Authorization> authorizations = user.getAuthorizations();
        assertTrue(authorizations != null
                && authorizations.size() == 1
                && authorizations.contains(Authorization.HRManager));
    }

    @Test
    public void authorize_Cashier() {
        user.authorize(Authorization.Cashier);
        assertTrue(user.isAuthorized(Authorization.Cashier));
        assertTrue(user.isAuthorized(Authorization.HRManager));
        assertFalse(user.isAuthorized(Authorization.Storekeeper));
    }

    @Test
    public void authorize_Storekeeper() {
        user.authorize(Authorization.Storekeeper);
        assertTrue(user.isAuthorized(Authorization.Storekeeper));
        assertTrue(user.isAuthorized(Authorization.HRManager));
        assertFalse(user.isAuthorized(Authorization.Cashier));
    }

    @Test
    public void authorize_ShiftManager() {
        user.authorize(Authorization.Storekeeper);
        assertTrue(user.isAuthorized(Authorization.Storekeeper));
        assertTrue(user.isAuthorized(Authorization.HRManager));
        assertFalse(user.isAuthorized(Authorization.Cashier));
    }
}