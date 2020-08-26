package com.healthmantra.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.healthmantra.myapplication.CommonClass;
import com.healthmantra.myapplication.Models.OrderInfoMo;
import com.healthmantra.myapplication.R;
import com.healthmantra.myapplication.RecyclerViewClickInterface;


import java.util.List;

public class SubscAd extends RecyclerView.Adapter<SubscAd.MyViewHolder> {
    public List<OrderInfoMo> orderInfoMoList;
    public Context mContext;
    private RecyclerViewClickInterface recyclerViewClickInterface;


    public SubscAd(List<OrderInfoMo> orderInfoMoList, Context mContext, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.orderInfoMoList = orderInfoMoList;
        this.mContext = mContext;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }


    @NonNull
    @Override
    public SubscAd.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_subscription, parent, false);



        return new SubscAd.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscAd.MyViewHolder holder, int position) {


        OrderInfoMo orderInfomodel = orderInfoMoList.get(position);

        String orderid = orderInfomodel.getOrderId();

        holder.textViewTime.setText(orderInfomodel.orderTime+" "+orderInfomodel.getOrderDate());
        holder.textViewOrderid.setText("Order #"+orderInfomodel.orderId);
        if (orderInfomodel.getOrderType().equals("Customize")){
            holder.textViewNameQuantity.setText(orderInfomodel.getOrderitemName()+"-"+orderInfomodel.getOrderQuantity()+"ml\nQuantity in week"+"("+orderInfomodel.getOrderSunQuantity()+" , "+orderInfomodel.getOrderMonQuantity()+" , "+orderInfomodel.getOrderTueQuantity()+" , "+orderInfomodel.getOrderWedQuantity()+" , "+orderInfomodel.getOrderThrQuantity()+" , "+orderInfomodel.getOrderFriQuantity()+" , "+orderInfomodel.getOrderSatQuantity()+")");

        }
        else {
            holder.textViewNameQuantity.setText(orderInfomodel.getOrderitemName()+"-"+orderInfomodel.getOrderQuantity()+"ml x "+orderInfomodel.getOrderPlusMinusQuantity());

        }

        holder.textViewDeliveryDate.setText("from "+orderInfomodel.getOrderStartTime()+" to "+orderInfomodel.getOrderEndTime());

        Glide.with(mContext).load(orderInfomodel.getOrderitemImage()).into(holder.imageView);



        holder.aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){
                holder.aSwitch.setText("Subscription Paused");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SubscriptionList")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                        .child(orderInfomodel.getOrderId())
                        .child("subscriptionstatus");

                reference.setValue("paused");
                reference.notify();

            }
            else {
                holder.aSwitch.setText("Subscription Active");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SubscriptionList").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .child(orderid).child("subscriptionstatus");

                reference.setValue("Active");
                notifyDataSetChanged();

            }


        });

        if (!orderInfomodel.getOrderAddress().equals("address")){
            holder.textViewAddress.setText(CommonClass.address+" "+CommonClass.region+","+CommonClass.location);

        }
        else
        {
            holder.textViewAddress.setText(CommonClass.region+","+CommonClass.location);
        }



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = null;
                if (orderInfomodel.getOrderType().equals("everyday"))
                    type = "Every Day";
                else if (orderInfomodel.getOrderType().equals("on interval")){
                    type = "Every "+orderInfomodel.getOrderIntervalType()+" Day";
                }
                else if (orderInfomodel.getOrderType().equals("customize"))
                {
                    String type1 = null,type2= null,type3 = null,type4= null,type5 = null,type6= null,type7 = null;

                    if (!orderInfomodel.getOrderSunQuantity().equals("0")){
                        type1 = "Sun";
                    }
                    if (!orderInfomodel.getOrderMonQuantity().equals("0")){
                        type2 = ",Mon";
                    }
                    if (!orderInfomodel.getOrderTueQuantity().equals("0")){
                        type3 = ",Tue";
                    }


                    if (!orderInfomodel.getOrderWedQuantity().equals("0")){
                        type4 = ",Wed";
                    }
                    if (!orderInfomodel.getOrderThrQuantity().equals("0")){
                        type5 = ",Thu";
                    }
                    if (!orderInfomodel.getOrderFriQuantity().equals("0")){
                        type6 = ",Fri";
                    }
                    if (!orderInfomodel.getOrderSatQuantity().equals("0")){
                        type7 = ",Sat";
                    }

                    type = type1+type1+type3+type4+type5+type6+type7;

                }
                recyclerViewClickInterface.onItemClick(type,orderInfomodel.orderPlusMinusQuantity,orderInfomodel.orderStartTime,orderInfomodel.orderEndTime);

               // Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show();



            }
        });
    }

    @Override
    public int getItemCount() {
        return orderInfoMoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewOrderid,textViewTime,textViewNameQuantity,  textViewDeliveryDate,textViewAddress;

        ImageView imageView;

        RelativeLayout relativeLayout;

        Switch aSwitch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_image_order_card);
            textViewOrderid = itemView.findViewById(R.id.order_id);
            textViewTime = itemView.findViewById(R.id.time_date);

            textViewNameQuantity = itemView.findViewById(R.id.item_name);

            textViewDeliveryDate = itemView.findViewById(R.id.date_subsribe);
            textViewAddress = itemView.findViewById(R.id.order_address);

            relativeLayout  = itemView.findViewById(R.id.rr_sub);

            aSwitch = itemView.findViewById(R.id.switch_subscription_paused);
        }
    }
}
