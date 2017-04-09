package com.aeiton.housey.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aeiton.housey.Activity.GameView;
import com.aeiton.housey.HouseyNumber;
import com.aeiton.housey.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 14-Feb-17.
 */

public class CustomGridAdapter extends BaseAdapter {


    Context context;
    private static LayoutInflater inflater = null;

    public CustomGridAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return GameView.numList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View rootView;

        rootView = inflater.inflate(R.layout.number_column_item,null);
        holder.btn = (Button) rootView.findViewById(R.id.btn);
        holder.card = (FrameLayout) rootView.findViewById(R.id.card);


        if (GameView.numList.get(position).isActive()) {

            if (GameView.numList.get(position).isSelected()){
                holder.btn.setBackgroundColor(Color.parseColor("#66bb6a"));
                holder.btn.setTextColor(Color.parseColor("#ffffff"));
            }

            holder.btn.setText(""+GameView.numList.get(position).getNum());
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!GameView.numList.get(position).isSelected()){
                        GameView.numList.get(position).selected();
                        holder.btn.setBackgroundResource(R.drawable.circle_bg);
                        holder.btn.setTextColor(Color.parseColor("#ffffff"));
                        GameView.selectedStack.add(0,position);

                    }else {
                        GameView.numList.get(position).unSelect();
                        holder.btn.setBackgroundResource(R.drawable.number_unselected_bg);
                        holder.btn.setTextColor(Color.parseColor("#000000"));
                        int i = 0;
                        for(int item : GameView.selectedStack){
                            if (item == position) {
                                GameView.selectedStack.remove(i);
                                break;
                            }
                            i++;
                        }
                    }
                }
            });
        }
        return rootView;
    }

    public class Holder {
        Button btn;
        FrameLayout card;
    }
}
