package cn.edu.stu.max.cocovendor.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.stu.max.cocovendor.adapters.MyInternalListAdapter;
import cn.edu.stu.max.cocovendor.javaClass.FileService;
import cn.edu.stu.max.cocovendor.javaClass.ToastFactory;
import cn.edu.stu.max.cocovendor.R;
import cn.edu.stu.max.cocovendor.adapters.SalesSettingAdapter;
import cn.edu.stu.max.cocovendor.databaseClass.Goods;

public class SalesSettingActivity extends AppCompatActivity implements SalesSettingAdapter.Callback{

    private final static String FROMPATH = "/mnt/usb_storage/USB_DISK2/udisk0/Goods/";   // U盘货物存储路径
    private final static String FROMPATH_1 = "/mnt/usb_storage/USB_DISK2/udisk0/CocoVendorInit/";   // U盘初始信息存储路径
    private final static String TOPATH = "/mnt/internal_sd/CocoVendor/Goods/";
    private final static String TOPATH_1 = "/mnt/internal_sd/CocoVendorInit/";

    private RecyclerView recyclerViewSalesSetting;
    private SalesSettingAdapter salesSettingAdapter;
    private List<Goods> list = new ArrayList<>();
    //控制有多少个货柜道
    private static int CABINET_SIZE = 24;
    private static float PRICE_STEP = 0.5f;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_setting);
        //加入返回箭头
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences("cabinet_floor", MODE_PRIVATE);
        for (int i = 0; i < CABINET_SIZE; i ++) {
            int whichGoods =  preferences.getInt("cabinet_floor_" + i, 0);
            if (whichGoods == 0) {
                list.add(new Goods());
            } else {
                list.add(DataSupport.find(Goods.class, whichGoods));
            }
        }
        //找到UI控件
        RecyclerView recyclerViewSalesSetting = (RecyclerView) findViewById(R.id.rv_sales_setting);
        //设置线性布局 Creates a vertical LinearLayoutManager
        recyclerViewSalesSetting.setLayoutManager(new LinearLayoutManager(this));
        //设置recyclerView每个item间的分割线
        recyclerViewSalesSetting.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //创建recyclerView的实例，并将数据传输到适配器
        salesSettingAdapter = new SalesSettingAdapter(list, SalesSettingActivity.this, this);
        //recyclerView显示适配器内容
        recyclerViewSalesSetting.setAdapter(salesSettingAdapter);
        //找到按钮UI控件并设置添加按钮监听事件

        final Button buttonSalesSettingUDiskImport = (Button) findViewById(R.id.btn_sales_setting_udisk_import);
        buttonSalesSettingUDiskImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (USBExists()) {
                    FileService.copyFolder(FROMPATH, TOPATH);
                    FileService.copyFolder(FROMPATH_1, TOPATH_1);
//                    File[] UDiskFiles = FileService.getFiles(FROMPATH);
//                    for (File file : UDiskFiles) {
//                        if (file != null)
//                            FileService.copyFile(file.getPath(), TOPATH + file.getName());
//                    }

                    File[] internalFiles = FileService.getFiles(TOPATH);
                    for (File file : internalFiles) {
                        Goods goods = new Goods();
                        goods.setImage_path_s(file.getAbsolutePath());
                        goods.setName(getGoodsName(file.getName()));
                        goods.setSales_price(getGoodsPrice(file.getName()));
                        goods.save();
                    }
                    ToastFactory.makeText(SalesSettingActivity.this, "U盘文件已经成功导入，请点击上述按钮进行货物更换！", Toast.LENGTH_LONG).show();
                    // buttonSalesSettingUDiskImport.setClickable(false);
                } else {
                    ToastFactory.makeText(SalesSettingActivity.this, "请插入U盘!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        final Button buttonSalesSettingLoadLocalGoods = (Button) findViewById(R.id.btn_sales_setting_init);
//        buttonSalesSettingLoadLocalGoods.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                File[] internalFiles = FileService.getFiles(TOPATH);
//                for (File file : internalFiles) {
//                    Goods goods = new Goods();
//                    goods.setImage_path_s(file.getAbsolutePath());
//                    goods.setName(getGoodsName(file.getName()));
//                    goods.setSales_price(getGoodsPrice(file.getName()));
//                    goods.save();
//                }
//                ToastFactory.makeText(SalesSettingActivity.this, "本地文件已经成功导入", Toast.LENGTH_SHORT).show();
//            }
//        });
        Button buttonSalesSettingFull = (Button) findViewById(R.id.btn_sales_setting_full);
        buttonSalesSettingFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Goods i : list) {
                    if (i.getName() != null) {
                        i.setNum(5);
                        i.save();
                    }
                }
                salesSettingAdapter.notifyDataSetChanged();
            }
        });
        //下架所有商品
        Button buttonSalesSettingClear = (Button) findViewById(R.id.btn_sales_setting_clear);
        buttonSalesSettingClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SalesSettingActivity.this);
                builder.setMessage("确认下架全部商品吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        list.clear();
                        SharedPreferences.Editor editor = preferences.edit();
                        for (int i = 0; i < CABINET_SIZE; i ++) {
                            int whichGoods =  preferences.getInt("cabinet_floor_" + i, 0);
                            Goods goods = DataSupport.find(Goods.class, whichGoods);
                            if (goods != null) {
                                String GoodsOnSaleLocal = goods.getOnSaleLocal();
                                GoodsOnSaleLocal = GoodsOnSaleLocal.replace("-" + i + ":", "");
                                goods.setOnSaleLocal(GoodsOnSaleLocal);
                                if (GoodsOnSaleLocal != null) {
                                    goods.setOnSale(false);
                                }
                                goods.save();
                            }
                            editor.putInt("cabinet_floor_" + i, 0);
                            editor.apply();
                            list.add(new Goods());
                        }
                        salesSettingAdapter.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
            }
        });
        Button buttonSalesSettingReturn = (Button) findViewById(R.id.btn_sales_setting_return);
        buttonSalesSettingReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        for (int i = 0; i < CABINET_SIZE; i ++) {
            int whichGoods =  preferences.getInt("cabinet_floor_" + i, 0);
            if (whichGoods == 0) {
                list.add(new Goods());
            } else {
                list.add(DataSupport.find(Goods.class, whichGoods));
            }
        }
        salesSettingAdapter.notifyDataSetChanged();
    }

    //实现返回功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void click(View v) {
        int postion = (Integer) v.getTag();
        Goods goods = list.get(postion);
        switch (v.getId()) {
            case R.id.btn_set_goods:
                Intent intent = new Intent(this, SheetGoodsActivity.class);
                intent.putExtra("cabinetNum", (Integer) v.getTag());
                intent.putExtra("isSelGoods", true);
                startActivity(intent);
                break;
            case R.id.btn_del_goods:
                SharedPreferences preferences = getSharedPreferences("cabinet_floor", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                //直接判断返回的goods变量是否为null会有问题，转为判断名字是否null
                if (goods.getName() != null) {
                    String GoodsOnSaleLocal = goods.getOnSaleLocal();
                    GoodsOnSaleLocal = GoodsOnSaleLocal.replace("-" + postion + ":", "");
                    goods.setOnSaleLocal(GoodsOnSaleLocal);
                    if (GoodsOnSaleLocal != null) {
                        goods.setOnSale(false);
                    }
                    goods.save();
                    list.remove(postion);
                    list.add(postion, new Goods());
                }
                editor.putInt("cabinet_floor_" + postion, 0);
                editor.apply();
                salesSettingAdapter.notifyItemChanged(postion);
                break;
            case R.id.btn_goods_price_add:
                if (goods.getName() != null) {
                    goods.setSales_price(goods.getSales_price() + PRICE_STEP);//PRICE_STEP开始设置为0.5
                    goods.save();
                    salesSettingAdapter.notifyItemChanged(postion);
                }
                break;
            case R.id.btn_goods_price_dec:
                if (goods.getName() != null) {
                    if(goods.getSales_price() > PRICE_STEP) {
                        goods.setSales_price(goods.getSales_price() - PRICE_STEP);
                    } else {
                        goods.setSales_price(0);
                    }
                    goods.save();
                    salesSettingAdapter.notifyItemChanged(postion);
                }
                break;
        }
    }

    private List<Goods> getGoodsList(String filePath) {
        File[] files = FileService.getFiles(filePath);
        list.clear();
        for (int i = 0; i < files.length; i ++) {
            Goods goods = new Goods();
            goods.setImage_path_s(files[i].getAbsolutePath());
            goods.setName(getGoodsName(files[i].getName()));
            goods.setSales_price(getGoodsPrice(files[i].getName()));
            goods.save();
            list.add(goods);
        }
        return list;
    }

    public String getGoodsName(String fileName) {
        return fileName.substring(0, fileName.indexOf("￥"));
    }

    public float getGoodsPrice(String fileName) {
        return Float.parseFloat(fileName.substring(fileName.indexOf("￥") + 1,fileName.lastIndexOf('.')));
    }

    private boolean USBExists() {
        File usbFile = new File(FROMPATH);
        if (usbFile.exists()) {
            return true;
        } else {
            return false;
        }
    }
}