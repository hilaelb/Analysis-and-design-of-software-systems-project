package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Authorization;
import businessLayer.employeeModule.Employee;
import businessLayer.employeeModule.User;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;


import java.sql.SQLException;

import java.util.*;

public class UserDAO extends DAO {

    private static UserDAO instance;
    private HashMap<Integer, User> cache;
    private UserAuthorizationsDAO userAuthorizationsDAO;

    private enum Columns {
        Username,
        Password,
        LoggedIn;
    }

    //needed roles HashMap<Role,Integer>, shiftRequests HashMap<Role,List<Employees>>, shiftWorkers Map<Role,List<Employees>>, cancelCardApplies List<String>, shiftActivities List<String>.
    public UserDAO(UserAuthorizationsDAO userAuthorizationsDAO){
        super("USERS", new String[]{Columns.Username.name()});
        this.userAuthorizationsDAO = userAuthorizationsDAO;
        this.cache = new HashMap<>();
    }

    public UserDAO(String dbName, UserAuthorizationsDAO userAuthorizationsDAO){
        super(dbName,"USERS", new String[]{Columns.Username.name()});
        this.userAuthorizationsDAO = userAuthorizationsDAO;
        this.cache = new HashMap<>();
    }


    private int getHashCode(String username){
        return (username).hashCode();
    }
    public void create(User user) throws DalException {
        try {
            this.userAuthorizationsDAO.create(user);
            String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s) VALUES('%s','%s','%s')",
                    Columns.Username.name(), Columns.Password.name(), Columns.LoggedIn.name(),
                    user.getUsername(), user.getPassword(), String.valueOf(user.isLoggedIn()));
            cursor.executeWrite(queryString);
            this.cache.put(getHashCode(user.getUsername()), user);
        } catch (SQLException e) {
            throw new DalException(e);
        }
    }

    public User get(String username) throws DalException {
        if (this.cache.get(getHashCode(username))!=null)
            return this.cache.get(getHashCode(username));
        User ans = this.select(username);
        this.cache.put(getHashCode(username),ans);
        return ans;
    }

    public List<User> getAll() throws DalException {
        List<User> list = new LinkedList<>();
        for(Object o: selectAll()) {
            if(!(o instanceof Employee))
                throw new DalException("Something went wrong");
            User user = ((User)o);
            if (this.cache.get(getHashCode(user.getUsername())) != null)
                list.add(this.cache.get(getHashCode(user.getUsername())));
            else {
                list.add(user);
                this.cache.put(getHashCode(user.getUsername()), user);
            }
        }
        return list;
    }

    public void update(User user) throws DalException {
        if(!this.cache.containsValue(user))
            throw new DalException("Object doesn't exist in the database! Create it first.");
        if(!this.cache.containsKey(getHashCode(user.getUsername())) || this.cache.get(getHashCode(user.getUsername()))!= user)
            throw new DalException("Cannot change primary key of an object. You must delete it and then create a new one.");
        Exception ex = null;
        try {
            Object[] key = {user.getUsername()};
            this.userAuthorizationsDAO.update(user);
            String queryString = String.format("UPDATE "+TABLE_NAME+" SET %s = ? , %s = ? , %s = ? WHERE",
                    Columns.Username.name(), Columns.Password.name(), Columns.LoggedIn.name());
            queryString = queryString.concat(createConditionForPrimaryKey(key));
            cursor.executeWrite(queryString);
        } catch(SQLException e) {
            throw new DalException(e);
        }
    }
    public void delete(User user) throws DalException {
        this.cache.remove(getHashCode(user.getUsername()));
        Object[] keys = {user.getUsername()};
        this.userAuthorizationsDAO.delete(user);
        super.delete(keys);
    }

    User select(String id) throws DalException {
        Object[] keys = {id};
        return ((User) super.select(keys));
    }

    protected User convertReaderToObject(OfflineResultSet reader) {
        User ans = null;
        try{
            String username = reader.getString(Columns.Username.name());
            String password = reader.getString(Columns.Password.name());
            String loggedIn = reader.getString(Columns.LoggedIn.name());

            ans = new User(username,password);

            Set<Authorization> auth = this.userAuthorizationsDAO.getAll(username);
            ans.setAuthorizations(auth);

        } catch (Exception throwables) {
            // throwables.printStackTrace();
        }
        return ans;
    }

}


