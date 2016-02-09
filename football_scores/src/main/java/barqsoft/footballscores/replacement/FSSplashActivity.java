package barqsoft.footballscores.replacement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 12/29/2015.
 */
public class FSSplashActivity extends AppCompatActivity {

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
        //setContentView();
        ButterKnife.bind(this);
    }
}
