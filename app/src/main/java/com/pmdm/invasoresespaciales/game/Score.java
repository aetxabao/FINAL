package com.pmdm.invasoresespaciales.game;

public class Score  implements Comparable<Score>{

    public String name = "AAA";
    public int level = 0;
    public int points = 0;

    public Score(String name, int level, int points) {
        this.name = name;
        this.level = level;
        this.points = points;
    }

    public Score(String str){
        try {
            String[] fields = str.trim().split("\\s+");
            name = fields[0];
            level = Integer.parseInt(fields[1]);
            points = Integer.parseInt(fields[2]);
        }catch (Exception e){}
    }

    @Override
    public String toString(){// AAA   00   00000
        String str = " " + name.toUpperCase();
        str += "   " + String.format("%02d", level);
        str += "  " + String.format("%05d", points) + " ";
        return str;
    }

    @Override
    public int compareTo(Score another) {
        int x = 0;
        if (this.points==another.points){
            if (this.level==another.level){
                x = this.name.compareTo(another.name);
            }else{
                x = this.level - another.level;
            }
        }else{
            x = this.points - another.points;
        }
        return -1*x;
    }

}
