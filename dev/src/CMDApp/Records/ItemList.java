package CMDApp.Records;

import java.util.HashMap;
import java.util.Objects;

public record ItemList (int id, HashMap<String, Integer> load,HashMap<String, Integer> unload) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemList itemList = (ItemList) o;
        return id == itemList.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
