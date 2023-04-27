package dataAccessLayer.employeeModule;

import businessLayer.employeeModule.Shift;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class ShiftToCancelsDAO extends DAO{
    private static ShiftToCancelsDAO instance;
    private HashMap<Integer, List<String>> cache;
    private enum Columns {
        ShiftDate,
        ShiftType,
        Branch,
        CancelAction;
    }
    private ShiftToCancelsDAO()throws DalException {
        super("SHIFT_CANCELS", new String[]{Columns.ShiftDate.name(), Columns.ShiftType.name(), Columns.Branch.name()});
        this.cache = new HashMap<>();
    }
    static ShiftToCancelsDAO getInstance() throws DalException {
       if(instance == null)
          instance = new ShiftToCancelsDAO();
       return instance;
    }
    private int getHashCode(LocalDate dt, Shift.ShiftType st, String branch){
        return (formatLocalDate(dt) + st.name() + branch).hashCode();
    }

    List<String> getAll(LocalDate dt, Shift.ShiftType st, String branch) throws DalException {
        if (this.cache.get(getHashCode(dt,st,branch))!=null)
            return this.cache.get(getHashCode(dt,st,branch));
        List<String> ans = this.select(dt,st.name(),branch);
        this.cache.put(getHashCode(dt,st,branch),ans);
        return ans;
    }

    void create(Shift shift, String branch) throws DalException {
        try {
            if(this.cache.containsKey(getHashCode(shift.getShiftDate(), shift.getShiftType(), branch)))
                throw new DalException("Key already exists!");
            List<String> entries = new LinkedList<>();
            for(String str: shift.getShiftCancels()) {
                String queryString = String.format("INSERT INTO " + TABLE_NAME + "(%s, %s, %s, %s) VALUES('%s','%s','%s','%s')",
                        ShiftToCancelsDAO.Columns.ShiftDate.name(), ShiftToCancelsDAO.Columns.ShiftType.name(), ShiftToCancelsDAO.Columns.Branch.name(), Columns.CancelAction.name(),
                        formatLocalDate(shift.getShiftDate()), shift.getShiftType().name(), branch, str);
                cursor.executeWrite(queryString);
                entries.add(str);
            }
            this.cache.put(getHashCode(shift.getShiftDate(), shift.getShiftType(), branch),entries );
        } catch (SQLException e) {
            throw new DalException(e);
        }
    }

    void update(Shift s, String branch) throws DalException {
        if(!this.cache.containsKey(getHashCode(s.getShiftDate(), s.getShiftType(), branch)))
            throw new DalException("Key doesnt exist! Create it first.");
        this.delete(s,branch);
        this.create(s,branch);
    }

    void delete(Shift s, String branch) throws DalException {
        this.cache.remove(getHashCode(s.getShiftDate(),s.getShiftType(),branch));
        Object[] keys = {s.getShiftDate(),s.getShiftType().name(),branch};
        super.delete(keys);
    }

    List<String> select(LocalDate date, String shiftType, String branch) throws DalException {
        Object[] keys = {date,shiftType, branch};
        return ((List<String>) super.select(keys));
    }

    protected List<String> convertReaderToObject(OfflineResultSet reader) {
        List<String> ans = new LinkedList<>();
        try {

            while (reader.next()) {
                String str = reader.getString(Columns.CancelAction.name());
                if(str == null)
                    continue;
                ans.add(str);

            }
        }catch (Exception e){ }
        return ans;
    }

    public void deleteAll() throws DalException{
        super.deleteAll();
        cache.clear();
    }
}
