package TransportModule.BusinessLayer.Records;

import java.util.Objects;

public record Truck (String id, String model, int baseWeight, int maxWeight, CoolingCapacity coolingCapacity){

    public enum CoolingCapacity{
        NONE,
        COLD,
        FROZEN
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return id == truck.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
