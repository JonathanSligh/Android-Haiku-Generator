package midterm.jrs0058.comp3710.csse.eng.auburn.edu.jrs0058HAIKU;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonathan on 3/3/2015.
 */
public class WordsAdapter extends ArrayAdapter<Words> {

    private Activity context;
    ArrayList<Words> data = null;

    public WordsAdapter(Activity context, int resource, ArrayList<Words> data)
    {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_row_dropdown, parent, false);
        }

        Words item = data.get(position);

        if(item != null)
        {   // Parse the data from each object and set it.
            TextView text = (TextView) row.findViewById(R.id.text1);
            if(text != null) {
                text.setText(item.getWord());
                text.setTextColor(context.getResources().getColor(android.R.color.black));
                text.setTextSize(20);
            }
        }

        return row;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_row, parent, false);
        }

        Words item = data.get(position);

        if(item != null)
        {   // Parse the data from each object and set it.
            TextView text = (TextView) row.findViewById(R.id.text1);
            if(text != null) {
                text.setText(item.getWord());
                text.setTextColor(context.getResources().getColor(android.R.color.black));
                text.setTextSize(20);
            }
        }

        return row;
    }
}