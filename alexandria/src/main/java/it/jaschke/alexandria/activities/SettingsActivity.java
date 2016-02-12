package it.jaschke.alexandria.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import it.jaschke.alexandria.R;

/** -----------------------------------------------------------------------------------------------
 *  [SettingsActivity] CLASS
 *  ORIGINAL DEVELOPER: Sascha Jaschke
 *  DESCRIPTION: SettingsActivity activity displays the SharedPreferences options.
 *  -----------------------------------------------------------------------------------------------
 */

public class SettingsActivity extends PreferenceActivity {

    /** ACTIVITY LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.preferences);

        // Sets a listener for the Toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_settings_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
