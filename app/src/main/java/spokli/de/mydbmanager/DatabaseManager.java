package spokli.de.mydbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 04.11.2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "testSchema";

    // Pokemons table name
    private static final String TABLE_POKEMON = "Pokemon";

    // Pokemons Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POKEMON_TABLE = "CREATE TABLE " + TABLE_POKEMON + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_POKEMON_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POKEMON);

        // Create tables again
        onCreate(db);
    }

    public List<String> getTables(){

        List<String> tableList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT name FROM sqlite_temp_master WHERE type ='table'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                tableList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // return contact list
        return tableList;

    }

    // Adding new Pokemon
    public void addPokemon(Pokemon pokemon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, pokemon.getName());
        values.put(KEY_NAME, pokemon.getName());
        values.put(KEY_TYPE, pokemon.getTypeString());

        // Inserting Row
        db.insert(TABLE_POKEMON, null, values);
        db.close(); // Closing database connection
    };


    // Getting single Pokemon
    public Pokemon getPokemon(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(   TABLE_POKEMON,
                                    new String[] {
                                            KEY_ID,
                                            KEY_NAME,
                                            KEY_TYPE },
                                    KEY_ID + "=?",
                                    new String[] {
                                            String.valueOf(id) },
                                    null,
                                    null,
                                    null,
                                    null);
        if (cursor != null)
            cursor.moveToFirst();

        Pokemon pokemon = new Pokemon(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return pokemon;
    };

    // Getting All Pokemon
    public List<Pokemon> getAllPokemon() {
        List<Pokemon> pokemonList = new ArrayList<Pokemon>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_POKEMON;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pokemon pokemon = new Pokemon(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
                // Adding contact to list
                pokemonList.add(pokemon);
            } while (cursor.moveToNext());
        }

        // return contact list
        return pokemonList;
    };

    // Getting Pokemon Count
    public int getPokemonCount() {
        String countQuery = "SELECT  * FROM " + TABLE_POKEMON;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    };

    // Updating single Pokemon
    public int updatePokemon(Pokemon pokemon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pokemon.getName());
        values.put(KEY_TYPE, pokemon.getTypeString());

        // updating row
        return db.update(TABLE_POKEMON, values, KEY_ID + " = ?",
                new String[] { String.valueOf(pokemon.getId()) });
    };

    // Deleting single Pokemon
    public void deletePokemon(Pokemon pokemon) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POKEMON, KEY_ID + " = ?",
                new String[] { String.valueOf(pokemon.getId()) });
        db.close();
    };
}
