package cn.edu.stu.max.cocovendor.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.stu.max.cocovendor.R;
import cn.edu.stu.max.cocovendor.activities.DailySalesActivity;
import cn.edu.stu.max.cocovendor.activities.MonthlySalesActivity;
import cn.edu.stu.max.cocovendor.activities.SingleProductSalesAnalyzeActivity;
import cn.edu.stu.max.cocovendor.databaseClass.Goods;
import cn.edu.stu.max.cocovendor.databaseClass.SingleProductSalesAnalyze;
import cn.edu.stu.max.cocovendor.databaseClass.SingleProductSalesPandect;
import cn.edu.stu.max.cocovendor.javaClass.FileService;

public class SingleProductSalesAnalyzeAdapter extends RecyclerView.Adapter {
    private List<SingleProductSalesAnalyze> list;
    private Context context;

    public SingleProductSalesAnalyzeAdapter(List<SingleProductSalesAnalyze> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_sales_analyze_item, parent, false);
        return new sheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        sheetViewHolder vh = (sheetViewHolder) holder;

        vh.getTv_sheetRow1().setText(String.valueOf(position + 1));
        if (list.get(position).getGoodsName() == null) vh.getIv_sheetRow2().setImageResource(R.color.colorTransparency);
        else vh.getIv_sheetRow2().setImageBitmap(FileService.getUDiskBitmap(list.get(position).getImagePath()));
        vh.getTv_sheetRow2().setText(list.get(position).getGoodsName());

        vh.getBtn_sheetRow2().setOnClickListener(new CustomClickListener(position));
        vh.getBtn_sheetRow3().setOnClickListener(new CustomClickListener(position));
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
        private final Button btn_sheetRow2;
        private final Button btn_sheetRow3;

        private sheetViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tv_sheetRow1 = (TextView) itemView.findViewById(R.id.text_id);
            iv_sheetRow2 = (ImageView) itemView.findViewById(R.id.text_goods_picture);
            tv_sheetRow2 = (TextView) itemView.findViewById(R.id.text_goods_name);
            btn_sheetRow2 = (Button) itemView.findViewById(R.id.btn_daily_sales);
            btn_sheetRow3 = (Button) itemView.findViewById(R.id.btn_monthly_sales);

        }

        private TextView getTv_sheetRow1() {
            return tv_sheetRow1;
        }

        private ImageView getIv_sheetRow2() {
            return iv_sheetRow2;
        }

        private TextView getTv_sheetRow2() {
            return tv_sheetRow2;
        }

        private Button getBtn_sheetRow2() {
            return btn_sheetRow2;
        }

        private Button getBtn_sheetRow3() { return btn_sheetRow3; }
    }

    private class CustomClickListener implements View.OnClickListener {
        private int position;
        public CustomClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_daily_sales) {
                Intent intent = new Intent(context, DailySalesActivity.class);
                context.startActivity(intent);
            } else if (view.getId() == R.id.btn_monthly_sales) {
                Intent intent = new Intent(context, MonthlySalesActivity.class);
                context.startActivity(intent);
            }
        }
    }
}
