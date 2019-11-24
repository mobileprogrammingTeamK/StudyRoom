package com.example.studyroom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.studyroom.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TextsAdapter extends ArrayAdapter<Texts> {
    private Context mContext;
    private ArrayList<Texts> threadList = new ArrayList<>();



    public TextsAdapter(Context context, ArrayList<Texts> list){
        super(context,0,list);
        mContext = context;
        this.threadList = list;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Nullable
    @Override
    public Texts getItem(int position) {
        return super.getItem(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.listview_activity,parent,false);
        Texts currentTexts = threadList.get(position);
        TextView title = listItem.findViewById(R.id.threadTitle);
        title.setText(currentTexts.getThreadTitle());

        TextView text = listItem.findViewById(R.id.threadText);
        text.setText(currentTexts.getThreadText());
        final int pos = position;
        if(MainBoard.studentId == currentTexts.getUserId()){
            ImageButton editButton = listItem.findViewById(R.id.editBtn);
            editButton.setVisibility(View.VISIBLE);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    View dialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout,null);
                    builder.setTitle("Edit");
                    builder.setView(dialog);
                    EditText txtTitle = dialog.findViewById(R.id.editTxtTitle);
                    EditText txtText = dialog.findViewById(R.id.editTxtText);
                    txtTitle.setText(threadList.get(pos).getThreadTitle());
                    txtText.setText(threadList.get(pos).getThreadText());
                    final AlertDialog alertDialog = builder.show();
                    Button createBtn = dialog.findViewById(R.id.createBtn);
                    createBtn.setVisibility(View.VISIBLE);
                    createBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //database testss
                        }
                    });
                    Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
                    cancelBtn.setVisibility(View.VISIBLE);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
            });
            ImageButton deleteButton = listItem.findViewById(R.id.deleteBtn);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete Post");
                    builder.setMessage("Are you sure delete this post?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(),"Post Deleted",Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        return listItem;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    public void filter(String searchText){
        searchText = searchText.toLowerCase();
        threadList = MainBoard.textsList;
        MainBoard.textsList.clear();
        if(searchText.length() == 0){
            MainBoard.textsList.addAll(this.threadList);
        }else {
            try {
            for(Texts posts : threadList){
                if(posts.getThreadTitle().toLowerCase().contains(searchText) || posts.getThreadText().toLowerCase().contains(searchText)){
                    MainBoard.textsList.add(posts);
                }
            }}catch (Exception e){}
        }
        notifyDataSetChanged();
    }
}
