package cn.edu.stu.max.cocovendor.databaseClass;

import org.litepal.crud.DataSupport;

/**
 * Created by 0 on 2017/11/20.
 */

public class SingleProductSalesPandect extends DataSupport {

    // 商品名称
    private String goodsName;
    // 商品销量
    private int goodsSalesNum;
    // 商品成本价
    private float goodsCostPrice;
    // 商品售价
    private float goodsSalesPrice;
    // 现金支付次数
    private int cashTimes;
    // 微信支付次数
    private int wechatTimes;
    // 支付宝支付次数
    private int alipayTimes;
    // 销售总额
    private float salesAll;
    //商品图片存放路径
    private String imagePath;

    private int total_num = 0;
    private int total_cash = 0;
    private int total_wechat = 0;
    private int total_alipay = 0;
    private int total_sum = 0;

    public String getGoodsName() {return goodsName;}
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsSalesNum() {return goodsSalesNum;}
    public void setGoodsSalesNum(int goodsSalesNum) {
        this.goodsSalesNum = goodsSalesNum;
    }

    public float getGoodsCostPrice() {return goodsCostPrice;}
    public void setGoodsCostPrice(float goodsCostPrice) {
        this.goodsCostPrice = goodsCostPrice;
    }

    public float getGoodsSalesPrice() {return goodsSalesPrice;}
    public void setGoodsSalesPrice(float goodsSalesPrice) {
        this.goodsSalesPrice = goodsSalesPrice;
    }

    public int getCashTimes() {return cashTimes;}
    public void setCashTimes(int cashTimes) {
        this.cashTimes = cashTimes;
    }

    public int getWechatTimes() {return wechatTimes;}
    public void setWechatTimes(int wechatTimes) {
        this.wechatTimes = wechatTimes;
    }

    public int getAlipayTimes() {return alipayTimes;}
    public void setAlipayTimes(int alipayTimes) {
        this.alipayTimes = alipayTimes;
    }

    public float getSalesAll() {return salesAll;}
    public void setSalesAll(float salesAll) {
        this.salesAll = salesAll;
    }

    public String getImagePath() {return imagePath;}
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getTotal_num() {return total_num;}
    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public int getTotal_cash() {return total_cash;}
    public void setTotal_cash(int total_cash) {
        this.total_cash = total_cash;
    }

    public int getTotal_wechat() {return total_wechat;}
    public void setTotal_wechat(int total_wechat) {
        this.total_wechat = total_wechat;
    }

    public int getTotal_alipay() {return total_alipay;}
    public void setTotal_alipay(int total_alipay) {
        this.total_alipay = total_alipay;
    }

    public int getTotal_sum() {return total_sum;}
    public void setTotal_sum(int total_sum) {
        this.total_sum = total_sum;
    }
}
