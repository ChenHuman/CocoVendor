package cn.edu.stu.max.cocovendor.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.litepal.LitePal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import cn.edu.stu.max.cocovendor.javaClass.ToastFactory;
import cn.edu.stu.max.cocovendor.R;
import cn.edu.stu.max.cocovendor.javaClass.ViewHolder;
import cn.edu.stu.max.cocovendor.services.VideoService;
import cn.edu.stu.max.cocovendor.databaseClass.LocalInfo;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化数据库
        LitePal.initialize(this);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        // 设置监控摄像默认值
        String packageName = getPackageName();
        SharedPreferences settings = getSharedPreferences(packageName + "_preferences", MODE_PRIVATE);
        boolean sw = settings.getBoolean("SwitchPreference", false);
        String revolution = settings.getString("RevolutionListPreference", "cif");
        String spyTime = settings.getString("SpyTimeListPreference", "24hours");
        ViewHolder.revolution = revolution;
        ViewHolder.sw = sw;
        ViewHolder.spyTime = spyTime;

        // 启动摄像服务
        startService(new Intent(StartActivity.this, VideoService.class));

        // 判断程序是否首次启动，配置默认设置
        SharedPreferences preferences = getSharedPreferences("default_setting", MODE_PRIVATE);
        if (preferences.getBoolean("firstRun", true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
            ToastFactory.makeText(StartActivity.this, "第一次启动", Toast.LENGTH_SHORT).show();

            LocalInfo localInfo = new LocalInfo();
            fillLocalInfo(localInfo);
        }

        // 用于调试信息查看，查看SHA值
        Log.d("SHA1:", sHA1(this));

        // 等待2s后自动进入到HomePageActivity界面
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 2000ms
                Intent intent = new Intent(StartActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

    // 查看本应用程序的值（该值是用于第一次获取有关申请高德地图相关服务的key值，具体信息可参考lbs.amap.com/api/
    // 目前该已应用已获取到该key值，位于AndroidManifest.xml 里，如下：
    // <meta-data
    // android:name="com.amap.api.v2.apikey"
    // android:value="d47a4f292183bbe8a69fff44e38dbfee" />
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 设置本机本地信息
    private void fillLocalInfo(LocalInfo localInfo) {
        localInfo.setMachine_id(1);
        localInfo.setIp("10.28.34.5");
        localInfo.setMac_address("64-00-6A-76-46-60");
        localInfo.setServer_ip("127.0.0.1");
        localInfo.setLanguage("Chinese");
        localInfo.setLocal_address("未知");
        localInfo.setDefault_password("123");
        localInfo.setLogin_password("123");
        localInfo.setVersion(1);
        localInfo.setTel_number("0750-1234567");
        localInfo.setAd_rules("a1b1c1d1e1");
        localInfo.save();
    }
}
