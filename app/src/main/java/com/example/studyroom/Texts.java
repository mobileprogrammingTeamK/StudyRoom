package com.example.studyroom;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

public class Texts {

    private String postId;
    private String postTitle;
    private String postText;
    private String userId;




    public Texts(){

    }
    public Texts(String postTitle,String postText, String userId) {
        this.userId = userId;
        this.postTitle= postTitle;
        this.postText = postText;

    }


    public String getPostId(){return postId;}
    public String getPostTitle(){
        return postTitle;
    }
    public String getPostText(){
        return postText;
    }
    public String getUserId(){return userId;}



    public void setThreadTitle(String threadTitle){
        this.postTitle = threadTitle;
    }
    public void setThreadText(String threadText){
        this.postText = threadText;
    }
    public void setUserId(String userId){ this.userId = userId; }
    public void setPostId(String postId){this.postId = postId;}


}
