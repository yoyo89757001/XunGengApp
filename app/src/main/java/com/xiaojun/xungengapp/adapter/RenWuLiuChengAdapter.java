package com.xiaojun.xungengapp.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaojun.xungengapp.R;
import com.xiaojun.xungengapp.intface.ClickIntface;

import java.util.List;

/**
 * Created by Administrator on 2017/10/3.
 */

public class RenWuLiuChengAdapter extends RecyclerView.Adapter<RenWuLiuChengAdapter.ViewHolder> {
    private List<String> datas;
    private ClickIntface clickIntface;

    public void setClickIntface(ClickIntface clickIntface){
        this.clickIntface=clickIntface;
    }

    public RenWuLiuChengAdapter(List<String> datas) {
        this.datas = datas;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.renwuliucheng_item,viewGroup,false);
        return new ViewHolder(view);
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            if (position%2==0){
                viewHolder.ll_bg.setBackgroundResource(R.drawable.zidonghuoqu10);
                viewHolder.bianhao1.setBackgroundResource(R.drawable.yuan_lv);
                viewHolder.bianhao1.setTextColor(Color.parseColor("#00C196"));
                viewHolder.zhuangtai.setText("进行中");
                viewHolder.zhuangtai.setBackgroundResource(R.drawable.shixin_lv);

            }else {
                viewHolder.ll_bg.setBackgroundResource(R.drawable.zidonghuoqu_hui);
                viewHolder.bianhao1.setBackgroundResource(R.drawable.yuan_hui);
                viewHolder.bianhao1.setTextColor(Color.parseColor("#EBECEC"));
                viewHolder.zhuangtai.setText("已完成");
                viewHolder.zhuangtai.setBackgroundResource(R.drawable.shixin_hui);
            }

    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
      class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_bg;
        private TextView bianhao1,bianhao2,dizhi,zhuangtai;


        private ViewHolder(View view){
            super(view);
            bianhao1 = (TextView) view.findViewById(R.id.bianhao);
            bianhao2 = (TextView) view.findViewById(R.id.bianhao2);
            dizhi = (TextView) view.findViewById(R.id.dizhi);
            zhuangtai = (TextView) view.findViewById(R.id.zhuangtai);
            ll_bg= (LinearLayout) view.findViewById(R.id.ll_bg);

        }
    }


}
