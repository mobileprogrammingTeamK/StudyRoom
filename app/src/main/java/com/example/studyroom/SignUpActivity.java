package com.example.studyroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
    }

    public void onClickedRequest(View v) {

        EditText name_et = (EditText) findViewById(R.id.name_et);
        EditText num_et = (EditText) findViewById(R.id.num_et);
        EditText psw_et = (EditText) findViewById(R.id.psw_et);
        EditText checkpsw_et = (EditText) findViewById(R.id.checkpsw_et);
        EditText email_et = (EditText) findViewById(R.id.email_et);

        EditText[] editTexts = {name_et, num_et, psw_et, checkpsw_et, email_et};
        List<EditText> ErrorFields = new ArrayList<EditText>();

        for (EditText edit : editTexts) {
            if (TextUtils.isEmpty(edit.getText()))
                ErrorFields.add(edit);
        }
        for (int i = 0; i < ErrorFields.size(); i++) {
            EditText error = ErrorFields.get(i);
            error.setError("this field required");

            if (i == ErrorFields.size()-1)
                return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("가입을 요청하시겠습니까?");
        builder.setMessage("승인 완료시 이용 가능합니다.");
        builder.setNegativeButton("아니오", null);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String toast_message = "가입이 요청 되었습니다.";
                Toast toast = Toast.makeText(SignUpActivity.this, toast_message, Toast.LENGTH_LONG);
                toast.show();

                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /*
중복확인 > db와 대조하여 동일한 데이터가 있는지 확인
 */
    /*
    public void onClickedOverlap(View v) {

    }
     */
}

