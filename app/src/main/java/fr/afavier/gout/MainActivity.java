package fr.afavier.gout;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;

import fr.afavier.gout.entities.Entities;
import fr.afavier.gout.entities.Event;
import fr.afavier.gout.entities.User;

public class MainActivity extends AppCompatActivity {

    Button valid_findEvent;

    EditText form_eventName, form_pseudoOrganizer, form_postalCode, form_city, form_dateStart, form_dateEnd;
    RadioGroup form_booking;

    Entities enti;

    final ArrayList<String> params = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Toolbar config **/
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /** End Toolbar config **/

        Intent intent = getIntent();

        if(intent.hasExtra("msg")){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Sorry ...");
            builder.setMessage(intent.getStringExtra("msg"));
            builder.setPositiveButton("Ok", null);
            builder.show();
            intent.removeExtra("msg");
        }

        /** GET champs data **/
        form_eventName = (EditText) findViewById(R.id.form_eventName);
        form_pseudoOrganizer = (EditText) findViewById(R.id.form_pseudoOrganizer);
        form_postalCode = (EditText) findViewById(R.id.form_postalCode);
        form_city = (EditText) findViewById(R.id.form_city);
        form_dateStart = (EditText) findViewById(R.id.form_dateStart);
        form_dateEnd = (EditText) findViewById(R.id.form_dateEnd);

        form_booking = (RadioGroup) findViewById(R.id.form_booking);

        /** Button Listener **/
        addListenerOnButton();
    }


    public void addListenerOnButton() {
        params.clear();

        valid_findEvent = (Button) findViewById(R.id.btn_findEvent);
        valid_findEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(!form_eventName.getText().toString().equals("")){
                    params.add("name="+form_eventName.getText());
                }
                if(!form_pseudoOrganizer.getText().toString().equals("")){
                    params.add("pseudoOrganizer="+form_pseudoOrganizer.getText());
                }
                if(!form_postalCode.getText().toString().equals("")){
                    params.add("postalCode="+form_postalCode.getText());
                }
                if(!form_city.getText().toString().equals("")){
                    params.add("city="+form_city.getText());
                }
                if(!form_dateStart.getText().toString().equals("")){
                    params.add("dateStart="+form_dateStart.getText());
                }
                if(!form_dateEnd.getText().toString().equals("")){
                    params.add("dateEnd="+form_dateEnd.getText());
                }

                Intent intent = new Intent(MainActivity.this,ListEventActivity.class);
                intent.putExtra("params",params);
                startActivity(intent);
            }
        });
    }
}
