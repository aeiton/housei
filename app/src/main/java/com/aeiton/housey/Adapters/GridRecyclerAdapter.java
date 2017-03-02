package com.aeiton.housey.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aeiton.housey.R;

/**
 * Created by User on 14-Feb-17.
 */

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> {

    private String[] mData = new String[0];
    private LayoutInflater mInflater;

    public GridRecyclerAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.number_column_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            String item = mData[position];
            holder.btn.setText(item);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn;
        public ViewHolder(View itemView) {
            super(itemView);
            btn = (Button) itemView.findViewById(R.id.btn);
        }
    }
}
