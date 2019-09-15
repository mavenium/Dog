package ir.mavenium.dog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class SettingsActivity extends BaseActivity {

    private Toolbar settingToolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        settingToolbar = findViewById(R.id.settings_activity_toolbar);
        setSupportActionBar(settingToolbar);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_activity_settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("app_language")) {
                    updateLocale(new Locale(sharedPreferences.getString("app_language", "en")));

                    Intent refresh = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(refresh);
                }
            }

        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void updateLocale(Locale locale) {
        super.updateLocale(locale);
    }
}