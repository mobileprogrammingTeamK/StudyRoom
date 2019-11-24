package com.example.studyroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
    }


    /*
-Clicked signup button
dialog : 컴퓨터정보웅앵 학생만 이용이 가능합니다. 가입하시겠습니까? > 예/아니오(null)
            예 > intent로 가입창으로 이동
            아니오 > 이동x
 */
    public void onClickedSignup(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("가입하시겠습니까?");
        builder.setMessage("컴퓨터정보통신공학과 학생만 이용이 가능합니다.");
        builder.setNegativeButton("아니오", null);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*
-Clicked login button
if 회원 정보가 존재 > dialog : 로그인이 되었습니다. > 자리예약/분실물
                                자리예약 > intent로 자리창으로 이동
                                분실물 > intent로 분실물창으로 이동
else > dialog : 회원정보 오류입니다. 학번 혹은 비밀번호를 확인해주세요. > 닫기(null)
 */
    public void onClickedLogin(View v) {
//        if (checkLogin() == true) {
        Log.v("로그인","1");
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        Log.v("로그인","2");
            builder.setTitle("로그인이 되었습니다.");
            builder.setMessage("어느 창으로 이동하시겠습니까?");
            builder.setNegativeButton("분실물 게시판", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, MainBoard.class);
                    startActivity(intent);
                }

            });
        Log.v("로그인","3");
            builder.setPositiveButton("자리 예약", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, SeatStatusActivity.class);
                    startActivity(intent);
                }

            });
            AlertDialog dialog = builder.create();
            dialog.show();
//        }
//        else {
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("회원정보 오류입니다.");
//            builder.setMessage("학번 또는 비밀번호를 확인해주세요.");
//            builder.setPositiveButton("예", null);
//        }
    }

    public boolean checkLogin() {
        return true;
    }
}



