package com.healthmantra.myapplication.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.healthmantra.myapplication.CommonClass;
import com.healthmantra.myapplication.Models.OrderInfoMo;
import com.healthmantra.myapplication.R;


import java.util.List;

public class OrderAd extends RecyclerView.Adapter<OrderAd.MyViewHolder> {
    public List<OrderInfoMo> orderInfoMoList;
    public Context mContext;


    public OrderAd(List<OrderInfoMo> orderInfoMoList, Context mContext) {
        this.orderInfoMoList = orderInfoMoList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Log.d("TAG", "getItemCount: "+ orderInfoMoList.size());

        OrderInfoMo orderInfomodel = orderInfoMoList.get(position);

        holder.textViewTime.setText(orderInfomodel.orderTime+" "+orderInfomodel.getOrderDate());
        holder.textViewOrderid.setText("Order #"+orderInfomodel.orderId);
        holder.textViewNameQuantity.setText(orderInfomodel.getOrderitemName()+"-"+orderInfomodel.getOrderQuantity()+"ml x "+orderInfomodel.getOrderPlusMinusQuantity());


        holder.textViewDeliveryDate.setText(" for "+orderInfomodel.getOrderStartTime());

        Glide.with(mContext).load(orderInfomodel.getOrderitemImage()).into(holder.imageView);

        int a = Integer.parseInt(orderInfomodel.getOrderPlusMinusQuantity());
        int b = Integer.parseInt(orderInfomodel.getOrderitemPrice());
        int total = a*b;
        holder.textViewPrice.setText("Price Paid - Rs"+ String.valueOf(total));


        if (!orderInfomodel.getOrderAddress().equals("address")){
            holder.textViewAddress.setText(CommonClass.address+" "+CommonClass.region+","+CommonClass.location);

        }
        else
        {
            holder.textViewAddress.setText(CommonClass.region+","+CommonClass.location);
        }


    }

    @Override
    public int getItemCount() {

        Log.d("TAG", "getItemCount: "+ orderInfoMoList.size());
        return orderInfoMoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewOrderid,textViewTime,textViewNameQuantity,textViewPrice, textViewDeliveryDate,textViewAddress;

        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);



            imageView = itemView.findViewById(R.id.item_image_order_card);
            textViewOrderid = itemView.findViewById(R.id.order_id);
            textViewTime = itemView.findViewById(R.id.time_date);

            textViewNameQuantity = itemView.findViewById(R.id.item_name);

            textViewPrice = itemView.findViewById(R.id.item_price);

            textViewDeliveryDate = itemView.findViewById(R.id.date_delivery);
            textViewAddress = itemView.findViewById(R.id.order_address);
        }
    }
}
