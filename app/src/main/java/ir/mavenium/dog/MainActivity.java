package ir.mavenium.dog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private TabLayout tableLayout;
    private ViewPager viewPager;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withActivity(MainActivity.this)
                .withPermissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE)
                .withListener(new MultiplePermissionsListener() {

                    /**
                     * @param report
                     */
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            Log.i(TAG, "onPermissionsChecked: All Permissions Granted !");
                        }
                        if (report.isAnyPermissionPermanentlyDenied()){
                            Toast.makeText(MainActivity.this,getText(R.string.any_permission_permanently_denied),Toast.LENGTH_SHORT).show();
                        }
                    }

                    /**
                     * @param permissions
                     * @param token
                     */
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, final PermissionToken token) {
                        Snackbar.make(viewPager,getText(R.string.need_permission), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getText(R.string.allow_permission), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        token.continuePermissionRequest();
                                    }
                                }).show();
                    }

                }).check();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        tableLayout = findViewById(R.id.tab_layout);
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * @param tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            /**
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            /**
             * @param tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), tableLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.option_menu_about:
                final AlertDialog.Builder infoApp = new AlertDialog.Builder(MainActivity.this);
                infoApp.setTitle(getString(R.string.app_information)).setMessage(getText(R.string.app_information_message)).setPositiveButton(getText(R.string.app_home_page), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent showHomePage = new Intent(Intent.ACTION_VIEW);
                        showHomePage.setData(Uri.parse("https://github.com/mavenium/Dog"));
                        startActivity(showHomePage);
                    }
                }).setNeutralButton(getText(R.string.app_information_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder exitApp = new AlertDialog.Builder(MainActivity.this);
            exitApp.setTitle(getString(R.string.app_exit_title)).setMessage(getString(R.string.app_exit_message)).setPositiveButton(getString(R.string.app_exit_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            }).setNeutralButton(getString(R.string.app_exit_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setCancelable(false).show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param locale
     */
    @Override
    public void updateLocale(Locale locale) {
        super.updateLocale(locale);
        setTitle(R.string.app_name);
    }
}
