package cn.edu.stu.max.cocovendor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.edu.stu.max.cocovendor.adapters.SalesSettingAdapter;
import cn.edu.stu.max.cocovendor.R;
import cn.edu.stu.max.cocovendor.adapters.SheetGoodsAdapter;
import cn.edu.stu.max.cocovendor.databaseClass.Goods;
import cn.edu.stu.max.cocovendor.javaClass.FileService;
import cn.edu.stu.max.cocovendor.javaClass.ToastFactory;

public class SheetGoodsActivity extends AppCompatActivity {

    private SheetGoodsAdapter sheetGoodsAdapter;
    private int fromWhich;
    private boolean isSelGoods;
    private TextView tv_sheetRow1_sel;
    private ImageView iv_sheetRow2_sel;
    private TextView tv_sheetRow2_sel;
    private TextView tv_sheetRow3_sel;
    private TextView tv_sheetRow4_sel;
    private TextView tv_sheetRow5_sel;
    private TextView tv_sheetRow6_sel;
    private int preGoodsIndex;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_goods);
        //加入返回箭头
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayout linearLayoutGoodsSel = (LinearLayout) findViewById(R.id.ll_goods_sel);
        Intent intent = getIntent();
        fromWhich = intent.getIntExtra("cabinetNum", 0);//下标从0开始
        isSelGoods = intent.getBooleanExtra("isSelGoods", false);
        SharedPreferences preferences = getSharedPreferences("cabinet_floor", MODE_PRIVATE);
        preGoodsIndex =  preferences.getInt("cabinet_floor_" + fromWhich, 0);
        editor = preferences.edit();
        linearLayoutGoodsSel.setVisibility(isSelGoods ? View.VISIBLE : View.GONE);
        List<Goods> allGoods = DataSupport.findAll(Goods.class);
        if (isSelGoods) {
            tv_sheetRow1_sel = (TextView) findViewById(R.id.tv_sheetRow1_sel);
            iv_sheetRow2_sel = (ImageView) findViewById(R.id.iv_sheetRow2_sel);
            tv_sheetRow2_sel = (TextView) findViewById(R.id.tv_sheetRow2_sel);
            tv_sheetRow3_sel = (TextView) findViewById(R.id.tv_sheetRow3_sel);
            tv_sheetRow4_sel = (TextView) findViewById(R.id.tv_sheetRow4_sel);
            tv_sheetRow5_sel = (TextView) findViewById(R.id.tv_sheetRow5_sel);
            tv_sheetRow6_sel = (TextView) findViewById(R.id.tv_sheetRow6_sel);
            Goods initGoods = DataSupport.find(Goods.class, preGoodsIndex);
            if (initGoods != null) {
                tv_sheetRow1_sel.setText(String.valueOf(initGoods.getId()));
                // iv_sheetRow2_sel.setImageResource(initGoods.getImage_path());
                iv_sheetRow2_sel.setImageBitmap(FileService.getUDiskBitmap(initGoods.getImage_path_s()));
                tv_sheetRow2_sel.setText(initGoods.getName());
                tv_sheetRow3_sel.setText(String.valueOf(initGoods.getSales_price()));
                tv_sheetRow4_sel.setText(String.valueOf(initGoods.getNum()));
                if (!initGoods.isOnSale()) {
                    tv_sheetRow5_sel.setText("未上架");
                    tv_sheetRow5_sel.setTextColor(Color.RED);
                } else {
                    tv_sheetRow5_sel.setText("在售中");
                    tv_sheetRow5_sel.setTextColor(Color.GREEN);
                }
                tv_sheetRow6_sel.setText(initGoods.getOnSaleLocal());
            }
        }
        RecyclerView recyclerViewSalesSetting = (RecyclerView) findViewById(R.id.rv_sales_setting);
        //设置线性布局 Creates a vertical LinearLayoutManager
        recyclerViewSalesSetting.setLayoutManager(new LinearLayoutManager(this));
        //设置recyclerView每个item间的分割线
        recyclerViewSalesSetting.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //创建recyclerView的实例，并将数据传输到适配器
        sheetGoodsAdapter = new SheetGoodsAdapter(allGoods, SheetGoodsActivity.this);
        sheetGoodsAdapter.setOnItemClickListener(new SheetGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                ToastFactory.makeText(SheetGoodsActivity.this, "点击了item" + position, Toast.LENGTH_SHORT).show();
                if (isSelGoods) {
                    Goods goods = sheetGoodsAdapter.getItem(position);
                    tv_sheetRow1_sel.setText(String.valueOf(goods.getId()));
                    // iv_sheetRow2_sel.setImageResource(goods.getImage_path());
                    iv_sheetRow2_sel.setImageBitmap(FileService.getUDiskBitmap(goods.getImage_path_s()));
                    tv_sheetRow2_sel.setText(goods.getName());
                    tv_sheetRow3_sel.setText(String.valueOf(goods.getSales_price()));
                    tv_sheetRow4_sel.setText(String.valueOf(goods.getNum()));
                    if (!goods.isOnSale()) {
                        tv_sheetRow5_sel.setText("未上架");
                        tv_sheetRow5_sel.setTextColor(Color.RED);
                    } else {
                        tv_sheetRow5_sel.setText("在售中");
                        tv_sheetRow5_sel.setTextColor(Color.GREEN);
                    }
                    tv_sheetRow6_sel.setText(goods.getOnSaleLocal());
                    //确定按钮功能
                    Button buttonSelGoodsOk = (Button) findViewById(R.id.btn_sel_goods_ok);
                    buttonSelGoodsOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Goods preGoods = null;
                            if (preGoodsIndex != 0) {
                                preGoods = sheetGoodsAdapter.getItem(preGoodsIndex - 1);//货物编号是从1开始，Item从0开始，所以减1
                                String preGoodsOnSaleLocal = preGoods.getOnSaleLocal();
                                if (preGoodsOnSaleLocal != null) {
                                    preGoodsOnSaleLocal = preGoodsOnSaleLocal.replace("-" + fromWhich + ":", "");
                                    preGoods.setOnSaleLocal(preGoodsOnSaleLocal);
                                }
                                if (preGoodsOnSaleLocal != null) {
                                    preGoods.setOnSale(false);
                                }
                                preGoods.save();
                            }

                            Goods toSelGoods = sheetGoodsAdapter.getItem(position);
                            String toSelGoodsOnSaleLocal = toSelGoods.getOnSaleLocal();
                            if (toSelGoodsOnSaleLocal == null) {
                                toSelGoodsOnSaleLocal = "";
                            }
                            toSelGoods.setOnSale(true);
                            if (preGoodsIndex == 0) {
                                toSelGoods.setOnSaleLocal(toSelGoodsOnSaleLocal + "-" + fromWhich + ":");
                            } else if (preGoods.getId() != toSelGoods.getId()) {
                                toSelGoods.setOnSaleLocal(toSelGoodsOnSaleLocal + "-" + fromWhich + ":");
                            }
                            toSelGoods.save();
                            editor.putInt("cabinet_floor_" + fromWhich, toSelGoods.getId());
                            editor.apply();
                            finish();
                        }
                    });
                }
            }
        });
        //recyclerView显示适配器内容
        recyclerViewSalesSetting.setAdapter(sheetGoodsAdapter);

        Button buttonSalesSettingReturn = (Button) findViewById(R.id.btn_sales_setting_return);
        buttonSalesSettingReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
}
