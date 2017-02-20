package com.example.timofey.myapplication;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by timofey on 20.02.2017.
 */

public class NoteColors {


    public static Integer getColor(int importance){

        Map<Integer, Integer> colors = new HashMap<>();
        colors.put(0, R.color.white);
        colors.put(1, R.color.green);
        colors.put(2, R.color.yellow);
        colors.put(3, R.color.red);

        return colors.get(importance);
    }

    public static Integer getHeaderColor(int importance){

        Map<Integer, Integer> colors = new HashMap<>();
        colors.put(0, R.color.gray_head);
        colors.put(1, R.color.green_head);
        colors.put(2, R.color.yellow_head);
        colors.put(3, R.color.red_head);

        return colors.get(importance);
    }

}
