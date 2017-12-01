package cn.edu.stu.max.cocovendor.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.stu.max.cocovendor.R;
import cn.edu.stu.max.cocovendor.databaseClass.Goods;
import cn.edu.stu.max.cocovendor.databaseClass.SingleProductSalesPandect;
import cn.edu.stu.max.cocovendor.javaClass.FileService;

public class SingleProductSalesPandectAdapter extends RecyclerView.Adapter {
    private List<SingleProductSalesPandect> list;
    private Context context;

    public SingleProductSalesPandectAdapter(List<SingleProductSalesPandect> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_sales_pandect_item, parent, false);
        return new sheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        sheetViewHolder vh = (sheetViewHolder) holder;
        vh.getTv_sheetRow1().setText(String.valueOf(position + 1));
        if (list.get(position).getGoodsName() == null) vh.getIv_sheetRow2().setImageResource(R.color.colorTransparency);
        else vh.getIv_sheetRow2().setImageBitmap(FileService.getUDiskBitmap(list.get(position).getImagePath()));
        vh.getTv_sheetRow2().setText(list.get(position).getGoodsName());
        vh.getTv_sheetRow3().setText(String.valueOf(list.get(position).getGoodsSalesNum()));
        vh.getTv_sheetRow4().setText(String.valueOf(list.get(position).getGoodsCostPrice()));
        vh.getTv_sheetRow5().setText(String.valueOf(list.get(position).getGoodsSalesPrice()));
        vh.getTv_sheetRow6().setText(String.valueOf(list.get(position).getCashTimes()));
        vh.getTv_sheetRow7().setText(String.valueOf(list.get(position).getWechatTimes()));
        vh.getTv_sheetRow8().setText(String.valueOf(list.get(position).getAlipayTimes()));
        vh.getTv_sheetRow9().setText(String.valueOf(list.get(position).getSalesAll()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class sheetViewHolder extends RecyclerView.ViewHolder{
        private final View mView;
        private final TextView tv_sheetRow1;
        private final ImageView iv_sheetRow2;
        private final TextView tv_sheetRow2;
        private final TextView tv_sheetRow3;
        private final TextView tv_sheetRow4;
        private final TextView tv_sheetRow5;
        private final TextView tv_sheetRow6;
        private final TextView tv_sheetRow7;
        private final TextView tv_sheetRow8;
        private final TextView tv_sheetRow9;

        private sheetViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tv_sheetRow1 = (TextView) itemView.findViewById(R.id.text_single_product_sales_pandect_goods_id);
            iv_sheetRow2 = (ImageView) itemView.findViewById(R.id.img_single_product_sales_pandect_goods_picture);
            tv_sheetRow2 = (TextView) itemView.findViewById(R.id.text_single_product_sales_pandect_goods_name);
            tv_sheetRow3 = (TextView) itemView.findViewById(R.id.text_single_product_sales_pandect_goods_sales_num);
            tv_sheetRow4 = (TextView) itemView.findViewById(R.id.text_single_product_sales_pandect_goods_cost_price);
            tv_sheetRow5 = (TextView) itemView.findViewById(R.id.text_single_product_sales_pandect_goods_sales_price);
            tv_sheetRow6 = (TextView) itemView.findViewById(R.id.text_single_product_sales_pandect_goods_cash_times);
            tv_sheetRow7 = (TextView) itemView.findViewById(R.id.text_single_product_sales_pandect_goods_wechat_times);
            tv_sheetRow8 = (TextView) itemView.findViewById(R.id.text_single_product_sales_pandect_goods_alipay_times);
            tv_sheetRow9 = (TextView) itemView.findViewById(R.id.text_single_product_sales_pandect_goods_sales_all);
        }

        private TextView getTv_sheetRow1() {
            return tv_sheetRow1;
        }

        public ImageView getIv_sheetRow2() {
            return iv_sheetRow2;
        }

        private TextView getTv_sheetRow2() {
            return tv_sheetRow2;
        }

        private TextView getTv_sheetRow3() {
            return tv_sheetRow3;
        }

        private TextView getTv_sheetRow4() {
            return tv_sheetRow4;
        }

        public TextView getTv_sheetRow5() {
            return tv_sheetRow5;
        }

        private TextView getTv_sheetRow6() {
            return tv_sheetRow6;
        }

        private TextView getTv_sheetRow7() {
            return tv_sheetRow7;
        }

        private TextView getTv_sheetRow8() {
            return tv_sheetRow8;
        }

        private TextView getTv_sheetRow9() {
            return tv_sheetRow9;
        }
    }
}
