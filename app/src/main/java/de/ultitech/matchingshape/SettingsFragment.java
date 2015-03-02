package de.ultitech.matchingshape;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by davidgreiner on 3/2/15.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
