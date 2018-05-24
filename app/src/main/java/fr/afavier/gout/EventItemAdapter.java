package fr.afavier.gout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.afavier.gout.entities.Event;

/**
 * Created by favier on 18/04/2018.
 */

public class EventItemAdapter extends ArrayAdapter<Event> {

    Context context;
    LayoutInflater layoutInflater=null;

    public EventItemAdapter(Context context, int resource, List<Event> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public LayoutInflater getLayoutInflater() {
        if(layoutInflater==null)
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vue;
        if(convertView==null) {

            vue = getLayoutInflater().inflate(R.layout.listitem_event, parent, false);
        }
        else
            vue = convertView;
        TextView titre = (TextView)vue.findViewById(R.id.listitem_Titre);
        TextView description = (TextView)vue.findViewById(R.id.listitem_Description);
        TextView autor = (TextView)vue.findViewById(R.id.listitem_Autor);

        Event event= (Event)getItem(position);

        vue.setTag(event);
        titre.setText(event.getName());
        description.setText(event.getDescription());
        autor.setText("Organizer : " + event.getPseudo_organizer());

        return vue;

    }
}
