package cn.edu.stu.max.cocovendor.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;

import cn.edu.stu.max.cocovendor.javaClass.ToastFactory;
import cn.edu.stu.max.cocovendor.R;
import cn.edu.stu.max.cocovendor.adapters.SalesSettingAdapter;
import cn.edu.stu.max.cocovendor.databaseClass.Goods;

public class SalesSettingActivity extends AppCompatActivity implements SalesSettingAdapter.Callback{

    private RecyclerView recyclerViewSalesSetting;
    private SalesSettingAdapter salesSettingAdapter;
    private List<Goods> list = new ArrayList<Goods>();
    //控制有多少个货柜道
    private static int CABINET_SIZE = 24;
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
        recyclerViewSalesSetting = (RecyclerView) findViewById(R.id.rv_sales_setting);
        //设置线性布局 Creates a vertical LinearLayoutManager
        recyclerViewSalesSetting.setLayoutManager(new LinearLayoutManager(this));
        //设置recyclerView每个item间的分割线
        recyclerViewSalesSetting.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //创建recyclerView的实例，并将数据传输到适配器
        salesSettingAdapter = new SalesSettingAdapter(list, SalesSettingActivity.this, this);
        //recyclerView显示适配器内容
        recyclerViewSalesSetting.setAdapter(salesSettingAdapter);
        //找到按钮UI控件并设置添加按钮监听事件
        final Button buttonSalesSettingInit = (Button) findViewById(R.id.btn_sales_setting_init);
        buttonSalesSettingInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LayoutInflater inflater = getLayoutInflater();
//                final View view = inflater.inflate(R.layout.add_goods_dialog, (ViewGroup) findViewById(R.id.ll_add_goods));
//                final int[] index = new int[1];
//                final float[] goodsPrice = new float[1];
//                AlertDialog.Builder builder = new AlertDialog.Builder(SalesSettingActivity.this);
//                builder.setTitle(R.string.label_add_goods)
//                        .setSingleChoiceItems(R.array.goods_array, -1, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                AlertDialog.Builder builderIn = new AlertDialog.Builder(SalesSettingActivity.this);
//                                if (view.getParent() != null) {
//                                    ((ViewGroup) view.getParent()).removeView(view);
//                                }
//                                builderIn.setView(view);
//                                builderIn.setTitle("价格设置")
//                                        .setPositiveButton(R.string.label_save, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                // FIRE ZE MISSILES!
//                                                EditText editTextPrice = (EditText) view.findViewById(R.id.ed_goods_price);
//                                                try {
//                                                    goodsPrice[0] = Float.valueOf(editTextPrice.getText().toString().trim());
//                                                } catch (NumberFormatException e) {
//                                                    ToastFactory.makeText(SalesSettingActivity.this, "没有输入价钱", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        })
//                                        .setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                // User cancelled the dialog
//                                            }
//                                        });
//                                builderIn.setCancelable(false);
//                                TextView textView = (TextView) view.findViewById(R.id.label_goods_name);
//                                textView.setText(getResources().getStringArray(R.array.goods_array)[which]);
//                                index[0] = which;
//                                builderIn.create().show();
//                            }
//                        })
//                        .setPositiveButton(R.string.label_save, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                // FIRE ZE MISSILES!
//                                try {
//                                    Goods goods = new Goods();
//                                    goods.setName(getResources().getStringArray(R.array.goods_array)[index[0]]);
//                                    goods.setImage_path(getResources().getIdentifier("ic_category_" + index[0], "drawable", getPackageName()));
//                                    ToastFactory.makeText(SalesSettingActivity.this, String.valueOf(getResources().getIdentifier("ic_category_" + 0, "drawable", getPackageName())), Toast.LENGTH_SHORT).show();
//                                    goods.setSales_price(goodsPrice[0]);
//                                    goods.save();
//                                } catch (IllegalStateException e) {
//                                    ToastFactory.makeText(SalesSettingActivity.this, "已经存在这种商品了", Toast.LENGTH_SHORT).show();
//                                }
//                                DataSupport.findAllAsync(Goods.class).listen(new FindMultiCallback() {
//                                    @Override
//                                    public <T> void onFinish(List<T> t) {
//                                        List<Goods> allGoods = (List<Goods>) t;
//                                        salesSettingAdapter.notifyDataSetChanged();
//                                        //recyclerView显示适配器内容
//                                        recyclerViewSalesSetting.setAdapter(salesSettingAdapter);
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                // User cancelled the dialog
//                            }
//                        });
//                // Create the AlertDialog object and return it
//                builder.setCancelable(false);
//                builder.create().show();
                for (int i = 0; i < 34; i ++) {
                    Goods goods = new Goods();
                    goods.setName(getResources().getStringArray(R.array.goods_name_array)[i]);
                    goods.setCost_price((float) getResources().getIntArray(R.array.goods_price_array)[i] / 10.0f);
                    goods.setSales_price(goods.getCost_price());
                    goods.setImage_path(getResources().getIdentifier("ic_category_" + i, "drawable", getPackageName()));
                    goods.setNum(5);
                    goods.setOnSale(false);
                    goods.save();
                }
                buttonSalesSettingInit.setVisibility(View.INVISIBLE);
            }
        });
        Button buttonSalesSettingFull = (Button) findViewById(R.id.btn_sales_setting_full);
        buttonSalesSettingFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Goods> toFullGoods = salesSettingAdapter.getList();
                for (Goods i : toFullGoods) {
                    i.setNum(5);
                    i.save();
                }
                salesSettingAdapter.notifyDataSetChanged();
            }
        });
        //下架所有商品
        Button buttonSalesSettingClear = (Button) findViewById(R.id.btn_sales_setting_clear);
        buttonSalesSettingClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                int postion = (Integer) v.getTag();
                Goods goods = salesSettingAdapter.getList().get(postion);
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
        }
    }
}
