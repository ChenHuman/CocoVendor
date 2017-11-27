package cn.edu.stu.max.cocovendor.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.io.File;
import java.util.List;

import cn.edu.stu.max.cocovendor.databaseClass.CabinetDailySales;
import cn.edu.stu.max.cocovendor.databaseClass.CabinetMonthlySales;
import cn.edu.stu.max.cocovendor.databaseClass.LocalInfo;
import cn.edu.stu.max.cocovendor.databaseClass.SingleProductDailySales;
import cn.edu.stu.max.cocovendor.databaseClass.SingleProductSalesAnalyze;
import cn.edu.stu.max.cocovendor.databaseClass.SingleProductSalesPandect;
import cn.edu.stu.max.cocovendor.javaClass.FileService;
import cn.edu.stu.max.cocovendor.adapters.GridViewAdapter;
import cn.edu.stu.max.cocovendor.javaClass.Model;
import cn.edu.stu.max.cocovendor.javaClass.ToastFactory;
import cn.edu.stu.max.cocovendor.R;
import cn.edu.stu.max.cocovendor.databaseClass.Goods;
import cn.edu.stu.max.cocovendor.databaseClass.Sales;
import cn.edu.stu.max.cocovendor.javaClass.ViewHolder;
import cn.edu.stu.max.cocovendor.adapters.ViewPagerAdapter;

public class HomePageActivity extends SerialPortActivity {

    String text = "It is a test message!!";

    SharedPreferences cabinetDailySalesSharedPreference;
    SharedPreferences.Editor editor;

    private static final int SECONDS_OF_AD = 60;    // 60秒无操作后自动进入全屏广告播放模式

    private final static String TOPATH = "/storage/sdcard0/tencent/QQfile_recv/b/";               // 本机广告存储路径

    private static final int REQUEST_CODE_1 = 1;
    private static final int REQUEST_PAY_RESULT_CODE = 2;

    private VideoView videoViewHomePageAd;
    private ImageView imageViewHomePageAd;

    private static int videoFileIndex = 0;
    private static int[] videoListOrder;
    private static int[] videoListFrequency;

    private SharedPreferences share;

    private static final String adSettingDataFileName = "sharedfile";     // 定义保存的文件的名称

    private Context context;

    Handler handler = new Handler();

    private TextView textViewCoinMoney;

    private ViewPager mPager;
    private List<View> mPagerList;
    private List<Model> mDatas;
    private LinearLayout mLlDot;
    private LayoutInflater inflater;
    /**
     * 总的页数
     */
    private int pageCount;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 12;
    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;
    private ViewPagerAdapter viewPagerAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_home_page);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mLlDot = (LinearLayout) findViewById(R.id.ll_dot);

