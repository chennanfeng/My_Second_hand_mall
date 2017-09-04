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
import com.example.administrator.my_second_hand_mall.bmobObject.sp;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/8/4.
 */

public class spAdapter extends RecyclerView.Adapter<spAdapter.ViewHolder> {

    private List<sp> mspList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView sp_username;
        TextView sp_name;
        TextView sp_describe;
        TextView sp_time;
        TextView sp_price;
        ImageView img_delete;
        ImageView blackLine;
        boolean select;//判断当前项是否被选择为删除

        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.sp_userImg);
            sp_username = (TextView) itemView.findViewById(R.id.sp_username);
            sp_name = (TextView) itemView.findViewById(R.id.sp_name);
            sp_describe = (TextView) itemView.findViewById(R.id.sp_describe);
            sp_time = (TextView) itemView.findViewById(R.id.sp_time);
            sp_price = (TextView) itemView.findViewById(R.id.sp_price);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
            blackLine = (ImageView) itemView.findViewById(R.id.blackLine);
            select = false;
        }
    }

    public spAdapter(List<sp> spList){
        mspList = spList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sp_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.select) {
                    holder.img_delete.setImageResource(R.drawable.img_de_true);
                    holder.select = true;
                    int position = holder.getAdapterPosition();
                    Utils.spList_del.add(mspList.get(position));
                }
                else {
                    holder.img_delete.setImageResource(R.drawable.img_de_false);
                    holder.select = false;
                    int position = holder.getAdapterPosition();
                    Utils.spList_del.remove(mspList.get(position));
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utils.m){
                    int position = holder.getAdapterPosition();
                    sp s = mspList.get(position);
                    Intent intent = new Intent(v.getContext(), itemActivity.class);
                    intent.putExtra("Name", s.getSp_name());
                    intent.putExtra("Describe", s.getDescribe());
                    intent.putExtra("Url", s.getSp_icon().getUrl());
                    intent.putExtra("Price",  s.getPrice());
                    intent.putExtra("username",s.getUsername());
                    v.getContext().startActivity(intent);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        sp s = mspList.get(position);
        if(Utils.m){
            if(Utils.canDel){
                holder.img_delete.setVisibility(View.VISIBLE);
            }
            else {
                holder.img_delete.setVisibility(View.GONE);
            }
            holder.circleImageView.setVisibility(View.GONE);
            holder.sp_username.setVisibility(View.GONE);
            holder.blackLine.setVisibility(View.GONE);
            holder.sp_name.setText(s.getSp_name());
            if(s.getDescribe().length() > 30){
                holder.sp_describe.setText(s.getDescribe().substring(0 , 30) + "...");
            }
            else {
                holder.sp_describe.setText(s.getDescribe());
            }
            holder.sp_time.setText(s.getCreatedAt());
            holder.sp_price.setText(s.getPrice()+"￥");
        }
        else {
            Glide.with(holder.circleImageView.getContext()).load(s.getUser_iconUrl()).placeholder(R.drawable.person_outline)
                    .error(R.drawable.person_outline).into(holder.circleImageView);
            holder.sp_username.setText(s.getUsername());
            holder.sp_name.setText(s.getSp_name());
            if(s.getDescribe().length() > 20){
                holder.sp_describe.setText(s.getDescribe().substring(0 , 20) + "...");
            }
            else {
                holder.sp_describe.setText(s.getDescribe());
            }
            holder.sp_time.setText(s.getCreatedAt());
            holder.sp_price.setText(s.getPrice()+"￥");
        }
    }

    @Override
    public int getItemCount() {
        return mspList.size();
    }
}
