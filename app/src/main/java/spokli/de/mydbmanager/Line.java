package spokli.de.mydbmanager;

import java.util.ArrayList;
import java.util.HashMap;

public class Line {

    public HashMap<String, Object> values;

    public Line(Column[] columns) {
        for (Column c : columns) {
            String columnName = c.getName();

            values.put(columnName, null);
        }
    }

    public void setAttribute(String columnName, Object value){
        if(values.keySet().contains(columnName)){
            values.put(columnName, value);
        }
        else {
            //TODO schicke Exception zurück, dass Spalte nicht existiert
        }
    }

    public Object getAttribute(String columnName){
        if(values.keySet().contains(columnName)){
            return values.get(columnName);
        }
        else {
            //TODO schicke Exception zurück, dass Spalte nicht existiert
            return null;
        }
    }

    public HashMap<String, Object> getValues(){
        return values;
    }
}
