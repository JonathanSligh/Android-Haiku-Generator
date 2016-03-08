package midterm.jrs0058.comp3710.csse.eng.auburn.edu.jrs0058HAIKU;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class MainActivity extends FragmentActivity implements Build_Haiku_Fragment.DisplayListener, Display_Haiku_Fragment.DisplayListener2{
    FragmentManager fm;
    Build_Haiku_Fragment b;
    String[] displayArray = new String[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            b = new Build_Haiku_Fragment();
            fm.beginTransaction().add(R.id.fragment_frame, b, "build_haiku").commit();
        }
    }

    /*********************************************************************************************************************************************************************
     ******************  Used to pass info from build to disaplay fragment
     *********************************************************************************************************************************************************************/
    public void displayHaiku(String line1, String line2, String line3)
    {
        Display_Haiku_Fragment d = new Display_Haiku_Fragment();
        fm.beginTransaction().replace(R.id.fragment_frame, d, "display").addToBackStack(null).commit();
        displayArray[0] = line1;
        displayArray[1] = line2;
        displayArray[2] = line3;
    }
    public String[] getData()
    {
        return displayArray;
    }

}
