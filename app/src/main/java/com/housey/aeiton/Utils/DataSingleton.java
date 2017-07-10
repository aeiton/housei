package com.housey.aeiton.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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

    public static Queue<String> adPaths = new LinkedList<>();

    public static ArrayList<Rewards> rewards = new ArrayList<>();

    public static ArrayList<Rewards> rule = new ArrayList<>();

    public static ArrayList<String> adUrls = new ArrayList<>();
}
