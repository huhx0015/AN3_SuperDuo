package it.jaschke.alexandria.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
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
        addPreferencesFromResource(R.xml.preferences);
    }
}
