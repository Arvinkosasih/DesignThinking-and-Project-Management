package com.woyobank.woyobank.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woyobank.woyobank.R;

import java.util.ArrayList;

/*
 * Adapter for the recycler view
 * used in the transaction history
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> dateTimes = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> signs = new ArrayList<>();
    private ArrayList<String> amounts = new ArrayList<>();
    private ArrayList<String> newBalances = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<String> dateTime, ArrayList<String> title, ArrayList<String> sign, ArrayList<String> amount, ArrayList<String> newBalance){
        dateTimes = dateTime;
        titles = title;
        signs = sign;
        amounts = amount;
        newBalances = newBalance;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewHolder) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.tvDateTime.setText(dateTimes.get(position));
        holder.tvTitle.setText(titles.get(position));


        holder.tvSign.setText(signs.get(position));
        holder.tvAmount.setText(amounts.get(position));
        holder.tvNewBalance.setText(newBalances.get(position));
        holder.tvBalance.setText("Balance :");
        holder.tvDollarSign2.setText("S$");
    }

    @Override
    // any other variable other than "context" would have worked
    // the .size is only to accept more iterations of the values
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvDateTime, tvTitle, tvBalance, tvDollarSign1, tvDollarSign2, tvSign, tvAmount, tvNewBalance;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBalance = itemView.findViewById(R.id.tvBalance);
            tvDollarSign1 = itemView.findViewById(R.id.tvDollarSign);
            tvDollarSign2 = itemView.findViewById(R.id.tvDollarSign2);
            tvSign = itemView.findViewById(R.id.tvSign);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvNewBalance = itemView.findViewById(R.id.tvNewBalance);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
