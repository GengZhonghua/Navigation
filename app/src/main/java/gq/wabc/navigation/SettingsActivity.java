package gq.wabc.navigation;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    Preference openService, help, version;
    CheckBoxPreference backPosition;
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置up键可用
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        addPreferencesFromResource(R.xml.preferences);

        openService = findPreference(getString(R.string.string_open_service));
        backPosition = (CheckBoxPreference) findPreference(getString(R.string.string_position_back));
        help = findPreference(getString(R.string.string_help));
        version = findPreference(getString(R.string.string_version));
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            this.version.setSummary(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        openService.setOnPreferenceClickListener(this);
        help.setOnPreferenceClickListener(this);
        backPosition.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //点击了up键
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Activity销毁时启动辅助服务
        Intent intent = new Intent(this, NavigationService.class);
        startService(intent);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        String key = preference.getKey();
        if (key.equals(getString(R.string.string_open_service))) {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        } else if (key.equals(getString(R.string.string_help))) {
            new AlertDialog.Builder(this)
                    .setView(LayoutInflater.from(this).inflate(R.layout.help, null)).show();
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        NavigationService.backPosition = (boolean) newValue;
        ((CheckBoxPreference) preference).setChecked((boolean) newValue);

        return false;
    }
}
