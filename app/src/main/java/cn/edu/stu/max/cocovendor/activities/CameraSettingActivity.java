package cn.edu.stu.max.cocovendor.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.os.Bundle;
import android.view.MenuItem;

import cn.edu.stu.max.cocovendor.R;

public class CameraSettingActivity extends AppCompatPreferenceActivity {

    private static ListPreference revolutionListPreference = null;
    private static ListPreference spyTimeListPreference = null;
    private static SwitchPreference switchPreference = null;
    public static boolean isPreferenceChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new CameraSettingFragment()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isPreferenceChanged = false;
    }

    public static class CameraSettingFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //从xml文件中添加Preference项
            addPreferencesFromResource(R.xml.camera_setting_preferences);
            //findPreference函数通过key可以找到对应的设置项
            switchPreference = (SwitchPreference)findPreference("SwitchPreference");
            switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    isPreferenceChanged = true;
                    return true;
                }
            });
            //switchPreference.setOnPreferenceChangeListener(CameraSettingActivity.this);
            revolutionListPreference = (ListPreference) findPreference("RevolutionListPreference");
            revolutionListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    isPreferenceChanged = true;
                    ListPreference listPreference = (ListPreference) preference;
                    CharSequence[] entries = listPreference.getEntries();
                    int index = listPreference.findIndexOfValue((String)o);
                    listPreference.setSummary(entries[index]);
                    return true;
                }
            });
//            revolutionListPreference.setOnPreferenceChangeListener(this);
            revolutionListPreference.setSummary(revolutionListPreference.getEntry());
            spyTimeListPreference = (ListPreference) findPreference("SpyTimeListPreference");
            spyTimeListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    isPreferenceChanged = true;
                    ListPreference listPreference = (ListPreference) preference;
                    CharSequence[] entries = listPreference.getEntries();
                    int index = listPreference.findIndexOfValue((String)o);
                    listPreference.setSummary(entries[index]);
                    return true;
                }
            });
//            spyTimeListPreference.setOnPreferenceChangeListener(this);
            spyTimeListPreference.setSummary(spyTimeListPreference.getEntry());
        }
    }

    /*
     * (non-Javadoc)
     * @param preference
     * @param newValue
     * @return
     * @see android.preference.Preference.OnPreferenceChangeListener#onPreferenceChange(android.preference.Preference, java.lang.Object)
     */
//    @Override
//    public boolean onPreferenceChange(Preference preference, Object newValue) {
//        // TODO Auto-generated method stub
//        //判断是哪个Preference改变了
//        if (preference.getKey().equals("RevolutionListPreference")) {
//            ListPreference listPreference = (ListPreference) preference;
//            CharSequence[] entries = listPreference.getEntries();
//            int index = listPreference.findIndexOfValue((String)newValue);
//            listPreference.setSummary(entries[index]);
//        } else if (preference.getKey().equals("SwitchPreference")){
//            //读取设置信息
//            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//            boolean sw = settings.getBoolean("SwitchPreference", false);
//        } else if (preference.getKey().equals("SpyTimeListPreference")){
//            ListPreference listPreference = (ListPreference) preference;
//            CharSequence[] entries = listPreference.getEntries();
//            int index = listPreference.findIndexOfValue((String)newValue);
//            listPreference.setSummary(entries[index]);
//        }else{
//            //如果返回false表示不允许被改变
//            return false;
//        }
//        //返回true表示允许改变
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (isPreferenceChanged) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CameraSettingActivity.this);
                builder.setMessage("设置发生变化，需重启后生效，是否立即重启？");
                builder.setTitle("提示");
                builder.setPositiveButton("立即重启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("稍后重启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                return true;
            } else {
                finish();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
