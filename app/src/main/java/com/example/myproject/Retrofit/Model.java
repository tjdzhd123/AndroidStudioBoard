package com.example.myproject.Retrofit;


import java.sql.Timestamp;

public class Model {
    private int seq;
    private String title;
    private String content;
    private String id_frt;
    private Timestamp dt_frt;
    private String id_lst;
    private Timestamp dt_lst;
    private String dt;
    private String file_path;
    private String nickname;


    public int getSeq() {
        return seq;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getId_frt() {
        return id_frt;
    }

    public String getNickname() {return nickname;}

    public Timestamp getDt_frt() {
        return dt_frt;
    }

    public String getId_lst() {
        return id_lst;
    }

    public Timestamp getDt_lst() {
        return dt_lst;
    }

    public String getDt() {return dt;}

    public String getFile_path(){ return file_path;}
}
