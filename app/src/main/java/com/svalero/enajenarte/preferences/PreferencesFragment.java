package com.svalero.enajenarte.preferences;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import com.svalero.enajenarte.R;

public class PreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle saveInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey);
    }
}
