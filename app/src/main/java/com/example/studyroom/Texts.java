package com.example.studyroom;


public class Texts {
    private String threadTitle;
    private String threadText;
    private int userId;

    public Texts(String threadTitle,String threadText, int userId) {
        this.threadText = threadText;
        this.threadTitle= threadTitle;
        this.userId = userId;

    }
    public String getThreadTitle(){
        return threadTitle;
    }
    public String getThreadText(){
        return threadText;
    }
    public int getUserId(){return userId;}
    public void setThreadTitle(String threadTitle){
        this.threadTitle = threadTitle;
    }
    public void setThreadText(String threadText){
        this.threadText = threadText;
    }
    public void setUserId(int userId){ this.userId = userId; }
}
