package com.housey.aeiton.Adapters;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.housey.aeiton.R;

import static com.housey.aeiton.Utils.DataSingleton.caller;

/**
 * Created by JAR on 14-06-2017.
 */

public class CustomDialog extends DialogFragment{

    static Context context;
    ListView lv;

    public CustomDialog(){

    }

    public static CustomDialog newInstance(Context con){
        context = con;
        return new CustomDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.claim_custom_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ListView) view.findViewById(R.id.listView);

        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.claim_single_item, R.id.phone_num_tv, caller);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + caller[position])));
            }
        });

    }
}