//        mPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN: { // 手指下来的时候,取消之前绑定的Runnable
//
//                        handler.removeCallbacks(runnable);
//
//                        break;
//                    }
//                    case MotionEvent.ACTION_UP: { // 手指离开屏幕，发送延迟消息 ，5秒后执行
//
//                        handler.postDelayed(runnable, 1000 * SECONDS_OF_AD);
//
//                        break;
//                    }
//                }
//                return false;
//            }
//        });

        //初始化数据源,函数后面得知是24
        initDatas();
        inflater = LayoutInflater.from(this);
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        try {
            for (int i = 0; i < pageCount; i++) {
                //每个页面都是inflate出一个新实例
                GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
                gridView.setAdapter(new GridViewAdapter(this, mDatas, i, pageSize));
                mPagerList.add(gridView);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int pos = position + curIndex * pageSize;
                        ToastFactory.makeText(HomePageActivity.this, mDatas.get(pos).getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomePageActivity.this, PayActivity.class);
                        intent.putExtra("which_floor", pos);
                        startActivityForResult(intent, 2);
                    }
                });
            }
            viewPagerAdapter = new ViewPagerAdapter(mPagerList);
            //设置适配器
            mPager.setAdapter(viewPagerAdapter);
            //设置圆点
            setOvalLayout();
        } catch (NullPointerException e) {
            ToastFactory.makeText(HomePageActivity.this, "当前没有商品", Toast.LENGTH_SHORT).show();
        }

        Button buttonPageLeft = (Button) findViewById(R.id.btn_page_left);
        Button buttonPageRight = (Button) findViewById(R.id.btn_page_right);
        buttonPageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curIndex > 0) {
                    // 取消圆点选中
                    mLlDot.getChildAt(curIndex)
                            .findViewById(R.id.v_dot)
                            .setBackgroundResource(R.drawable.dot_normal);
                    mPager.setCurrentItem(--curIndex);
                    // 圆点选中
                    mLlDot.getChildAt(curIndex)
                            .findViewById(R.id.v_dot)
                            .setBackgroundResource(R.drawable.dot_selected);
                }
            }
        });
        buttonPageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pageCount数量是从1开始，判断时需要减1
                if (curIndex < pageCount - 1) {
                    // 取消圆点选中
                    mLlDot.getChildAt(curIndex)
                            .findViewById(R.id.v_dot)
                            .setBackgroundResource(R.drawable.dot_normal);
                    mPager.setCurrentItem(++curIndex);
                    // 圆点选中
                    mLlDot.getChildAt(curIndex)
                            .findViewById(R.id.v_dot)
                            .setBackgroundResource(R.drawable.dot_selected);
                }
            }
        });

        cabinetDailySalesSharedPreference = getSharedPreferences("cabinet_daily_sales_file", MODE_PRIVATE);
        editor = cabinetDailySalesSharedPreference.edit();   // 使处于可编辑状态

        final String packageName = getPackageName();
        SharedPreferences settings = getSharedPreferences(packageName + "_preferences", MODE_PRIVATE);
        boolean sw = settings.getBoolean("SwitchPreference", false);
        ViewHolder.sw = sw;

        TextView textViewMachineId = (TextView) findViewById(R.id.tv_machine_id);
        TextView textViewMachineVersion = (TextView) findViewById(R.id.tv_machine_version);
        TextView textViewIp = (TextView) findViewById(R.id.tv_ip);
        textViewMachineId.setText(String.valueOf(DataSupport.findFirst(LocalInfo.class).getMachine_id()));
        textViewMachineVersion.setText("v" + DataSupport.findFirst(LocalInfo.class).getVersion() + ".0");
        textViewIp.setText(DataSupport.findFirst(LocalInfo.class).getIp());

        //尝试定位操作
        AMapLocationClient mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(mLocationListener);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(false);
        mLocationOption.setInterval(10000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();

        context = this;

        textViewCoinMoney = (TextView) findViewById(R.id.coin_money);

        textViewCoinMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences share = getSharedPreferences("serial_mode_file", MODE_PRIVATE);  // 实例化
                SharedPreferences.Editor editor = share.edit();   // 使处于可编辑状态
                editor.putString("serial_mode", "A");
                editor.apply();
            }
        });

        ImageButton a = (ImageButton) findViewById(R.id.a);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences share = getSharedPreferences("serial_mode_file", MODE_PRIVATE);  // 实例化
                SharedPreferences.Editor editor = share.edit();   // 使处于可编辑状态
                editor.putString("serial_mode", "D");
                editor.apply();
            }
        });

        videoViewHomePageAd = (VideoView) findViewById(R.id.vv_hp_ad);
        imageViewHomePageAd = (ImageView) findViewById(R.id.iv_hp_ad);
        imageViewHomePageAd.setVisibility(View.INVISIBLE);

        share = super.getSharedPreferences(adSettingDataFileName, MODE_PRIVATE);
        if (share.getBoolean("isAdSettingChanged", false)) {
            ToastFactory.makeText(HomePageActivity.this, "good" + share.getString("Frequency_" + 0, "null"), Toast.LENGTH_LONG).show();
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

        TextView textViewHomepageTestlogin = (TextView) findViewById(R.id.tv_homepage_testlogin);
        textViewHomepageTestlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                intent.putExtra("IsLogin", true);
                startActivity(intent);
            }
        });

        SendThread sendThread = new SendThread();
        sendThread.start();
    }

    private class SendThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {

                SharedPreferences preferences = getSharedPreferences("serial_mode_file", MODE_PRIVATE);
                String serialMode =  preferences.getString("serial_mode", "A");

//                try {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    switch (serialMode) {
                        case "A":
                            text = "A";
                            SharedPreferences share = getSharedPreferences("cargo_lane_test_file", MODE_PRIVATE);  // 实例化
                            SharedPreferences.Editor editor = share.edit();   // 使处于可编辑状态
                            editor.putString("cargo_lane_status", "bad");
                            editor.apply();
                            break;
                        case "D":
                            text = "D";
                            SharedPreferences share1 = getSharedPreferences("cargo_lane_test_file", MODE_PRIVATE);  // 实例化
                            SharedPreferences.Editor editor1 = share1.edit();   // 使处于可编辑状态
                            editor1.putString("cargo_lane_status", "bad");
                            editor1.apply();
                            break;
                        case "C":
                            text = "C";
                            SharedPreferences share2 = getSharedPreferences("cargo_lane_test_file", MODE_PRIVATE);  // 实例化
                            SharedPreferences.Editor editor2 = share2.edit();   // 使处于可编辑状态
                            editor2.putString("cargo_lane_status", "good");
                            editor2.apply();
                            break;
                    }

