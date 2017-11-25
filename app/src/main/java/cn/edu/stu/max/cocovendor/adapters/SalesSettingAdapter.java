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
import cn.edu.stu.max.cocovendor.activities.SheetGoodsActivity;
import cn.edu.stu.max.cocovendor.databaseClass.Goods;

public class SalesSettingAdapter extends RecyclerView.Adapter<SalesSettingAdapter.ViewHolder> {
    private List<Goods> list;
    private Context context;

    public SalesSettingAdapter(List<Goods> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_setting_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_sheetRow1.setText(String.valueOf(position + 1));
        if (list.get(position).getName() == null) {
            holder.iv_sheetRow2.setImageResource(R.color.colorTransparency);
            holder.tv_sheetRow2.setText("");
            holder.tv_sheetRow3.setText("");
            holder.tv_sheetRow4.setText("");
        } else {
            holder.iv_sheetRow2.setImageResource(list.get(position).getImage_path());
            holder.tv_sheetRow2.setText(list.get(position).getName());
            holder.tv_sheetRow3.setText(String.valueOf(list.get(position).getSales_price()));
            holder.tv_sheetRow4.setText(String.valueOf(list.get(position).getNum()));
        }
        holder.btn_setGoods.setOnClickListener(new GoodsSettingClickListener(position));
        holder.btn_delGoods.setOnClickListener(new GoodsSettingClickListener(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Goods getItem(int index) {
        return list.get(index);
    }

    public List<Goods> getList() {
        return list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tv_sheetRow1;
        final ImageView iv_sheetRow2;
        final TextView tv_sheetRow2;
        final TextView tv_sheetRow3;
        final TextView tv_sheetRow4;
        final Button btn_setGoods;
        final Button btn_delGoods;

        private ViewHolder(View itemView) {
            super(itemView);
            tv_sheetRow1 = (TextView) itemView.findViewById(R.id.tv_sheetRow1);
            iv_sheetRow2 = (ImageView) itemView.findViewById(R.id.iv_sheetRow2);
            tv_sheetRow2 = (TextView) itemView.findViewById(R.id.tv_sheetRow2);
            tv_sheetRow3 = (TextView) itemView.findViewById(R.id.tv_sheetRow3);
            tv_sheetRow4 = (TextView) itemView.findViewById(R.id.tv_sheetRow4);
            btn_setGoods = (Button) itemView.findViewById(R.id.btn_set_goods);
            btn_delGoods = (Button) itemView.findViewById(R.id.btn_del_goods);
        }
    }

    private class GoodsSettingClickListener implements View.OnClickListener {
        private int postion;

        GoodsSettingClickListener(int postion) {
            this.postion = postion;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_set_goods:
                    Intent intent = new Intent(context, SheetGoodsActivity.class);
                    intent.putExtra("cabinetNum", postion);
                    intent.putExtra("isSelGoods", true);
                    context.startActivity(intent);
                    break;
                case R.id.btn_del_goods:
            }
        }
    }
}
