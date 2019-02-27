package spokli.de.mydbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GenericDatabaseManager extends SQLiteOpenHelper {

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "testSchema";
    private String dbname;
    private String tablename;
    private int version;
    private HashMap<String, Column> columns;

    // Pokemons table name
    //private static final String TABLE_POKEMON = "Pokemon";

    // Pokemons Table Columns names
    //private static final String KEY_ID = "id";
    //private static final String KEY_NAME = "name";
    //private static final String KEY_TYPE = "type";

    public GenericDatabaseManager(Context _context, int _version, String _dbname, String _tablename, Column[] _columns) {

        this.dbname = _dbname;
        this.tablename = _tablename;
        this.version = _version;

        for (Column _c : _columns) {
            columns.put(_c.getName(), _c);
        }

        super(_context, _dbname, null, _version);
    }

    private String getColumnSql() {
        String _columnSql = "";
        Iterator<Column> iter = columns.values().iterator();
        while (iter.hasNext()) {
            Column _c = iter.next();
            _columnSql += _c.getName() + " " + _c.getType() + " " + _c.getKeyString() + ", ";
        }
        _columnSql = _columnSql.substring(0, _columnSql.length() - 1); // Remove last comma
        return _columnSql;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase _db) {

        String _createStatement = "CREATE TABLE " + tablename + "("
                + getColumnSql() + ")";

        _db.execSQL(_createStatement);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + tablename);

        // Create tables again
        //onCreate(db);
    }

    public List<String> getTables() {

        List<String> _tableList = new ArrayList<>();

        // Select All Query
        String _query = "SELECT name FROM sqlite_master WHERE type ='table'";

        SQLiteDatabase _db = this.getReadableDatabase();
        Cursor cursor = _db.rawQuery(_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                _tableList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // return contact list
        return _tableList;

    }

    // Adding new line
    public void addLine(Line _line) {
        SQLiteDatabase _db = this.getWritableDatabase();

        ContentValues _values = new ContentValues();

        HashMap<String, Object> lineValues = _line.getValues();
        HashSet<String> columnNames = (HashSet) lineValues.keySet();

        Iterator iter = columnNames.iterator();
        while (iter.hasNext()) {
            String columnName = "" + iter.next();
            Object value = lineValues.get(columnName);

            // Get type of column and convert value
            Column c = columns.get(columnName);
            String type = c.getType();
            if (type.matches("VARCHAR\\([0-9]*\\)")) {
                String castedValue = "" + value;
                _values.put(columnName, castedValue);
            } else if (type.equals("INTEGER")) {
                int castedValue = (int) value;
                _values.put(columnName, castedValue);
            }
        }

        // Inserting Row
        _db.insert(tablename, null, _values);
        _db.close(); // Closing database connection
    }

    // Get lines
    public ArrayList<Line> getLines(HashMap<String, Object> values) {

        ArrayList<Line> lines = new ArrayList<>();
//        String[] _whereColumns = new String[values.size()];
        String[] _whereValues = new String[values.size()];
        String whereConditions = "";

        Iterator iter = values.entrySet().iterator();
        int i = 0;

        while (iter.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
            String columnName = entry.getKey();
            String value = entry.getValue() + "";

            // if columnName is not in global column-list, don't use this entry
            if (columns.containsKey(columnName)) {
                whereConditions += columnName + "=? AND ";
//                _whereColumns[i] = columnName;
                _whereValues[i] = value;
            }
            i++;
        }
        // Remove last "AND" from whereConditions
        whereConditions = whereConditions.substring(0, whereConditions.length() - 4);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(tablename,
                null, // null means select *
                whereConditions,
                _whereValues,
                null, // group
                null, // order
                null, // having
                null); // limit
        if (cursor != null) {
            cursor.moveToFirst();
            do {

                Line line = new Line((Column[]) columns.entrySet().toArray());
                for (int j = 0; j <= cursor.getCount(); j++) {
                    line.setAttribute(cursor.getColumnName(j), cursor.getString(j));
                }
                lines.add(line);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return lines
        return lines;
    }

    // Updating single line
    public int updateLine(Line line) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pokemon.getName());
        values.put(KEY_TYPE, pokemon.getTypeString());

        // updating row
        return db.update(TABLE_POKEMON, values, KEY_ID + " = ?",
                new String[]{String.valueOf(pokemon.getId())});
    }

    ;

    // Deleting single Pokemon
    public void deletePokemon(Pokemon pokemon) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POKEMON, KEY_ID + " = ?",
                new String[]{String.valueOf(pokemon.getId())});
        db.close();
    }

    ;
}
