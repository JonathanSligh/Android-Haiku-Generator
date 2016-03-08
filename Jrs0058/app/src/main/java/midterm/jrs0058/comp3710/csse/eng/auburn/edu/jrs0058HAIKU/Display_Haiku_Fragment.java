package midterm.jrs0058.comp3710.csse.eng.auburn.edu.jrs0058HAIKU;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Jonathan on 3/3/2015.
 */
public class Display_Haiku_Fragment extends Fragment {
    DisplayListener2 mCallback;
    Activity act;
    View v;
    public interface DisplayListener2 {
        public String[] getData();
    }
    @Override
    public void onAttach(Activity a)
    {
        super.onAttach(a);
        act = a;
        try{
            mCallback = (DisplayListener2) a;
        }
        catch (ClassCastException e){
            throw new ClassCastException(a.toString() + "must implement OnNumberPressedListener");
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView _1 = (TextView) v.findViewById(R.id.display_first_line);
        TextView _2 = (TextView) v.findViewById(R.id.display_second_line);
        TextView _3 = (TextView) v.findViewById(R.id.display_third_line);
        outState.putString("line_1", _1.getText().toString());
        outState.putString("line_2", _2.getText().toString());
        outState.putString("line_3", _3.getText().toString());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.display_haiku_fragment, container, false);
        TextView _1 = (TextView) v.findViewById(R.id.display_first_line);
        TextView _2 = (TextView) v.findViewById(R.id.display_second_line);
        TextView _3 = (TextView) v.findViewById(R.id.display_third_line);
        if (savedInstanceState != null)
        {
            _1.setText(savedInstanceState.getString("line_1"));
            _2.setText(savedInstanceState.getString("line_2"));
            _3.setText(savedInstanceState.getString("line_3"));
        }
        else {
            String[] temp = mCallback.getData();
            _1.setText(temp[0]);
            _2.setText(temp[1]);
            _3.setText(temp[2]);
        }
        return v;
    }
}
