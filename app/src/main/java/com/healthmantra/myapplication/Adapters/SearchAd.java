package com.healthmantra.myapplication.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.healthmantra.myapplication.Activity.ItemDetailsActicity;
import com.healthmantra.myapplication.Models.HomeMo;
import com.healthmantra.myapplication.R;


import java.util.List;

public class SearchAd extends RecyclerView.Adapter<SearchAd.MyViewHolder> {
    public Context mContext;
    public List<HomeMo> mHomeList;

    public SearchAd(Context mContext, List<HomeMo> mHomeList) {
        this.mContext = mContext;
        this.mHomeList = mHomeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        final HomeMo homemodel = mHomeList.get(position);


        holder.itemname.setText(homemodel.getItemName());

        holder.itemprice.setText("₹" + homemodel.getItemPrice200());


            Glide.with(holder.itemimage.getContext())
                    .load(homemodel.getItemImage())
                    .into(holder.itemimage);




        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ItemDetailsActicity.class);
                intent.putExtra("itemid", homemodel.getItemId());
                intent.putExtra("itemname", homemodel.getItemName());

                intent.putExtra("itemprice200", homemodel.getItemPrice200());
                intent.putExtra("itemprice1000", homemodel.getItemPrice1000());

                intent.putExtra("itemimage", homemodel.getItemImage());

                intent.putExtra("itemdiscount",homemodel.getItemDiscount());
                intent.putExtra("itemdetail",homemodel.getItemDetail());

                intent.putExtra("itemsubcription200",homemodel.getItemSubscriptionprice200());
                intent.putExtra("itemsubcription1000",homemodel.getItemSubscriptionprice1000());

                intent.putExtra("itembenefits",homemodel.getItembenefit());
                intent.putExtra("itemprevent",homemodel.getItemprevent());

                intent.putExtra("itemonetime200",homemodel.getItemOneTime200());
                intent.putExtra("itemonetime1000",homemodel.getItemOneTime1000());

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mHomeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView itemname, itemprice;
        public ImageView itemimage;
        public LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            itemname = itemView.findViewById(R.id.itemname);
            itemprice = itemView.findViewById(R.id.itemprice);

            itemimage = itemView.findViewById(R.id.itemimage);

            linearLayout = itemView.findViewById(R.id.ll_items);


        }
    }
}
