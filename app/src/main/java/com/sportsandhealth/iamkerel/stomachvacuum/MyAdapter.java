package com.sportsandhealth.iamkerel.stomachvacuum;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private int[] progressDataset;
    public Resources resources;
    public Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView textView;
        public ProgressBar progressBar;
        public TextView progressText;

        public MyViewHolder(CardView cv, TextView tv,
                            ProgressBar pb, TextView pt) {
            super(cv);
            cardView = cv;
            textView = tv;
            progressBar = pb;
            progressText = pt;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(int[] myDataset, Resources res, Context con) {
        progressDataset = myDataset;
        resources = res;
        context = con;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_day, parent, false);
        TextView textView = (TextView) cardView.findViewById(R.id.text_card_view);
        ProgressBar progressBar = (ProgressBar) cardView.findViewById(R.id.day_progress_bar);
        TextView progressText = (TextView) cardView.findViewById(R.id.day_progress_text);

        MyViewHolder vh = new MyViewHolder(cardView, textView, progressBar, progressText);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String day = String.valueOf(position+1)+resources.getString(R.string.th_day);
        holder.textView.setText(day);

        holder.progressBar.setProgress(progressDataset[position]);

        String progress = String.valueOf(progressDataset[position])+"%";
        holder.progressText.setText(progress);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DayActivity.class);
                intent.putExtra("LEVEL", ProgramActivity.LEVEL);
                intent.putExtra("DAY", position);
                context.startActivity(intent);
            }
        });

        // paint over 100% completed day
        if (progressDataset[position] == 100) {
            holder.cardView.setCardBackgroundColor(resources.getColor(R.color.basic));
        } else {
            holder.cardView.setCardBackgroundColor(resources.getColor(R.color.white));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return progressDataset.length;
    }
}
