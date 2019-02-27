package spokli.de.mydbmanager;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class InputActivity extends Activity {

    Spinner spn_database = null;
    Spinner spn_schema = null;
    Spinner spn_table = null;

    ArrayList<String> databases = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        spn_database = (Spinner) this.findViewById(R.id.spn_database);
        spn_schema = (Spinner) this.findViewById(R.id.spn_schema);
        spn_table = (Spinner) this.findViewById(R.id.spn_table);

        addEventListeners();

        fillDatabaseSpinner();

    }

    private void fillDatabaseSpinner(){

        ArrayList<String> databases = new ArrayList<String>();

        databases.add("SQLite");
        databases.add("TestDatabase");

        this.databases = databases;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, databases);
        spn_database.setAdapter(arrayAdapter);
//        spn_database.setSelection(0);
    }

    private void fillSchemaSpinner(){
        DatabaseManager db = new DatabaseManager(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, db.getTables());
        spn_table.setAdapter(arrayAdapter);

    }

    private void addEventListeners(){

        spn_database.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Initialize next spinner
                fillSchemaSpinner();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
