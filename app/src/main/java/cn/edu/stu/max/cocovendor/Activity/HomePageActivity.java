package cn.edu.stu.max.cocovendor.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.litepal.crud.DataSupport;

import java.util.Date;

import java.io.File;

import cn.edu.stu.max.cocovendor.JavaClass.FileService;
import cn.edu.stu.max.cocovendor.R;
import cn.edu.stu.max.cocovendor.Service.VideoService;
import cn.edu.stu.max.cocovendor.databaseClass.LocalInfo;
import cn.edu.stu.max.cocovendor.databaseClass.Sales;

public class HomePageActivity extends AppCompatActivity {

    private final static String TOPATH = "/storage/sdcard0/tencent/QQfile_recv/b/";               // 本机广告存储路径

    private static final int REQUEST_CODE_1 = 1;

    private VideoView videoViewHomePageAd;
    private ImageView imageViewHomePageAd;

    private static int videoFileIndex = 0;
    private static int[] videoListOrder;
    private static int[] videoListFrequency;
    private static int tempFrequency = 0;

    private SharedPreferences share;

    private static final String adSettingDataFileName = "sharedfile";     // 定义保存的文件的名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        videoViewHomePageAd = (VideoView) findViewById(R.id.vv_hp_ad);
        imageViewHomePageAd = (ImageView) findViewById(R.id.iv_hp_ad);
        imageViewHomePageAd.setVisibility(View.INVISIBLE);

        share = super.getSharedPreferences(adSettingDataFileName, MODE_PRIVATE);
        if (share.getBoolean("isAdSettingChanged", false)) {
            Toast.makeText(HomePageActivity.this, "good" + share.getString("Frequency_" + 0, "null"), Toast.LENGTH_LONG).show();
        }

        initVideoPath();


        videoViewHomePageAd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                if (share.getBoolean("isAdSettingChanged", false)) {
                    if (videoListFrequency[videoFileIndex] > 1) {
                        videoListFrequency[videoFileIndex] = videoListFrequency[videoFileIndex] - 1;

                    } else {
                        videoListFrequency[videoFileIndex] = Integer.parseInt(share.getString("Frequency_" + videoFileIndex, "0"));
                        videoFileIndex ++;
                    }
                    File[] currentFiles = FileService.getFiles(TOPATH);

                    if (currentFiles.length != 0) {
                        if (videoFileIndex >= currentFiles.length) {
                            videoFileIndex = 0;
                        }
                        playVideo(currentFiles[videoListOrder[videoFileIndex]].getPath());
                    } else {
                        imageViewHomePageAd.setVisibility(View.VISIBLE);
                        videoViewHomePageAd.setVisibility(View.INVISIBLE);
                    }
                } else {
                    videoFileIndex ++;
                    File[] currentFiles = FileService.getFiles(TOPATH);

                    if (currentFiles.length != 0) {
                        if (videoFileIndex >= currentFiles.length) {
                            videoFileIndex = 0;
                        }
                        playVideo(currentFiles[videoFileIndex].getPath());
                    } else {
                        imageViewHomePageAd.setVisibility(View.VISIBLE);
                        videoViewHomePageAd.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });


//        name.setText(share.getString("name", "no"));
//        sex.setText(share.getString("sex", "ren"));
//        age.setText("hh" + share.getInt("age", 0));

//        Toast.makeText(HomePageActivity.this, "good" + share.getInt("age", 123), Toast.LENGTH_LONG).show();


