package com.housey.aeiton.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.housey.aeiton.Adapters.CustomDialog;
import com.housey.aeiton.Adapters.CustomGridAdapter;
import com.housey.aeiton.Adapters.PicassoImageSwitcher;
import com.housey.aeiton.Adapters.RecyclerAdapter;
import com.housey.aeiton.R;
import com.housey.aeiton.Utils.Constants;
import com.housey.aeiton.Utils.DataSingleton;
import com.housey.aeiton.Utils.HawkSingleton;
import com.housey.aeiton.Utils.HouseyNumber;
import com.housey.aeiton.Utils.Rewards;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.housey.aeiton.Utils.Constants.ISCARD;
import static com.housey.aeiton.Utils.Constants.ISPLAYED;
import static com.housey.aeiton.Utils.Constants.REGISTRATION_KEY;
import static com.housey.aeiton.Utils.Constants.SELECTEDNO;
import static com.housey.aeiton.Utils.Constants.USER_ID;
import static com.housey.aeiton.Utils.DataSingleton.adPaths;
import static com.housey.aeiton.Utils.DataSingleton.caller;
import static com.housey.aeiton.Utils.DataSingleton.card;
import static com.housey.aeiton.Utils.DataSingleton.cardId;
import static com.housey.aeiton.Utils.DataSingleton.cardNo;
import static com.housey.aeiton.Utils.DataSingleton.cardPattern;
import static com.housey.aeiton.Utils.DataSingleton.date;
import static com.housey.aeiton.Utils.DataSingleton.isPlayed;
import static com.housey.aeiton.Utils.DataSingleton.marquee;
import static com.housey.aeiton.Utils.DataSingleton.response;
import static com.housey.aeiton.Utils.DataSingleton.rewards;
import static com.housey.aeiton.Utils.DataSingleton.rule;
import static com.housey.aeiton.Utils.DataSingleton.selectedNos;
import static com.housey.aeiton.Utils.DataSingleton.userId;
import static com.housey.aeiton.Utils.DataSingleton.valid;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameView extends AppCompatActivity {

    public static boolean isVisible = false;
    public ImageButton undo;
    RecyclerView.Adapter adapter;
    GridView gv;
    RecyclerView recyclerView;
    ImageButton reset, prize, rules, rpClose;
    LinearLayout call;
    TextView ticketNo, rewardsTv, marqueeText, gameDate;

    String cardResponse, label = "ticket_no";
    JSONArray array;
    ImageSwitcher adBanner1, adBanner2, adBanner3;
    PicassoImageSwitcher switcher1, switcher2, switcher3;

    Animation leftOpen, leftClose, appear, disappear;
    int which, g = 0;
    int[] pos = {0, 1, 2, 0};
    ActionMenuView actionMenuView;

    Handler m_handler;
    Runnable m_handlerTask;
    int firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        actionMenuView = (ActionMenuView) toolbar.findViewById(R.id.menumenu);

        actionMenuView.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    /*case R.id.namma_tv_live: startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINK))); return true;
                    case R.id.namma_tv_yt: startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINK))); return true;*/
                    case R.id.exit_game:
                        onBackPressed();
                        return true;
                }
                return true;
            }
        });

        setSupportActionBar(toolbar);
        // toolbar.inflateMenu(R.menu.game_view_menu);

        reset = (ImageButton) findViewById(R.id.reset);
        undo = (ImageButton) findViewById(R.id.undo);
        rpClose = (ImageButton) findViewById(R.id.r_p_close);
        call = (LinearLayout) findViewById(R.id.call);
        ticketNo = (TextView) findViewById(R.id.ticket_no);
        gameDate = (TextView) findViewById(R.id.game_date);
        rewardsTv = (TextView) findViewById(R.id.rewards_tv);

        adBanner1 = (ImageSwitcher) findViewById(R.id.ad_banner1);
        adBanner2 = (ImageSwitcher) findViewById(R.id.ad_banner2);
        adBanner3 = (ImageSwitcher) findViewById(R.id.ad_banner3);

        marqueeText = (TextView) findViewById(R.id.marqueeText);

        prize = (ImageButton) findViewById(R.id.prize);
        rules = (ImageButton) findViewById(R.id.rules);

        gv = (GridView) findViewById(R.id.gridview);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        makeInvisible();
        setUpAnimation();
        setUpAdBanners();

        cardResponse = getIntent().getStringExtra("CARDRESPONSE");
        Log.d("CardResponse", " " + cardResponse);
        parseCard(cardResponse);

        isPlayed = HawkSingleton.getInstance().hawkGet(ISPLAYED, false);
        if (isPlayed) retriveList();

        ticketNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(label, ticketNo.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(GameView.this, "Ticket No " + cardNo + " is copied to the clipboard", Toast.LENGTH_LONG).show();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(GameView.this);
                dialog.setMessage("Do you want to reset ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedNos.clear();
                                HawkSingleton.getInstance().hawkPut(SELECTEDNO, "");
                                for (HouseyNumber h : card)
                                    h.unSelect();
                                gv.setAdapter(new CustomGridAdapter(GameView.this));

                            }
                        })
                        .setNegativeButton("NO", null)
                        .setCancelable(true)
                        .show();


            }
        });


        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNos.size() != 0) {
                    card.get(selectedNos.get(0)).unSelect();
                    selectedNos.remove(0);

                    gv.setAdapter(new CustomGridAdapter(GameView.this));
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeACall();
            }
        });


        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardsTv.setText("Rules");
                rewardsTv.setVisibility(View.VISIBLE);
                rewardsTv.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_format_list_bulleted_black_24dp),
                        null,
                        null,
                        null);

                initRecycler(1);
                if (isVisible) {
                    switch (which) {
                        case 1:
                            recyclerView.startAnimation(disappear);
                            rpClose.setAnimation(disappear);
                            rewardsTv.setAnimation(disappear);
                            makeInvisible();
                            break;
                        case 2:
                            recyclerView.startAnimation(appear);
                            rpClose.setAnimation(appear);
                            rewardsTv.setAnimation(appear);
                            which = 1;
                            makeVisible();
                            break;
                    }

                } else {
                    which = 1;
                    makeVisible();
                    rpClose.setAnimation(appear);
                    rewardsTv.setAnimation(appear);
                    recyclerView.startAnimation(appear);
                }
            }
        });

        prize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRecycler(0);
                rewardsTv.setText("Rewards");
              rewardsTv.setVisibility(View.VISIBLE);
                rewardsTv.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_card_giftcard_black_24dp),
                        null,
                        null,
                        null);
                if (isVisible) {
                    switch (which) {
                        case 1:
                            which = 2;
                            recyclerView.startAnimation(appear);
                            rpClose.setAnimation(appear);
                            rewardsTv.setAnimation(appear);
                            break;
                        case 2:
                            recyclerView.startAnimation(disappear);
                            rpClose.setAnimation(disappear);
                            rewardsTv.setAnimation(disappear);
                            makeInvisible();
                            break;
                    }
                } else {
                    which = 2;
                    makeVisible();

                    rpClose.setAnimation(appear);
                    rewardsTv.setAnimation(appear);
                    recyclerView.startAnimation(appear);
                }
            }
        });

        rpClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisible = false;
                recyclerView.startAnimation(disappear);
                rpClose.setAnimation(disappear);
                rewardsTv.setAnimation(disappear);
                makeInvisible();
            }
        });

    }


    @Override
    protected void onPause() {

        Log.d("onPause", "PAUSED");
        saveList();
        m_handler.removeCallbacks(m_handlerTask);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        Log.d("onDestroy", "DESTROYED");
        saveList();
        m_handler.removeCallbacks(m_handlerTask);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_view_menu, actionMenuView.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()){
            case R.id.namma_tv_live: startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINK)));
            case R.id.namma_tv_yt: startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINK)));
            case R.id.exit_game: onBackPressed();
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Boolean[] checked = {false,};
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Do you want to exit the game?")
                .setMessage(R.string.leave_message)
                .setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveList();
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), Splash.class));
                        finish();
                    }
                })

                .setNegativeButton("STAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();


    }

    private void parseCard(String response) {
        Log.d("Response", " " + response);
        try {
            JSONObject object = new JSONObject(response);

            cardNo = object.getString("ticket_no");
            cardPattern = object.getString("ticket_pattern");
            cardId = object.getString("ticket_id");
            valid = object.getString("valid_till");
            date = object.getString("game_time");
            caller[0] = object.getString("call1");
            caller[1] = object.getString("call2");

            //for rules
            JSONArray array = new JSONArray(object.getString("rules"));
            rule.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject rulesObj = array.getJSONObject(i);
                DataSingleton.rule.add(new Rewards(rulesObj.getString("rule")));
            }

            //for prize
            JSONArray array1 = new JSONArray(object.getString("prize"));
            rewards.clear();
            for (int i = 0; i < array1.length(); i++) {
                JSONObject prizeObj = array1.getJSONObject(i);

                DataSingleton.rewards.add(
                        new Rewards(
                                prizeObj.getString("prize"),
                                prizeObj.getString("cash"),
                                prizeObj.getString("extra")));
            }


            //for adpaths
            JSONArray array2 = new JSONArray(object.getString("ad_path"));
            adPaths.clear();
            for (int i = 0; i < array2.length(); i++) {
                JSONObject adsObj = array2.getJSONObject(i);
                adPaths.add(adsObj.getString("path"));
//                adPaths.add(adsObj.getString("path"));
//                adPaths.add(adsObj.getString("path"));
                Log.d("adPaths", " " + adPaths.get(i));
            }

            // for marquee text
            StringBuilder marqueeBuilder = new StringBuilder();
            JSONArray array3 = new JSONArray(object.getString("mr_text"));
            for (int i = 0; i < array3.length(); i++) {
                JSONObject marqObj = array3.getJSONObject(i);

                marqueeBuilder.append(marqObj.getString("text"));
                if (i < array3.length() - 1)
                    marqueeBuilder.append("     \u2022      ");//\u00bb
            }
            marquee = marqueeBuilder.toString();
            Log.d("Mar", "  " + marquee);


            setTicketNum(cardNo);

            marqueeText.setSelected(true);
            gameDate.setSelected(true);
            marqueeText.setText(marquee);

            gameDate.setText("Game Date: " + date);
            if (cardPattern != null)
                splitTicket();
            else {
                startActivity(new Intent(GameView.this, Splash.class));
                this.finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void splitTicket() {
        String[] subString;
        subString = cardPattern.split(",");
        card.clear();
        for (String s : subString) {
            if (s.equals("0"))
                card.add(new HouseyNumber(Integer.valueOf(s), false));
            else
                card.add(new HouseyNumber(Integer.valueOf(s), true));
        }

        gv.setAdapter(new CustomGridAdapter(GameView.this));
        displayAds();
    }

    private void saveData() {

        getSharedPreferences(REGISTRATION_KEY, MODE_PRIVATE).edit().putInt(USER_ID, userId).apply();
        HawkSingleton.getInstance().hawkPut(Constants.RESPONSE, response);
        HawkSingleton.getInstance().hawkPut(Constants.ISCARD, true);
        HawkSingleton.getInstance().hawkPut(Constants.VALIDITY, valid);
        HawkSingleton.getInstance().hawkPut(Constants.SELECTEDNO, array.toString());
        HawkSingleton.getInstance().hawkPut(Constants.ISPLAYED, isPlayed);

    }

    private void saveList() {
        array = new JSONArray();
        for (int i = 0; i < selectedNos.size(); i++) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("num", selectedNos.get(i));
                Log.d("selectedNo", " " + selectedNos.get(i));
                array.put(obj);
                saveData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void retriveList() {
        String jArray = HawkSingleton.getInstance().hawkGet(SELECTEDNO);
        JSONObject obj;
        int num;
        try {
            array = new JSONArray(jArray);
            selectedNos.clear();
            for (int j = 0; j < array.length(); j++) {
                obj = array.getJSONObject(j);

                num = obj.getInt("num");
                selectedNos.add(num);
                if (!card.isEmpty())
                    card.get(num).selected();
                else {
                    startActivity(new Intent(GameView.this, Splash.class));
                    GameView.this.finish();
                    HawkSingleton.getInstance().hawkPut(ISCARD, false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayAds() {
        firstTime++;

        m_handler = new Handler(Looper.getMainLooper());

        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... aVoid) {
                //wait for 30 secs here and continue
                Log.d("control", "DIB");

                changePositions();
                Log.d("TAG", "doInBackground: changingDone");
                return g;
            }

            @Override
            protected void onPostExecute(Integer aVoid) {
                super.onPostExecute(aVoid);

                Log.d("control", "onPostExecute");
                Log.d("PositionsOPE", pos[0] + " " + pos[1] + " " + pos[2] + " " + g + " ");


                Picasso.with(getApplicationContext())
                        .load(adPaths.get(pos[0]))
                        .placeholder(R.drawable.banner_1)
                        .error(R.drawable.banner_1)
                        .into(switcher1);
                Picasso.with(getApplicationContext())
                        .load(adPaths.get(pos[1]))
                        .placeholder(R.drawable.banner_1)
                        .error(R.drawable.banner_1)
                        .into(switcher2);
                Picasso.with(getApplicationContext())
                        .load(adPaths.get(pos[2]))
                        .placeholder(R.drawable.banner_1)
                        .error(R.drawable.banner_1)
                        .into(switcher3);

                m_handlerTask = new Runnable() {
                    @Override
                    public void run() {
                        displayAds();
                    }
                };
                int time1 = 3000, time2 = 3000; //switch ads after 3 secs (time to download
                if (firstTime == 1)
                    m_handler.postDelayed(m_handlerTask, time1);
                else m_handler.postDelayed(m_handlerTask, time2);

            }
        });

    }

    private void makeVisible() {
        isVisible = true;
        recyclerView.setVisibility(View.VISIBLE);
        rpClose.setVisibility(View.VISIBLE);

    }

    private void makeInvisible() {
        isVisible = false;
        rewardsTv.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        rpClose.setVisibility(View.INVISIBLE);
    }

    private void setUpAnimation() {
        leftOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rules_open_anim);
        leftClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rules_close_anim);
        appear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.btn_appear);
        disappear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.btn_disappear);
    }

    private void makeACall() {

        CustomDialog cd = CustomDialog.newInstance(getApplicationContext(), 1, " ");
        cd.show(getSupportFragmentManager(), "customDialog");
        cd.setStyle(0, R.style.DialogTheme);
    }

    private void setTicketNum(String num) {
        StringBuilder newNum = new StringBuilder();

        for (int i = 0; i < num.length(); i++) {
            newNum.append(num.charAt(i));
            if (i != num.length() - 1)
                newNum.append("\n");
        }
        ticketNo.setText(newNum);
    }

    void initRecycler(int which) {
        recyclerView.setVisibility(View.VISIBLE);
        adapter = new RecyclerAdapter(GameView.this, which);
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (which != 3) recyclerView.setAdapter(adapter);
    }

    private void changePositions() {

        if (g + 2 >= adPaths.size()) {
            pos[0] = g;
            pos[1] = pos[0] + 1;
            pos[2] = 0;
        }
        if (g + 1 >= adPaths.size()) {
            pos[0] = g;
            pos[1] = 0;
            pos[2] = pos[1] + 1;
        }
        if (g >= adPaths.size()) {
            g = 0;
            pos[0] = 0;
            pos[1] = pos[0] + 1;
            pos[2] = pos[1] + 1;
        }

        g++;
    }

    private void setUpAdBanners() {
        adBanner1.setInAnimation(leftClose);
        adBanner1.setOutAnimation(leftOpen);

        adBanner2.setInAnimation(leftClose);
        adBanner2.setOutAnimation(leftOpen);

        adBanner3.setInAnimation(leftClose);
        adBanner3.setOutAnimation(leftOpen);

        adBanner1.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(getApplicationContext());
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT));
                return view;
            }
        });

        adBanner2.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(getApplicationContext());
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT));
                return view;
            }
        });

        adBanner3.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(getApplicationContext());
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                view.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT));
                return view;
            }
        });

        switcher1 = new PicassoImageSwitcher(getApplicationContext(), adBanner1);
        switcher2 = new PicassoImageSwitcher(getApplicationContext(), adBanner2);
        switcher3 = new PicassoImageSwitcher(getApplicationContext(), adBanner3);

    }
}
