package com.pmdm.invasoresespaciales.game;

import java.util.ArrayList;
import java.util.Collections;

public class HighScore {

    private ArrayList<Score> list = new ArrayList<>();
    private int N = 5;

    public void putScore(Score score){
        list.add(score);
    }

    public String[] getHighScores(){
        Collections.sort(list);
        int n = (list.size()>=N)?N:list.size();
        String[] strs = new String[n];
        for(int i=0;i<n;i++){
            strs[i] = list.get(i).toString();
        }
        return strs;
    }

    public int getPosition(int points){
        Collections.sort(list);
        for(int i=0;i<list.size();i++){
            if (points>list.get(i).points) return ++i;
        }
        return list.size();
    }

}
