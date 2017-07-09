package com.housey.aeiton.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.housey.aeiton.R;
import com.housey.aeiton.Utils.DataSingleton;

/**
 * Created by User on 14-Feb-17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    Context con;
    int wh;

    public RecyclerAdapter(Context context, int which) { // 0 for rewards (3TV) 1 for rules (1TV)
        con = context;
        wh = which;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.rewards_single_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (wh){
            case 0:
                holder.reward.setVisibility(View.VISIBLE);
                holder.cash.setVisibility(View.VISIBLE);
                holder.extra.setVisibility(View.VISIBLE);
                holder.rule.setVisibility(View.GONE);

                holder.reward.setText(DataSingleton.rewards.get(position).getPrize());
                holder.cash.setText(con.getString(R.string.rs) + DataSingleton.rewards.get(position).getCash());
                holder.extra.setText("+ " + DataSingleton.rewards.get(position).getExtra());
                break;

            case 1:
                holder.reward.setVisibility(View.GONE);
                holder.cash.setVisibility(View.GONE);
                holder.extra.setVisibility(View.GONE);
                holder.rule.setVisibility(View.VISIBLE);

                holder.rule.setText(DataSingleton.rule.get(position).getRule());
        }


    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "getItemCount: rewardsSize" + DataSingleton.rewards.size());
        switch (wh){
            case 0: return DataSingleton.rewards.size();
            case 1: return DataSingleton.rule.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reward, cash, extra, rule;
        public ViewHolder(View itemView) {
            super(itemView);

            reward = (TextView) itemView.findViewById(R.id.reward_tv);
            cash = (TextView) itemView.findViewById(R.id.cash_tv);
            extra = (TextView) itemView.findViewById(R.id.extra_tv);
            rule = (TextView) itemView.findViewById(R.id.rule);
        }
    }
}
