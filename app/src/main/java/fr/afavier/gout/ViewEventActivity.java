package fr.afavier.gout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;

import fr.afavier.gout.entities.Categorie;
import fr.afavier.gout.entities.Clientele;
import fr.afavier.gout.entities.Event;
import fr.afavier.gout.entities.Tarif;

public class ViewEventActivity extends AppCompatActivity {
    TextView title;
    TextView description;
    TextView organizer;
    TextView dates;
    TextView adresse;
    TextView booking;
    TextView categorie;
    TextView clientele;
    TextView participation;

    LinearLayout prices;
    LinearLayout categories;
    LinearLayout clienteles;

    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        /** Toolbar config **/
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /** End Toolbar config **/

        final TextView title = findViewById(R.id.title);
        final TextView description = findViewById(R.id.description);
        final TextView organizer = findViewById(R.id.organizer);
        final TextView dates = findViewById(R.id.dates);
        final TextView adresse = findViewById(R.id.adresse);
        final TextView booking = findViewById(R.id.booking);
        final TextView participation = findViewById(R.id.participation);

        final LinearLayout prices = findViewById(R.id.prices);
        final LinearLayout categories = findViewById(R.id.categories);
        final LinearLayout clienteles = findViewById(R.id.clienteles);

        title.setText(R.string.loading);

        new Thread(new Runnable() {
            public void run() {
                final int eventId = getIntent().getExtras().getInt("event");

                final Event event = new Event(eventId);

                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @SuppressLint("ResourceAsColor")
                    public void run() {
                        if(event.getId() == 0){
                            Intent intent = new Intent(ViewEventActivity.this,MainActivity.class);
                            intent.putExtra("msg","No event find");
                            finish();
                        }else{
                            Log.i("DEBUG", event.toString());

                            title.setText(event.getName());

                            if(event.isBooking()){
                                booking.setText(getString(R.string.bookingEvent));
                            }

                            description.setText(event.getDescription());

                            str = getString(R.string.organiser) + " : " + event.getPseudo_organizer();
                            organizer.setText(str);

                            String stringDateStart = DateFormat.getDateTimeInstance().format(event.getDateStart());
                            String stringDateEnd = DateFormat.getDateTimeInstance().format(event.getDateEnd());
                            str = stringDateStart + " " + getString(R.string.to) + " " + stringDateEnd;
                            dates.setText(str);

                            str = event.getCity() + " " + event.getPostalCode() + " " + event.getAdresse();
                            adresse.setText(str);

                            for(Tarif t : event.getListTarif()){
                                str = t.getName() + " : " + t.getPrice();
                                TextView  textView = new TextView(ViewEventActivity.this);
                                textView.setText(str);
                                prices.addView(textView);
                            }

                            for(Categorie c : event.getListCategorie()){
                                TextView textView = new TextView(ViewEventActivity.this);
                                textView.setText(c.getName());
                                categories.addView(textView);
                            }

                            for(Clientele c : event.getListClientele()){
                                TextView  textView = new TextView(ViewEventActivity.this);
                                textView.setText(c.getName());
                                clienteles.addView(textView);
                            }

                            participation.setText(String.valueOf(event.getNbParticipant()));
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
