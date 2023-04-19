package DataAccessLayer;

//import org.sqlite.SQLiteConnection;
import java.sql.SQLException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public abstract class DAO<T extends DTO> {
    private Connection connection = null;
    private PreparedStatement ptmt = null;
    private ResultSet resultSet = null;

    private String tableName = "";

    public DAO(String tableName){
        this.tableName = tableName;
    }
    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionFactory.getInstance().getConnection();
        return conn;
    }

    public void add(T studentBean) {
        try {
            String queryString = "INSERT INTO student(RollNo, Name, Course, Address) VALUES(?,?,?,?)";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setInt(1, studentBean.getRollNo());
            ptmt.setString(2, studentBean.getName());
            ptmt.setString(3, studentBean.getCourse());
            ptmt.setString(4, studentBean.getAddress());
            ptmt.executeUpdate();
            System.out.println("Data Added Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public boolean Update(int id, String attributeName, String attributeValue)
    {
        try {
            String queryString = "UPDATE "+tableName+" SET "+attributeName+"=? WHERE ID=?";
            connection = getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setString(1, attributeValue);
            ptmt.setInt(2, id);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ptmt != null)
                    ptmt.close();
                if (connection != null)
                    connection.close();
            }

            catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public T findOne(final long id) {
        return entityManager.find(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    public T create(final T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public T update(final T entity) {
        return entityManager.merge(entity);
    }

    public void delete(final T entity) {
        entityManager.remove(entity);
    }

    public void deleteById(final long entityId) {
        final T entity = findOne(entityId);
        delete(entity);
    }
}