package com.yasam.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.yasam.sunshine.infrastructure.AppCompatPreferenceActivity;

public class SettingsActivity extends AppCompatPreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    private Preference mPref_Location = null;
    private Preference mPref_Units = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general);

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        mPref_Location = findPreference(getString(R.string.prefKey_location));
        bindPreferenceSummaryToValue(mPref_Location);

        mPref_Units = findPreference(getString(R.string.prefKey_tempUnits));
        bindPreferenceSummaryToValue(mPref_Units);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Memory leak prevention
        dettachOnPreferenceChangeListener(mPref_Location);
        dettachOnPreferenceChangeListener(mPref_Units);
    }

    private void dettachOnPreferenceChangeListener(Preference pref){
        if(pref != null)
            pref.setOnPreferenceClickListener(null);
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    /**
     * Called when a Preference has been changed by the user. This is
     * called before the state of the Preference is about to be updated and
     * before the state is persisted.
     *
     * @param preference The changed Preference.
     * @param newValue   The new value of the Preference.
     * @return True to update the state of the Preference with the new value.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean res = false;

        if(newValue!=null){
            CharSequence prefSummary = newValue.toString();

            if( preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list (since they have separate labels/values).
                ListPreference listPreference = (ListPreference) preference;

                int prefIndx = listPreference.findIndexOfValue(newValue.toString());

                prefSummary = prefIndx >= 0 ? listPreference.getEntries()[prefIndx] : null;
            }

            preference.setSummary(prefSummary);

            res = true;
        }
        return res;
    }

    /**
     * Launch the SettingsActivity
     * @param context
     * @return true if activity is started.
     */
    public static boolean launch(Context context) {
        boolean res = false;
        try{
            context.startActivity(new Intent(context, SettingsActivity.class));
            res = true;
        }catch (Exception ex){

        }
        return res;
    }

    /**
     * Location for the forecast
     *
     * @param context
     * @return
     */
    public static String getLocation(Context context) {
        String res = null;
        if(context != null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            res = pref.getString(
                    context.getString(R.string.prefKey_location),
                    context.getString(R.string.prefDefVal_location)
            );
        }
        return res;
    }

    /**
     * Temperature unit for the forecast
     *
     * @param context
     * @return
     */
    public static boolean isImperial(Context context) {
        boolean res = false;
        if(context!=null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            String temp_unit = pref.getString(
                    context.getString(R.string.prefKey_tempUnits),
                    context.getString(R.string.prefDefVal_tempUnits)
            );
            if (TextUtils.equals(temp_unit, context.getString(R.string.prefVal_tempUnits_imperial)))
                res = true;
        }
        return res;
    }

    /**
     * Open map with preferred location
     * @param context
     * @return
     */
    public static boolean openPreferredLocationOnMap(Context context) {
        boolean res = false;
        String location = getLocation(context);

        if(location != null && !TextUtils.isEmpty(location)){
            Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", location)
                    .build();

            Intent intentMap = new Intent(Intent.ACTION_VIEW)
                    .setData(geoLocation);

            if(intentMap.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intentMap);
            else
                Log.d(LOG_TAG, String.format(
                        "Couldn't view %s on the map, no receiving apps installed!", location));
        }
        return res;
    }
}
