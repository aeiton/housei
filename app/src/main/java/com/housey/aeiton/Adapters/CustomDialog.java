package com.housey.aeiton.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.housey.aeiton.R;

import static com.housey.aeiton.Utils.DataSingleton.caller;

/**
 * Created by JAR on 14-06-2017.
 */

public class CustomDialog extends DialogFragment {

    static Context context;
    static int wh;
    static String message;
    ListView lv;

    public CustomDialog() {

    }

    public static CustomDialog newInstance(Context con, int which, String mess) {
        message = mess;
        context = con;
        wh = which;
        return new CustomDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (wh == 0) return inflater.inflate(R.layout.error_custom, container);
        else if (wh == 1) return inflater.inflate(R.layout.claim_custom_layout, container);

        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (wh == 1) {
            lv = (ListView) view.findViewById(R.id.listView);

            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.claim_single_item, R.id.phone_num_tv, caller);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + caller[position])));
                }
            });

        } else if (wh == 0){

            TextView err = (TextView) view.findViewById(R.id.messageTV);
            Button errBtn = (Button) view.findViewById(R.id.err_btn);
            err.setText(message + "\nTry Again Later");
            errBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });



        }

    }
}