        TextView textViewHomepageTestlogin = (TextView) findViewById(R.id.tv_homepage_testlogin);
        textViewHomepageTestlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                intent.putExtra("IsLogin", true);
                startActivity(intent);
            }
        });

        ImageView imageViewGoods1 = (ImageView) findViewById(R.id.iv_goods_1);
        imageViewGoods1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sales sales = new Sales();
                sales.setSales_date(new Date());
                sales.setMachine_floor(1);
                sales.save();
            }
        });
        ImageView imageViewGoods2 = (ImageView) findViewById(R.id.iv_goods_2);
        imageViewGoods2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sales sales = new Sales();
                sales.setSales_date(new Date());
                sales.setMachine_floor(2);
                sales.save();
            }
        });
        ImageView imageViewGoods3 = (ImageView) findViewById(R.id.iv_goods_3);
        imageViewGoods3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sales sales = new Sales();
                sales.setSales_date(new Date());
                sales.setMachine_floor(3);
                sales.save();
            }
        });
        ImageView imageViewGoods4 = (ImageView) findViewById(R.id.iv_goods_4);
        imageViewGoods4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sales sales = new Sales();
                sales.setSales_date(new Date());
                sales.setMachine_floor(4);
                sales.save();
            }
        });
        ImageView imageViewGoods5 = (ImageView) findViewById(R.id.iv_goods_5);
        imageViewGoods5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sales sales = new Sales();
                sales.setSales_date(new Date());
                sales.setMachine_floor(5);
                sales.save();
            }
        });
        ImageView imageViewGoods6 = (ImageView) findViewById(R.id.iv_goods_6);
        imageViewGoods6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sales sales = new Sales();
                sales.setSales_date(new Date());
                sales.setMachine_floor(6);
                sales.save();
            }
        });
    }

    //活动转换之间都调用沉浸模式全屏
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    //重写返回按键内容
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }


    private void initVideoPath() {
        videoFileIndex = 0;
        File[] currentFiles = FileService.getFiles(TOPATH);
        if (currentFiles.length != 0) {
            if (share.getBoolean("isAdSettingChanged", false)) {
                videoListOrder = new int[currentFiles.length];
                videoListFrequency = new int[currentFiles.length];
                for (int i = 0; i < currentFiles.length; i++) {
                    videoListFrequency[i] = Integer.parseInt(share.getString("Frequency_" + i, "0"));
                    for (int j = 0; j < currentFiles.length; j++) {
                        if (share.getString("Ad_" + i, "null").equals(currentFiles[j].getName())) {
                            videoListOrder[i] = j;
                            break;
                        }
                    }
                }
                playVideo(currentFiles[videoListOrder[videoFileIndex]].getPath());
            }
            else {
                playVideo(currentFiles[videoFileIndex].getPath());
            }
        } else {
            imageViewHomePageAd.setVisibility(View.VISIBLE);
            videoViewHomePageAd.setVisibility(View.INVISIBLE);
        }

    }

    public void playVideo(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            videoViewHomePageAd.setVideoPath(file.getPath());
            videoViewHomePageAd.start();
        } else {
            imageViewHomePageAd.setVisibility(View.VISIBLE);
            videoViewHomePageAd.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoViewHomePageAd != null) {
            videoViewHomePageAd.suspend();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_1:
                if (resultCode == RESULT_OK) {
                    videoFileIndex = 0;
                    File[] currentFiles = FileService.getFiles(TOPATH);
                    if (currentFiles.length != 0) {
                        //Toast.makeText(HomePageActivity.this, "good", Toast.LENGTH_LONG).show();
                        imageViewHomePageAd.setVisibility(View.INVISIBLE);
                        videoViewHomePageAd.setVisibility(View.VISIBLE);
                        //videoViewHomePageAd.start();
                        if (share.getBoolean("isAdSettingChanged", false)) {
                            videoListFrequency = new int[currentFiles.length];
                            videoListOrder = new int[currentFiles.length];
                            for (int i = 0; i < currentFiles.length; i++) {
                                videoListFrequency[i] = Integer.parseInt(share.getString("Frequency_" + i, "0"));
                                for (int j = 0; j < currentFiles.length; j++) {
                                    if (share.getString("Ad_" + i, "null").equals(currentFiles[j].getName())) {
                                        videoListOrder[i] = j;
                                        break;
                                    }
                                }
                            }
                            playVideo(currentFiles[videoListOrder[videoFileIndex]].getPath());
                        } else {
                            playVideo(currentFiles[videoFileIndex].getPath());
                        }
                    } else {
                        imageViewHomePageAd.setVisibility(View.VISIBLE);
                        videoViewHomePageAd.setVisibility(View.INVISIBLE);
                    }
                }
        }
    }
}
