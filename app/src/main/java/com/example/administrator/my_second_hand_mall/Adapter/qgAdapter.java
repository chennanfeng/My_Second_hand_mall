package com.example.administrator.my_second_hand_mall.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.my_second_hand_mall.Activity.itemActivity;
import com.example.administrator.my_second_hand_mall.DAO.Utils;
import com.example.administrator.my_second_hand_mall.R;
import com.example.administrator.my_second_hand_mall.bmobObject.qg;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/8/2.
 */

public class qgAdapter extends RecyclerView.Adapter<qgAdapter.ViewHolder> {

    private List<qg> mqgList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView qg_username;
        TextView qg_name;
        TextView qg_describe;
        TextView qg_time;
        TextView qg_price;
        ImageView img_delete;
        ImageView blackLine;
        boolean select;//判断当前项是否被选择为删除

        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.qg_userImg);
            qg_username = (TextView) itemView.findViewById(R.id.qg_username);
            qg_name = (TextView) itemView.findViewById(R.id.qg_name);
            qg_describe = (TextView) itemView.findViewById(R.id.qg_describe);
            qg_time = (TextView) itemView.findViewById(R.id.qg_time);
            qg_price = (TextView) itemView.findViewById(R.id.qg_price);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
            blackLine = (ImageView) itemView.findViewById(R.id.blackLine);
            select = false;
        }
    }

    public qgAdapter(List<qg> qgList){
        mqgList = qgList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.qg_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.select) {
                    holder.img_delete.setImageResource(R.drawable.img_de_true);
                    holder.select = true;
                    int position = holder.getAdapterPosition();
                    Utils.qgList_del.add(mqgList.get(position));
                }
                else {
                    holder.img_delete.setImageResource(R.drawable.img_de_false);
                    holder.select = false;
                    int position = holder.getAdapterPosition();
                    Utils.qgList_del.remove(mqgList.get(position));
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utils.m){
                    int position = holder.getAdapterPosition();
                    qg q = mqgList.get(position);
                    Intent intent = new Intent(v.getContext(), itemActivity.class);
                    intent.putExtra("Name", q.getQgsp_name());
                    intent.putExtra("Describe", q.getDescribe());
                    intent.putExtra("Price",  q.getPrice());
                    intent.putExtra("username",q.getUsername());
                    v.getContext().startActivity(intent);
                }
            }
        });

        return holder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        qg q = mqgList.get(position);
        if(Utils.m){
            if(Utils.canDel){
                holder.img_delete.setVisibility(View.VISIBLE);
            }
            else {
                holder.img_delete.setVisibility(View.GONE);
            }
            holder.circleImageView.setVisibility(View.GONE);
            holder.qg_username.setVisibility(View.GONE);
            holder.blackLine.setVisibility(View.GONE);
            holder.qg_name.setText(q.getQgsp_name());
            if(q.getDescribe().length() > 30){
                holder.qg_describe.setText(q.getDescribe().substring(0 , 30) + "...");
            }
            else {
                holder.qg_describe.setText(q.getDescribe());
            }
            holder.qg_time.setText(q.getCreatedAt());
            holder.qg_price.setText(q.getPrice()+"￥");
        }
        else {
            Glide.with(holder.circleImageView.getContext()).load(q.getUser_iconUrl()).placeholder(R.drawable.person_outline)
                    .error(R.drawable.person_outline).into(holder.circleImageView);
            holder.qg_username.setText(q.getUsername());
            holder.qg_name.setText(q.getQgsp_name());
            if(q.getDescribe().length() > 20){
                holder.qg_describe.setText(q.getDescribe().substring(0 , 20) + "...");
            }
            else {
                holder.qg_describe.setText(q.getDescribe());
            }
            holder.qg_time.setText(q.getCreatedAt());
            holder.qg_price.setText(q.getPrice()+"￥");
        }
    }

    public int getItemCount() {
        return mqgList.size();
    }
}
