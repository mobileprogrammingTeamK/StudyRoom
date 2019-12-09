package com.example.studyroom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyroom.R;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


//Not gonna use this adapter





public class TextsAdapter extends FirebaseRecyclerAdapter<Texts, ViewHolder> {
    private Context mContext;
    private ArrayList<Texts> threadList;
    private List<Texts> arrayList;
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabase = database.getReference("posts");
    FirebaseRecyclerOptions<Texts> options;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    EditText txtTitle,txtText;
    List<Texts> list,backupList;
    private ObservableSnapshotArray<Texts> mSnapshots;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_activity,parent,false);
        mContext = parent.getContext();
        return new ViewHolder(view);
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


    public TextsAdapter(@NonNull FirebaseRecyclerOptions<Texts> options) {
        super(options);
        this.options = options;
        mSnapshots = options.getSnapshots();
        backupList = new ArrayList<>();

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Texts model) {
        holder.setPostTitle(model.getPostTitle());
        holder.setPostText(model.getPostText());
        final String postId = this.getSnapshots().getSnapshot(position).getKey();
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCreater("Read",model);

            }
        });
        currentUser = auth.getCurrentUser().getUid();
        if(currentUser.equals(model.getUserId())){
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final AlertDialog alertDialog = dialogCreater("Edit",model);
                    Button editBtn = alertDialog.findViewById(R.id.createBtn);
                    Button cancelBtn = alertDialog.findViewById(R.id.cancelBtn);
                    editBtn.setVisibility(View.VISIBLE);
                    cancelBtn.setVisibility(View.VISIBLE);
                    editBtn.setText("Edit");
                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Texts post = new Texts(txtTitle.getText().toString(),txtText.getText().toString(),currentUser);
                            mFirebaseDatabase.child(postId).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(mContext,"Post Edited",Toast.LENGTH_SHORT).show();
                                }
                            });
                            notifyDataSetChanged();
                            alertDialog.dismiss();
                        }
                    });
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
            });
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
                            Toast.makeText(mContext,"Post Deleted",Toast.LENGTH_SHORT).show();
                            mFirebaseDatabase.child(postId).removeValue();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    public void filter(String searchText){
        searchText = searchText.toLowerCase();
        mSnapshots = options.getSnapshots();
        list.addAll(mSnapshots);
        mSnapshots.clear();
        if(searchText.length() == 0){
            mSnapshots.addAll(list);
        }else {
            try {
            for(Texts posts : list){
                if(posts.getPostTitle().toLowerCase().contains(searchText) || posts.getPostText().toLowerCase().contains(searchText)){
                    mSnapshots.add(posts);

                }
            }}catch (Exception e){}
        }
        notifyDataSetChanged();
    }
    public AlertDialog dialogCreater(String dialogTitle,Texts data){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View dialog = inflater.inflate(R.layout.dialog_layout,null);
        txtTitle = dialog.findViewById(R.id.editTxtTitle);
        txtText = dialog.findViewById(R.id.editTxtText);
        txtTitle.setText(data.getPostTitle());
        txtText.setText(data.getPostText());
        builder.setTitle(dialogTitle);
        if(dialogTitle.equals("Read")){
            txtTitle.setFocusable(false);
            txtText.setFocusable(false);
        }
        builder.setView(dialog);
        return builder.show();

    }


}
class ViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout root;
    public TextView postTitle;
    public TextView postText;
    ImageButton editBtn,deleteBtn;

    public ViewHolder(View itemView){
        super(itemView);
        root = itemView.findViewById(R.id.rootLayout);
        postTitle = itemView.findViewById(R.id.threadTitle);
        postText = itemView.findViewById(R.id.threadText);
        editBtn = itemView.findViewById(R.id.editBtn);
        deleteBtn = itemView.findViewById(R.id.deleteBtn);
    }
    public void setPostTitle(String title){
        postTitle.setText(title);
    }
    public void setPostText(String text){ postText.setText(text);}
}
