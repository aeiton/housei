package com.housey.aeiton.Utils;

import android.widget.TextView;

/**
 * Created by JAR on 19-06-2017.
 */

public class Rewards {

    String prize, cash, extra, rule;

    public Rewards(){

    }

    public Rewards(String r){
        rule = r;
    }

    public Rewards(String p, String c, String e){
        this.prize = p;
        this.cash = c;
        this.extra = e;
    }

    public String getPrize() {
        return prize;
    }

    public  String getCash() {
        return cash;
    }

    public String getExtra() {
        return extra;
    }

    public String getRule() {
        return rule;
    }
}

