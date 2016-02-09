package barqsoft.footballscores.replacement;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import barqsoft.footballscores.R;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 12/29/2015.
 */
public class FSMainActivity extends AppCompatActivity {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    /** ACTIVITY LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void initLayout() {
        setContentView(R.layout.activity_fs_main);
        ButterKnife.bind(this);

        initSlidingTabs();
    }

    private void initSlidingTabs() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment();
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();
    }
}