//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                           ToastFactory.makeText(HomePageActivity.this, text, Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                    mOutputStream.write(text.getBytes());
//                    mOutputStream.write('\n');

//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    // 串口接收函数
    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                //ToastFactory.makeText(HomePageActivity.this, new String(buffer, 0, size), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //定位返回的信息
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    TextView textView = (TextView) findViewById(R.id.tv_homepage_testlogin);
                    String address = aMapLocation.getAddress();
                    textView.setText(address);
                    LocalInfo localInfo = DataSupport.findFirst(LocalInfo.class);
                    localInfo.setLocal_address(address);
                    localInfo.save();
                }
            }
        }
    };

    private void initVideoPath() {
        videoFileIndex = 0;
        try {
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
        } catch (NullPointerException e) {
            ToastFactory.makeText(HomePageActivity.this, "找不到文件路径", Toast.LENGTH_SHORT).show();
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
//        if (videoViewHomePageAd != null) {
//            videoViewHomePageAd.suspend();
//        }
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
                break;
            case REQUEST_PAY_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    int whichFloor = data.getIntExtra("which_floor", 0);
                    int whichGoods = data.getIntExtra("which_goods", 0);
                    String whichWay = data.getStringExtra("which_way");
                    String text = "You clicked on item" + whichGoods;
                    try {
                        mOutputStream.write(text.getBytes());
                        mOutputStream.write('\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (whichWay != null) {
                        try {
                            // 销售信息表记录
                            Goods goods = DataSupport.find(Goods.class, whichGoods);
                            Sales sales = new Sales();
                            sales.setSales_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                            sales.setGoods_id(goods.getId());
                            sales.setGoods_name(goods.getName());
                            sales.setMachine_floor(whichFloor + 1);//货物层下标0开始，需要加一
                            sales.setTrade_id("5846516");//还不知道用什么编号格式比较好
                            sales.setPay_way(whichWay);
                            sales.save();
                            if (goods.getNum() != 0) {
                                goods.setNum(goods.getNum() - 1);
                                goods.save();
                            }

                            // 设置机柜日销售统计信息表
                            setCabinetDailySalesAnalyzeSheet(whichGoods);

                            // 设置机柜月销售统计信息表
                            setCabinetMonthlySalesAnalyzeSheet(whichGoods);

                            // 设置单一商品销售总览信息表
                            setSingleProductSalesPandectSheet(sales, whichGoods);

                            // 设置单品销售统计信息表
                            setSingleProductSalesAnalyzeSheet(whichGoods);

                        } catch (NullPointerException e) {
                            ToastFactory.makeText(HomePageActivity.this, "目前没有商品" + whichGoods, Toast.LENGTH_SHORT).show();
                        }
                        viewPagerAdapter.notifyDataSetChanged();
                        Log.d("HomePage", "onActivityResult: I'm here!");
                    }
                }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        handler.postDelayed(runnable, 1000 * SECONDS_OF_AD);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

//        handler.removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
        // 用户SECONDS_OF_AD秒没操作了
//        Intent i = new Intent();
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setClass(context, ScreenSaverActivity.class);
//        context.startActivity(i);
        }
    };

    public boolean onTouchEvent(android.view.MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: { // 手指下来的时候,取消之前绑定的Runnable

                handler.removeCallbacks(runnable);
                break;
            }
            case MotionEvent.ACTION_UP: { // 手指离开屏幕，发送延迟消息 ，SECONDS_OF_AD秒后执行

                handler.postDelayed(runnable, 1000 * SECONDS_OF_AD);

                break;
            }
        }
        return super.onTouchEvent(event);
    };

    /**
     * 初始化数据源
     */
    private void initDatas() {
        mDatas = new ArrayList<>();
        //暂时初始化24个空货物的时候有两个点
        for (int i = 0; i < 24; i++) {
            SharedPreferences preferences = getSharedPreferences("cabinet_floor", MODE_PRIVATE);
            int whichGoods =  preferences.getInt("cabinet_floor_" + i, 0);
            Goods goods = DataSupport.find(Goods.class, whichGoods);
            if (whichGoods == 0) {
                //Model内参数分别为价格，名字，图片，是否显示售空
                mDatas.add(new Model("", "", R.drawable.ic_category_null, false));
            } else {
                mDatas.add(new Model("¥ " + String.valueOf(goods.getSales_price()), goods.getName(), goods.getImage_path(), goods.getNum() == 0));//最后一个参数，库存等于0为真时显示售空
            }
        }
    }

    /**
     * 设置圆点
     */
    public void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        mLlDot.getChildAt(0).findViewById(R.id.v_dot)
                .setBackgroundResource(R.drawable.dot_selected);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                mLlDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                mLlDot.getChildAt(position)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    // 设置机柜日销售统计信息表
    public void setCabinetDailySalesAnalyzeSheet(int whichGoods) {
        CabinetDailySales cabinetDailySales = new CabinetDailySales();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String cabinetDailySalesDate = sdf.format(new java.util.Date());
        String date =  cabinetDailySalesSharedPreference.getString(
                "cabinet_daily_sales_date", "2017-11-14");
        int num = cabinetDailySalesSharedPreference.getInt(
                "cabinet_daily_sales_num", 0);
        float totalMoney = cabinetDailySalesSharedPreference.getFloat("cabinet_daily_sales_total_money", 0);
        if (cabinetDailySalesDate.equals(date)) {
            num = num + 1;
            totalMoney = totalMoney + DataSupport.find(Goods.class, whichGoods).getSales_price();
            editor.putInt("cabinet_daily_sales_num", num);
            editor.putFloat("cabinet_daily_sales_total_money", totalMoney);
            editor.apply();
            cabinetDailySales.setCabinetDailySalesNum(num);
            cabinetDailySales.setCabinetDailySalesTotalMoney(totalMoney);
            cabinetDailySales.updateAll("cabinetDailySalesDate = ?", date);
        } else {
            date = cabinetDailySalesDate;
            num = 1;
            totalMoney = DataSupport.find(Goods.class, whichGoods).getSales_price();
            editor.putString("cabinet_daily_sales_date", date);
            editor.putInt("cabinet_daily_sales_num", num);
            editor.putFloat("cabinet_daily_sales_total_money", totalMoney);
            editor.apply();
            cabinetDailySales.setCabinetDailySalesDate(sdf);
            cabinetDailySales.setCabinetDailySalesNum(num);
            cabinetDailySales.setCabinetDailySalesTotalMoney(totalMoney);
            cabinetDailySales.save();
        }
    }

    // 设置机柜月销售统计信息表
    public void setCabinetMonthlySalesAnalyzeSheet(int whichGoods) {
        CabinetMonthlySales cabinetMonthlySales = new CabinetMonthlySales();
        SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM");
        String cabinetMonthlySalesDate = mSdf.format(new java.util.Date());
        String mDate =  cabinetDailySalesSharedPreference.getString(
                "cabinet_monthly_sales_date", "2017-10");
        int mNum = cabinetDailySalesSharedPreference.getInt(
                "cabinet_monthly_sales_num", 0);
        float mTotalMoney = cabinetDailySalesSharedPreference.getFloat(
                "cabinet_monthly_sales_total_money", 0);
        if (cabinetMonthlySalesDate.equals(mDate)) {
            mNum = mNum + 1;
            mTotalMoney = mTotalMoney + DataSupport.find(Goods.class, whichGoods).getSales_price();
            editor.putInt("cabinet_monthly_sales_num", mNum);
            editor.putFloat("cabinet_monthly_sales_total_money", mTotalMoney);
            editor.apply();
            cabinetMonthlySales.setCabinetMonthlySalesNum(mNum);
            cabinetMonthlySales.setCabinetMonthlySalesTotalMoney(mTotalMoney);
            cabinetMonthlySales.updateAll("cabinetMonthlySalesDate = ?", mDate);
        } else {
            mDate = cabinetMonthlySalesDate;
            mNum = 1;
            mTotalMoney = DataSupport.find(Goods.class, whichGoods).getSales_price();
            editor.putString("cabinet_monthly_sales_date", mDate);
            editor.putInt("cabinet_monthly_sales_num", mNum);
            editor.putFloat("cabinet_monthly_sales_total_money", mTotalMoney);
            editor.apply();
            cabinetMonthlySales.setCabinetMonthlySalesDate(mSdf);
            cabinetMonthlySales.setCabinetMonthlySalesNum(mNum);
            cabinetMonthlySales.setCabinetMonthlySalesTotalMoney(mTotalMoney);
            cabinetMonthlySales.save();
        }
    }

    // 设置单一商品销售总览信息表
    public void setSingleProductSalesPandectSheet(Sales sales, int whichGoods) {
        SingleProductSalesPandect singleProductSalesPandect = new SingleProductSalesPandect();
        String singleProductSalesPandectGoodsName = "";
        int singleProductSalesPandectGoodsSalesNum = 0;
        int singleProductSalesPandectGoodsSalesCashTimes = 0;
        int singleProductSalesPandectGoodsSalesWechatTimes = 0;
        int singleProductSalesPandectGoodsSalesAlipayTimes = 0;
        int singleProductSalesPandectGoodsImagePath = 0;
        float singleProductSalesPandectGoodsCostPrice = 0;
        float singleProductSalesPandectGoodsSalesPrice = 0;
        float singleProductSalesPandectGoodsSalesAll = 0;
        if (DataSupport.where("goodsName = ?", DataSupport.find(Goods.class, whichGoods).getName()).find(SingleProductSalesPandect.class) != null &&
                !DataSupport.where("goodsName = ?", DataSupport.find(Goods.class, whichGoods).getName()).find(SingleProductSalesPandect.class).isEmpty()) {
            singleProductSalesPandectGoodsSalesNum = DataSupport.where("goodsName = ?", DataSupport.find(Goods.class, whichGoods).getName())
                    .find(SingleProductSalesPandect.class).get(0).getGoodsSalesNum() + 1;
            switch (sales.getPay_way()) {
                case "现金": singleProductSalesPandectGoodsSalesCashTimes = DataSupport.where("goodsName = ?", DataSupport.find(Goods.class, whichGoods).getName())
                        .find(SingleProductSalesPandect.class).get(0).getCashTimes() + 1;
                    break;
                case "微信": singleProductSalesPandectGoodsSalesWechatTimes = DataSupport.where("goodsName = ?", DataSupport.find(Goods.class, whichGoods).getName())
                        .find(SingleProductSalesPandect.class).get(0).getWechatTimes() + 1;
                    break;
                case "支付宝": singleProductSalesPandectGoodsSalesAlipayTimes = DataSupport.where("goodsName = ?", DataSupport.find(Goods.class, whichGoods).getName())
                        .find(SingleProductSalesPandect.class).get(0).getAlipayTimes() + 1;
                    break;
            }
            singleProductSalesPandectGoodsImagePath = DataSupport.find(Goods.class, whichGoods).getImage_path();
            singleProductSalesPandectGoodsSalesAll = singleProductSalesPandectGoodsSalesNum * DataSupport.find(Goods.class, whichGoods).getSales_price();
            singleProductSalesPandectGoodsCostPrice = DataSupport.find(Goods.class, whichGoods).getCost_price();
            singleProductSalesPandectGoodsSalesPrice = DataSupport.find(Goods.class, whichGoods).getSales_price();
            singleProductSalesPandectGoodsName = DataSupport.find(Goods.class, whichGoods).getName();
            singleProductSalesPandect.setGoodsSalesNum(singleProductSalesPandectGoodsSalesNum);
            singleProductSalesPandect.setCashTimes(singleProductSalesPandectGoodsSalesCashTimes);
            singleProductSalesPandect.setWechatTimes(singleProductSalesPandectGoodsSalesWechatTimes);
            singleProductSalesPandect.setAlipayTimes(singleProductSalesPandectGoodsSalesAlipayTimes);
            singleProductSalesPandect.setSalesAll(singleProductSalesPandectGoodsSalesAll);
            singleProductSalesPandect.setGoodsCostPrice(singleProductSalesPandectGoodsCostPrice);
            singleProductSalesPandect.setGoodsSalesPrice(singleProductSalesPandectGoodsSalesPrice);
            singleProductSalesPandect.setImagePath(singleProductSalesPandectGoodsImagePath);
            singleProductSalesPandect.updateAll("goodsName = ?", singleProductSalesPandectGoodsName);
        } else {
            singleProductSalesPandectGoodsSalesNum = 1;
            switch (sales.getPay_way()) {
                case "现金": singleProductSalesPandectGoodsSalesCashTimes = 1;
                    break;
                case "微信": singleProductSalesPandectGoodsSalesWechatTimes = 1;
                    break;
                case "支付宝": singleProductSalesPandectGoodsSalesAlipayTimes = 1;
                    break;
            }
            singleProductSalesPandectGoodsImagePath = DataSupport.find(Goods.class, whichGoods).getImage_path();
            singleProductSalesPandectGoodsSalesAll = DataSupport.find(Goods.class, whichGoods).getSales_price();
            singleProductSalesPandectGoodsCostPrice = DataSupport.find(Goods.class, whichGoods).getCost_price();
            singleProductSalesPandectGoodsSalesPrice = DataSupport.find(Goods.class, whichGoods).getSales_price();
            singleProductSalesPandectGoodsName = DataSupport.find(Goods.class, whichGoods).getName();
            singleProductSalesPandect.setGoodsName(singleProductSalesPandectGoodsName);
            singleProductSalesPandect.setGoodsSalesNum(singleProductSalesPandectGoodsSalesNum);
            singleProductSalesPandect.setCashTimes(singleProductSalesPandectGoodsSalesCashTimes);
            singleProductSalesPandect.setWechatTimes(singleProductSalesPandectGoodsSalesWechatTimes);
            singleProductSalesPandect.setAlipayTimes(singleProductSalesPandectGoodsSalesAlipayTimes);
            singleProductSalesPandect.setSalesAll(singleProductSalesPandectGoodsSalesAll);
            singleProductSalesPandect.setGoodsCostPrice(singleProductSalesPandectGoodsCostPrice);
            singleProductSalesPandect.setGoodsSalesPrice(singleProductSalesPandectGoodsSalesPrice);
            singleProductSalesPandect.setImagePath(singleProductSalesPandectGoodsImagePath);
            singleProductSalesPandect.save();
        }
    }

    public void setSingleProductSalesAnalyzeSheet(int whichGoods) {
        SingleProductSalesAnalyze singleProductSalesAnalyze = new SingleProductSalesAnalyze();
        String singleProductSalesPandectGoodsName = "";
        int singleProductSalesPandectGoodsImagePath = 0;
        if (DataSupport.where("goodsName = ?", DataSupport.find(Goods.class, whichGoods).getName()).find(SingleProductSalesAnalyze.class) != null &&
                !DataSupport.where("goodsName = ?", DataSupport.find(Goods.class, whichGoods).getName()).find(SingleProductSalesAnalyze.class).isEmpty()) {
            singleProductSalesPandectGoodsImagePath = DataSupport.find(Goods.class, whichGoods).getImage_path();
            singleProductSalesPandectGoodsName = DataSupport.find(Goods.class, whichGoods).getName();
            singleProductSalesAnalyze.setImagePath(singleProductSalesPandectGoodsImagePath);
            singleProductSalesAnalyze.updateAll("goodsName = ?", singleProductSalesPandectGoodsName);
        } else {
            singleProductSalesPandectGoodsImagePath = DataSupport.find(Goods.class, whichGoods).getImage_path();
            singleProductSalesPandectGoodsName = DataSupport.find(Goods.class, whichGoods).getName();
            singleProductSalesAnalyze.setGoodsName(singleProductSalesPandectGoodsName);
            singleProductSalesAnalyze.setImagePath(singleProductSalesPandectGoodsImagePath);
            singleProductSalesAnalyze.save();
        }
    }
}
