package com.housey.aeiton.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.housey.aeiton.R;
import com.housey.aeiton.Activity.GameView;

import static com.housey.aeiton.Utils.DataSingleton.card;
import static com.housey.aeiton.Utils.DataSingleton.isPlayed;
import static com.housey.aeiton.Utils.DataSingleton.selectedNos;

/**
 * Created by User on 14-Feb-17.
 */

public class CustomGridAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    Context context;

    public CustomGridAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return card.size();
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

        rootView = inflater.inflate(R.layout.number_column_item, null);
        holder.btn = (Button) rootView.findViewById(R.id.btn);


        if (card.get(position).isActive()) {

            if (card.get(position).isSelected()) {
                holder.btn.setBackgroundResource(R.drawable.circle_bg);
                holder.btn.setTextColor(Color.parseColor("#ffffff"));
            }

            holder.btn.setText("" + card.get(position).getNum());
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!GameView.isVisible) {
                        isPlayed = true;
                        if (!card.get(position).isSelected()) {
                            card.get(position).selected();
                            holder.btn.setBackgroundResource(R.drawable.circle_bg);
                            holder.btn.setTextColor(Color.parseColor("#ffffff"));
                            selectedNos.add(0, position);
                        }
                    }
                }
            });

            holder.btn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!GameView.isVisible) {
                        if (card.get(position).isSelected()) {
                            card.get(position).unSelect();
                            holder.btn.setBackgroundResource(R.drawable.number_unselected_bg);
                            holder.btn.setTextColor(Color.parseColor("#000000"));
                            for (int i = 0; i < selectedNos.size(); i++) {
                                if (selectedNos.get(i).equals(card.get(position))) {
                                    selectedNos.remove(i);
                                }
                            }
                        }

                    }
                    return true;
                }
            });
        }
        return rootView;
    }

    public class Holder {
        Button btn;
    }
}
