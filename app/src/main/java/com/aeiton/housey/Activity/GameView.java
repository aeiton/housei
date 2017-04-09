package com.aeiton.housey.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aeiton.housey.Adapters.CustomGridAdapter;
import com.aeiton.housey.HouseyNumber;
import com.aeiton.housey.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameView extends AppCompatActivity {

    GridView gv;
    Context context;
    public static ArrayList<HouseyNumber> numList;
    ImageButton reset;
    LinearLayout undo;
    ImageView call;
    int MY_PERMISSION_CALL_PHONE = 1;
    TextView tv;
    boolean flag;

    public static ArrayList<Integer> selectedStack = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_view);
        gv = (GridView) findViewById(R.id.gridview);
        findViewById(R.id.marqueeText).setSelected(true);
//        reset = (ImageButton) findViewById(R.id.reset);
//        undo = (LinearLayout) findViewById(R.id.undo);
//        call = (ImageView) findViewById(R.id.call);


        Random rand = new Random();

        numList = new ArrayList<>();
        numList.add(new HouseyNumber(1, true));
        numList.add(new HouseyNumber(12, true));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(43, true));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(61, true));
        numList.add(new HouseyNumber(73, true));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(5, true));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(34, true));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(56, true));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(76, true));
        numList.add(new HouseyNumber(85, true));
        numList.add(new HouseyNumber(8, true));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(28, true));
        numList.add(new HouseyNumber(37, true));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(67, true));
        numList.add(new HouseyNumber(0, false));
        numList.add(new HouseyNumber(89, true));


        gv.setAdapter(new CustomGridAdapter(GameView.this));

//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(GameView.this);
//                dialog.setMessage("Do you want to reset ?")
//                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                for (HouseyNumber h : numList)
//                                    h.unSelect();
//                                gv.setAdapter(new CustomGridAdapter(GameView.this));
//                            }
//                        })
//                        .setNegativeButton("NO", null)
//                        .setCancelable(true)
//                        .show();
//
//
//            }
//        });


//        undo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedStack.size() != 0) {
//                    numList.get(selectedStack.get(0)).unSelect();
//                    selectedStack.remove(0);
//
//                    gv.setAdapter(new CustomGridAdapter(GameView.this));
//                }
//            }
//        });
//
//
//        call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:9876543210")));
//            }
//        });
    }


}
