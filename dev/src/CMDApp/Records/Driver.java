package CMDApp.Records;

import java.util.Objects;

public record Driver (int id, String name, LicenseType licenseType){

    enum LicenseType{
        A1,A2,A3,B1,B2,B3,C1,C2,C3
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return id == driver.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
