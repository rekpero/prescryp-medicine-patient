package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.prescywallet.presdigi.Model.OrderHistoryItem;
import com.prescywallet.presdigi.R;
import com.prescywallet.presdigi.TrackOrderActivity;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private List<OrderHistoryItem> listItems;
    private Context context;

    public OrderHistoryAdapter(List<OrderHistoryItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_order_history_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final OrderHistoryItem listItem = listItems.get(position);

        String date_change_format = getDateFormatChange(listItem.getDateOfOrder());
        String[] time_list = listItem.getTimeOfOrder().split(" ");
        String time_changed = time_list[0] + " " + time_list[1].toUpperCase();

        String date_shown = date_change_format.trim() + ", " + time_changed.trim();
        holder.date_of_order.setText(date_shown);
        holder.order_number.setText(listItem.getOrderNumber());
        Locale locale = new Locale("hi", "IN");
        final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        holder.grand_total.setText(nf.format(Float.valueOf(listItem.getGrandTotal())));

        if (listItem.getOrderStatus().equalsIgnoreCase("Confirmed")){
            holder.order_status.setCurrentStep(1);
            holder.order_status_text_view.setText("Your order has been confirmed");
        }else if (listItem.getOrderStatus().equalsIgnoreCase("Dispatched")){
            holder.order_status.setCurrentStep(2);
            holder.order_status_text_view.setText("Your order is on the way");
        }else {
            holder.order_status.setCurrentStep(3);
            holder.order_status_text_view.setText("Your order has been delivered");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TrackOrderActivity.class);
                intent.putExtra("Sender_Key", "From_Order_History");
                intent.putExtra("Order_Number", listItem.getOrderNumber());
                context.startActivity(intent);

            }
        });


    }

    private String getDateFormatChange(String date){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String outputDateStr = "";
        try {
            Date new_date = inputFormat.parse(date);
            outputDateStr = outputFormat.format(new_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateStr;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView date_of_order, order_number, order_status_text_view, grand_total;
        public StepperIndicator order_status;


        public ViewHolder(View itemview){
            super(itemview);
            date_of_order = itemview.findViewById(R.id.date_of_order);
            order_number = itemview.findViewById(R.id.order_number);
            order_status_text_view = itemview.findViewById(R.id.order_status_text_view);
            grand_total = itemview.findViewById(R.id.grand_total);
            order_status = itemview.findViewById(R.id.order_status);
        }
    }
}
