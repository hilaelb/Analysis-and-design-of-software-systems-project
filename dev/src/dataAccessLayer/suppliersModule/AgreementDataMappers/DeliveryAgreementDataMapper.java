package dataAccessLayer.suppliersModule.AgreementDataMappers;

import businessLayer.suppliersModule.DeliveryAgreements.DeliveryAgreement;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryByInvitation;
import businessLayer.suppliersModule.DeliveryAgreements.DeliveryFixedDays;
import dataAccessLayer.dalAbstracts.AbstractDataMapper;
import dataAccessLayer.dalUtils.OfflineResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryAgreementDataMapper  extends AbstractDataMapper {
    public DeliveryAgreementDataMapper() {
        super("delivery_agreement", new String[] {"bn_number", "have_transport", "by_invitation", "days"});
    }

    public void insert(String bnNumber, boolean haveTransport, boolean byInvitation, String days) throws SQLException {
        String columnsString = String.join(", ", columns);
        sqlExecutor.executeWrite(String.format("INSERT INTO %s (%s) VALUES('%s', %d, %d, '%s')",tableName, columnsString, bnNumber,
                haveTransport? 1 : 0, byInvitation? 1 : 0, days));
    }

    public void updateHaveTransport(String bnNumber, boolean haveTransport) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET have_transport = %d WHERE bn_number = '%s'", tableName, haveTransport? 1 : 0, bnNumber));
    }

    public void updateByInvitation(String bnNumber, boolean byInvitation) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET by_invitation = %d WHERE bn_number = '%s'", tableName, byInvitation? 1 : 0, bnNumber));
    }

    public void updateDays(String bnNumber, String days) throws SQLException {
        sqlExecutor.executeWrite(String.format("UPDATE %s SET days = %s WHERE bn_number = '%s'", tableName, days));
    }

    public DeliveryAgreement find(String bnNumber) throws SQLException {
        String columnsString = String.join(", ", columns);
        OfflineResultSet resultSet = sqlExecutor.executeRead(String.format("SELECT %s FROM %s WHERE bn_number = '%s'",columnsString,tableName,
                bnNumber));
        if(resultSet.next()) {
            boolean byInvitation = resultSet.getInt("by_invitation") == 1;
            if (byInvitation)
                return new DeliveryByInvitation(resultSet.getInt("have_transport") == 1, Integer.parseInt(resultSet.getString("days")));
            String[] daysString = resultSet.getString("days").split(",");
            List<Integer> days = new ArrayList<>();
            for (int i = 0; i < daysString.length; i++)
                days.add(Integer.parseInt(daysString[i]));
            return new DeliveryFixedDays(resultSet.getInt("have_transport") == 1, days);
        }
        return null;
    }

    public void delete(String bnNumber) throws SQLException {
        sqlExecutor.executeWrite(String.format("DELETE FROM %s WHERE bn_number = '%s'", tableName, bnNumber));
    }
}
