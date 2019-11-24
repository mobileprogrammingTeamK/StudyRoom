package com.example.studyroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;

import com.example.studyroom.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainBoard extends AppCompatActivity {

    TextsAdapter adapter;
    public static ArrayList<Texts> textsList = new ArrayList<>();
    public static int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_main);
        ListView listView = findViewById(R.id.listView);
        String[] threadTitle = {"Hello!","Hello2","Hello3","TEst4"};
        String[] threadText = {"Hello1","Test test","Test Test","Hello Test4"};
        int[] userIds = {2001, 2002,2003,2002};
        studentId = 2002;
        textsList = new ArrayList<>();
        for(int i =0; i<userIds.length; i++){
            textsList.add(new Texts(threadTitle[i],threadText[i],userIds[i]));
        }
        adapter = new TextsAdapter(this,textsList);
        listView.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogCreater(position,"Read",false);
            }
        });


        TextView emptyText = findViewById(R.id.emptyView);
        listView.setEmptyView(emptyText);

    }

    public void DialogCreater(int position,String dialogTitle,boolean editTextFocus){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainBoard.this);
        LayoutInflater inflater = (MainBoard.this).getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog_layout,null);
        builder.setTitle(dialogTitle);
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.show();
        if(!editTextFocus){
            EditText txtTitle = dialog.findViewById(R.id.editTxtTitle);
            EditText txtText = dialog.findViewById(R.id.editTxtText);
            txtTitle.setText(textsList.get(position).getThreadTitle());
            txtTitle.setFocusable(editTextFocus);
            txtText.setText(textsList.get(position).getThreadText());
            txtText.setFocusable(editTextFocus);
        }else {
            Button createBtn = dialog.findViewById(R.id.createBtn);
            createBtn.setVisibility(View.VISIBLE);
            Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
            cancelBtn.setVisibility(View.VISIBLE);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
    }

    public void onClick(View v){
        DialogCreater(0,"New Post",true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem search = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
