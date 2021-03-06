package barqsoft.footballscores.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import barqsoft.footballscores.R;

/** -----------------------------------------------------------------------------------------------
 *  [AboutActivity] CLASS
 *  ORIGINAL DEVELOPER: Yehya Khaled
 *  MODIFIED BY: Michael Yoon Huh (HUHX0015)
 *  DESCRIPTION: AboutActivity is responsible for displaying the application's purpose and
 *  copyrights.
 *  -----------------------------------------------------------------------------------------------
 */

public class AboutActivity extends AppCompatActivity {

    /** ACTIVITY LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    // PlaceholderFragment: A placeholder fragment containing a simple view.
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_about, container, false);
        }
    }
}
