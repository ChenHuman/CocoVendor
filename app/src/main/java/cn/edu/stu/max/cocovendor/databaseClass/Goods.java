package cn.edu.stu.max.cocovendor.databaseClass;

import android.graphics.Bitmap;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.List;

public class Goods extends DataSupport{
    //商品id
    private int id;
    //商品名称
    @Column(unique = true)
    private String name;
    //商品销售数量
    private int sellingNum;
    //商品成本价
    private float cost_price;
    //商品销售价
    private float sales_price;
    //现金支付笔数
    private int cashTimes;
    //支付宝支付笔数
    private int alipayTimes;
    //微信支付笔数
    private int wechatTimes;
    //销售总额
    private float totalSales;
    //商品图片存放路径
    private int image_path;
    // 商品图片存放路径
    private String image_path_s;

    public String getImage_path_s() {
        return image_path_s;
    }

    public void setImage_path_s(String image_path_s) {
        this.image_path_s = image_path_s;
    }

    // 商品图片
    private Bitmap image;
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
    //商品库存数量
    private int num;
    //销售记录id
    private List<Sales> sales_id;
    //是否在售
    @Column(defaultValue = "false")
    private boolean isOnSale;
    //在售位置
    private String onSaleLocal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSellingNum() {
        return sellingNum;
    }

    public void setSellingNum(int selling_num) {
        this.sellingNum = selling_num;
    }

    public float getCost_price() {
        return cost_price;
    }

    public void setCost_price(float cost_price) {
        this.cost_price = cost_price;
    }

    public float getSales_price() {
        return sales_price;
    }

    public void setSales_price(float sales_price) {
        this.sales_price = sales_price;
    }

    public int getCashTimes() {
        return cashTimes;
    }

    public void setCashTimes(int cashTimes) {
        this.cashTimes = cashTimes;
    }

    public int getAlipayTimes() {
        return alipayTimes;
    }

    public void setAlipayTimes(int alipayTimes) {
        this.alipayTimes = alipayTimes;
    }

    public int getWechatTimes() {
        return wechatTimes;
    }

    public void setWechatTimes(int wechatTimes) {
        this.wechatTimes = wechatTimes;
    }

    public float getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(float totalSales) {
        this.totalSales = totalSales;
    }

    public int getImage_path() {
        return image_path;
    }

    public void setImage_path(int image_path) {
        this.image_path = image_path;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Sales> getSales_id() {
        return DataSupport.where("goods_id = ?", String.valueOf(id)).find(Sales.class);
    }

    public void setSales_id(List<Sales> sales_id) {
        this.sales_id = sales_id;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }

    public String getOnSaleLocal() {
        return onSaleLocal;
    }

    public void setOnSaleLocal(String onSaleLocal) {
        this.onSaleLocal = onSaleLocal;
    }
}
