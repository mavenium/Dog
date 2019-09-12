package ir.mavenium.dog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withActivity(MainActivity.this)
                .withPermissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            Toast.makeText(MainActivity.this,"تمام دسترسی ها تایید شدند",Toast.LENGTH_SHORT).show();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()){
                            Toast.makeText(MainActivity.this,"حداقل یک دسترسی کامل رد شده است، برای تایید آن به تنظیمات برنامه بروید!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, final PermissionToken token) {
                        Snackbar.make(viewPager,"برای ادامه دادن، این دسترسی ها لازم هستند!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("اجازه دادن!", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        token.continuePermissionRequest();
                                    }
                                }).show();
                    }
                }).check();

        if(hasInternetConnection()){
            Toast.makeText(this, "Have Connection !", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No Connection", Toast.LENGTH_LONG).show();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tableLayout = findViewById(R.id.tab_layout);
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), tableLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        settingsChecker();
    }

    public void settingsChecker() {
        String language = sharedPreferences.getString("app_language", "en_US");
        Configuration configuration = getResources().getConfiguration();
        Locale currentLocale = getResources().getConfiguration().locale;

        if (!language.equals(currentLocale.toString())) {
            switch (language) {
                case "fa_IR":
                    configuration.setLocale(new Locale("fa", "IR"));
                    break;
                case "en_US":
                    configuration.setLocale(new Locale("en", "US"));
                    break;
            }

            Snackbar.make(viewPager, getText(R.string.snackbar_text), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.snackbar_action), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }).show();
        }
    }

    @Override
    protected void onResume() {
        settingsChecker();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case 1:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }
}
