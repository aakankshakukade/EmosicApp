package com.example.gauri.projectsem1;

/**
 * Created by GAURI on 14-11-2017.
 */

public class urls {
    int counter=0;
    int id;
    String mood;
    String songname;
    String songurl;
    public urls(){   }
    public urls(int id, String mood, String songname,String songurl){
        this.id = id;
        this.songname =songname;
        this.songurl =songurl;
    }

    public urls(String mood, String songname, String songurl){
        this.mood = mood;
        this.songname = songname;
        this.songurl=songurl;
    }
    public int getID(){
        return this.id;

    }

    public void setID(urls url){
        url.id= ++counter;
    }

    public String getsongname(){
        return this.songname;
    }

    public void setsongname(String name){
        this.songname = name;
    }

    public String getsongurl(){
        return this.songurl;
    }

    public void setsongurl(String songurl ){
        this.songurl =songurl;
    }

    public String getsongmood(){
        return this.mood;
    }

    public void setsongmood(String mood){
        this.mood = mood;
    }

}