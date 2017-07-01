package com.housey.aeiton.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAR on 19-03-2017.
 */

public class DataSingleton {

    public static int userId;

    public static String cardNo;

    public static String cardPattern;

    public static String cardId;

    public static String[] caller = {"number unknown", "number unknown"};

    public static String valid;

    public static String date;

    public static String response;

    public static String marquee;

    public static boolean isPlayed;

    public static ArrayList<HouseyNumber> card = new ArrayList<>();

    public static ArrayList<Integer> selectedNos = new ArrayList<>();

    public static ArrayList<String> adPaths = new ArrayList<>();

    public static ArrayList<Rewards> rewards = new ArrayList<>();

    public static ArrayList<Rewards> rule = new ArrayList<>();

}
