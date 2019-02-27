package spokli.de.mydbmanager;

/**
 * Created by Marco on 04.11.2015.
 */
public class Pokemon {

    private int id;
    private String name;
    private Type type;

    public Pokemon(int id, String name, Type type){
        this.setId(id);
        this.setName(name);
        this.setType(type);
    }

    public Pokemon(int id, String name, String type){
        this.setId(id);
        this.setName(name);
        this.setType(Type.valueOf(type.toUpperCase()));
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }
    public String getTypeString() {
        return "" + type;
    }
    public void setType(Type type) {
        this.type = type;
    }
}
