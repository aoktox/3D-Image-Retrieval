package id.prasetiyo.a3dimageretrieval.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import id.prasetiyo.a3dimageretrieval.R;
import id.prasetiyo.a3dimageretrieval.models.Similar;

/**
 * Created by aoktox on 02/07/16.
 */
public class SimilarAdapter extends ArrayAdapter<Similar> {
    public SimilarAdapter(Context context, ArrayList<Similar> similars) {
        super(context, 0, similars);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Similar similar = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_similar, parent, false);
        }
        // Lookup view for data population
        TextView id_txt = (TextView) convertView.findViewById(R.id.textView_id);
        TextView jarak_txt = (TextView) convertView.findViewById(R.id.textView_jarak);
        // Populate the data into the template view using the data object
        id_txt.setText("Objek = "+similar.getId());
        jarak_txt.setText("Tingkat kemiripan = "+similar.getJarak());
        // Return the completed view to render on screen
        return convertView;
    }
}