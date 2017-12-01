package cn.edu.stu.max.cocovendor.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.stu.max.cocovendor.R;
import cn.edu.stu.max.cocovendor.databaseClass.Goods;
import cn.edu.stu.max.cocovendor.javaClass.FileService;

public class SheetGoodsAdapter extends RecyclerView.Adapter<SheetGoodsAdapter.ViewHolder> {
    private List<Goods> list;
    private Context context;
    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public SheetGoodsAdapter(List<Goods> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sheet_goods_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_sheetRow1.setText(String.valueOf(list.get(position).getId()));
        if (list.get(position).getName() == null) {
            holder.iv_sheetRow2.setImageResource(R.color.colorTransparency);
        } else {
            // holder.iv_sheetRow2.setImageResource(list.get(position).getImage_path());
            holder.iv_sheetRow2.setImageBitmap(FileService.getUDiskBitmap(list.get(position).getImage_path_s()));
            holder.tv_sheetRow2.setText(list.get(position).getName());
            holder.tv_sheetRow3.setText(String.valueOf(list.get(position).getSales_price()));
            holder.tv_sheetRow4.setText(String.valueOf(list.get(position).getNum()));
            holder.tv_sheetRow6.setText(list.get(position).getOnSaleLocal());
            //判断如果商品在售状态为假，设置为未上架
            if (!list.get(position).isOnSale()) {
                holder.tv_sheetRow5.setText("未上架");
                holder.tv_sheetRow5.setTextColor(Color.RED);
            } else {
                holder.tv_sheetRow5.setText("在售中");
                holder.tv_sheetRow5.setTextColor(Color.GREEN);
            }

        }
        //判断如果监听有设置，则设置点击事件
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Goods getItem(int index) {
        return list.get(index);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tv_sheetRow1;
        final ImageView iv_sheetRow2;
        final TextView tv_sheetRow2;
        final TextView tv_sheetRow3;
        final TextView tv_sheetRow4;
        final TextView tv_sheetRow5;
        final TextView tv_sheetRow6;

        private ViewHolder(View itemView) {
            super(itemView);
            tv_sheetRow1 = (TextView) itemView.findViewById(R.id.tv_sheetRow1);
            iv_sheetRow2 = (ImageView) itemView.findViewById(R.id.iv_sheetRow2);
            tv_sheetRow2 = (TextView) itemView.findViewById(R.id.tv_sheetRow2);
            tv_sheetRow3 = (TextView) itemView.findViewById(R.id.tv_sheetRow3);
            tv_sheetRow4 = (TextView) itemView.findViewById(R.id.tv_sheetRow4);
            tv_sheetRow5 = (TextView) itemView.findViewById(R.id.tv_sheetRow5);
            tv_sheetRow6 = (TextView) itemView.findViewById(R.id.tv_sheetRow6);
        }
    }
}
