package compilador;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Object> table;

    public SymbolTable() {
        table = new HashMap<>();
    }

    public void add(String id, Object value) {
        table.put(id, value);
    }

    public Object get(String id) {
        return table.get(id);
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "table=" + table +
                '}';
    }
}