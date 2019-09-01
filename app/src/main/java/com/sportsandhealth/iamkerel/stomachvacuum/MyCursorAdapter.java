package com.sportsandhealth.iamkerel.stomachvacuum;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyCursorAdapter extends RecyclerView.Adapter<MyCursorAdapter.MyViewHolder> {
    private Cursor cursor;
    public Resources resources;
    public Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView textExercise;
        public ImageView imageDone;
        public TextView textTime;

        public MyViewHolder(CardView cv, TextView te,
                            TextView tt, ImageView iv) {
            super(cv);
            cardView = cv;
            textExercise = te;
            textTime = tt;
            imageDone = iv;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyCursorAdapter(Cursor myCursor, Resources res, Context con) {
        cursor = myCursor;
        resources = res;
        context = con;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyCursorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_exercise, parent, false);
        TextView textExercise = (TextView) cardView.findViewById(R.id.text_exercise);
        TextView textTime = (TextView) cardView.findViewById(R.id.text_exercise_time);
        ImageView imageDone = (ImageView) cardView.findViewById(R.id.image_done);

        MyViewHolder vh = new MyViewHolder(cardView, textExercise, textTime, imageDone);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        cursor.moveToPosition(position);

        //set exercise name
        switch (cursor.getInt(1)) {
            case 0:
                holder.textExercise.setText(R.string.lying_down);
                break;
            case 1:
                holder.textExercise.setText(R.string.stand_up);
                break;
            case 2:
                holder.textExercise.setText(R.string.table_top);
                break;
        }

        //exercise time
        String time = cursor.getString(2) + " " + resources.getString(R.string.seconds);
        holder.textTime.setText(time);

        //done indicator
        int done = cursor.getInt(3);
        if (done == 1) {
            holder.imageDone.setImageResource(R.drawable.done1);
        } else {
            holder.imageDone.setImageResource(R.drawable.done0);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TrainingActivity.class);
                intent.putExtra("LEVEL", DayActivity.level);
                intent.putExtra("DAY", DayActivity.day);
                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
