package fr.afavier.gout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.afavier.gout.entities.Entities;
import fr.afavier.gout.entities.Event;

public class ListEventActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);

        /** Toolbar config **/
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /** End Toolbar config **/

        /** Test des params **/
        if(!getIntent().hasExtra("params")){
            finish();
        }


        listView = (ListView)findViewById(R.id.lst_eventList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object tag = view.getTag();
                if(tag==null)
                    return;
                Event event = (Event) tag;
                if(event==null)
                    return;

                int eventId = event.getId();
                Intent intent = new Intent(ListEventActivity.this,ViewEventActivity.class);
                intent.putExtra("event",eventId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            public void run() {
                ArrayList<String> params = getIntent().getStringArrayListExtra("params");
                Entities entities = new Entities();
                final ArrayList<Event> events = entities.EventList(params);

                runOnUiThread(new Runnable() {
                    public void run() {
                        if(events.get(0).getId() == 0){
                            Intent intent = new Intent(ListEventActivity.this,MainActivity.class);
                            intent.putExtra("msg","Not event with this params");
                            startActivity(intent);
                        }else{
                            EventItemAdapter eventItemAdapter = new EventItemAdapter(ListEventActivity.this, -1, events);
                            listView.setAdapter(eventItemAdapter);
                        }
                    }
                });
            }
        }).start();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}