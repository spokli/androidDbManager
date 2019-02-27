package spokli.de.mydbmanager;

public class Column{

    private String name;
    private String type;
    private boolean key;

    public Column(String name, String type, boolean key){
        this.name = name;
        this.type = type;
        this.key = key;
    }

    public String getName(){
        return this.name;
    }
    public String getType(){
        return this.type;
    }
    public boolean getKey(){
        return this.key;
    }
    public String getKeyString(){
        if (this.key)
            return "PRIMARY KEY";
        else return "";
    }

    public String getTypeByName(String columnName){

    }
}