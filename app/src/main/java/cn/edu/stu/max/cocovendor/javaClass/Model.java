package cn.edu.stu.max.cocovendor.javaClass;

/**
 * Created by 0 on 2017/11/5.
 */

public class Model {
    private String price;
    private String name;
    private int iconRes;
    private boolean isSaleOut;

    public Model(String price, String name, int iconRes, boolean isSaleOut) {
        this.price = price;
        this.name = name;
        this.iconRes = iconRes;
        this.isSaleOut = isSaleOut;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String name) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public boolean isSaleOut() {
        return isSaleOut;
    }

    public void setSaleOut(boolean saleOut) {
        isSaleOut = saleOut;
    }
}