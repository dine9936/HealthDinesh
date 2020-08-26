package com.healthmantra.myapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.healthmantra.myapplication.R;
import com.healthmantra.myapplication.TransactionMo;

import java.util.List;

public class TransactAd extends RecyclerView.Adapter<TransactAd.MyViewHolder> {
    public List<TransactionMo> transactionMoList;
    public Context mContext;

    public TransactAd(List<TransactionMo> transactionMoList, Context mContext) {
        this.transactionMoList = transactionMoList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TransactAd.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transaction_history, parent, false);


        return new TransactAd.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactAd.MyViewHolder holder, int position) {

        TransactionMo transactionMo = transactionMoList.get(position);


        if (transactionMo.getType().equals("plus")){
            holder.amountDestination.setText("Wallet Recharge");
            holder.txnAmount.setText("+ ₹"+transactionMo.getTxnamount());
            holder.ordId.setText("Transaction Id : "+transactionMo.getTxnid());


        }
        else if (transactionMo.getType().equals("minus")){
            holder.amountDestination.setText("Debited From Wallet");
            holder.txnAmount.setText("- ₹"+transactionMo.getTxnamount()+".00");
            holder.txnAmount.setTextColor(Color.RED);
            holder.ordId.setText("Order Id : "+transactionMo.getTxnid());

        }
        else if (transactionMo.getType().equals("cashback")){
            holder.amountDestination.setText("CashBack");
            holder.txnAmount.setText("+ ₹"+transactionMo.getTxnamount()+".00");
            holder.ordId.setText("Transaction Id : "+transactionMo.getTxnid());

        }

        if (transactionMo.getTxnStatus().equals("Successful")){
            holder.status.setText("Successful");

        }
        else {
            holder.status.setText("Failed");
            holder.status.setTextColor(Color.RED);
        }



        holder.date.setText(transactionMo.getTxntime());

    }

    @Override
    public int getItemCount() {
        return transactionMoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView amountDestination, ordId, txnAmount,date,status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            amountDestination = itemView.findViewById(R.id.tv_transaction_type);
            ordId = itemView.findViewById(R.id.tv_transaction_detail);
            txnAmount = itemView.findViewById(R.id.tv_amount);

            date = itemView.findViewById(R.id.wallet_date);
            status = itemView.findViewById(R.id.tv_transaction_status);


        }
    }
}
