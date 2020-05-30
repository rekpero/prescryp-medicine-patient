package com.prescywallet.presdigi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.prescywallet.presdigi.DigitalPrescriptionActivity;
import com.prescywallet.presdigi.Model.ListItem;
import com.prescywallet.presdigi.R;

import java.util.List;

public class PresGridAdapter extends ArrayAdapter<ListItem> {
    Context mContext;
    int resourceId;
    List<ListItem> data;
    String key;

    public PresGridAdapter(Context context, int layoutResourceId, List<ListItem> data, String key)
    {
        super(context, layoutResourceId, data);
        this.mContext = context;
        this.resourceId = layoutResourceId;
        this.data = data;
        this.key = key;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent)
    {
        View itemView = convertView;
        ViewHolder holder = null;

        if (itemView == null) {
            final LayoutInflater layoutInflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(resourceId, parent, false);

            holder = new ViewHolder();
            holder.textViewPresId = itemView.findViewById(R.id.gridPresId);
            holder.textViewDate = itemView.findViewById(R.id.gridPresDate);
            holder.presCard = itemView.findViewById(R.id.presCard);
            itemView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) itemView.getTag();
        }

        final ListItem item = getItem(position);
        holder.textViewPresId.setText(item.getPrescriptionId());
        holder.textViewDate.setText(item.getDate());
        holder.presCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DigitalPrescriptionActivity.class);
                intent.putExtra("PrescriptionId", item.getPrescriptionId());
                intent.putExtra("Date", item.getDate());
                if (key.equalsIgnoreCase("Attach Prescription")){
                    intent.putExtra("Sender", "Attach Prescription");
                }else if (key.equalsIgnoreCase("See Prescription")){
                    intent.putExtra("Sender", "See Prescription");
                }
                mContext.startActivity(intent);
            }
        });

        return itemView;
    }

    static class ViewHolder
    {
        TextView textViewPresId;
        TextView textViewDate;
        CardView presCard;
    }

}
