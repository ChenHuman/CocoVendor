package cn.edu.stu.max.cocovendor.databaseClass;

import android.provider.ContactsContract;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 0 on 2017/11/21.
 */

public class SingleProductSalesAnalyze extends DataSupport {

    // 关联类
    private List<SingleProductDailySales> singleProductDailySales = new ArrayList<SingleProductDailySales>();
    // 商品名称
    private String goodsName;
    // 商品图片存放路径
    private String imagePath;

    public List<SingleProductDailySales> getSingleProductDailySales() {
        return singleProductDailySales;
    }

    public void setSingleProductDailySales(List<SingleProductDailySales> singleProductDailySales) {
        this.singleProductDailySales = singleProductDailySales;
    }

    public String getGoodsName() {return goodsName;}
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImagePath() {return imagePath;}
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
