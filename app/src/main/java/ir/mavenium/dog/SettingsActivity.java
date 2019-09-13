package ir.mavenium.dog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar settingToolbar;
    private SharedPreferences sharedPreferences;

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
            settingsChecker();

            finish();

            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
        }
        return super.onOptionsItemSelected(item);
    }

    public void settingsChecker() {
        String language = sharedPreferences.getString("app_language", "en");

        Locale currentLocale = getResources().getConfiguration().locale;

        if (!language.equals(currentLocale.toString())) {

            Context context = LocaleHelper.setLocale(this, language);
            Resources resources = context.getResources();
            Locale appLocale = new Locale(language);
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(appLocale);

            resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        }

    }
}