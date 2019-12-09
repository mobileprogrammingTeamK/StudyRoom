package com.example.studyroom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostAdapter extends FirebaseRecyclerAdapter<Texts, PostAdapter.PostViewHolder> {
    Context mContext;
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabase = database.getReference("posts");


    EditText txtTitle,txtText;
    private ClickListener mClickListener;

    public interface ClickListener {
        public void onItemClick(View v,Texts texts, int position);
        void editButtonClick(String postId, Texts texts, int position);
        void deleteButtonClick(String postId, Texts texts, int position);
    }
    public void setClickListener(ClickListener listener){
        mClickListener =listener;
    }
    public PostAdapter(FirebaseRecyclerOptions<Texts> options){
        super(options,true);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_activity,parent,false);
        mContext = parent.getContext();
        return new PostViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(final PostViewHolder holder, int position, final Texts model) {
        holder.setPostTitle(model.getPostTitle());
        holder.setPostText(model.getPostText());
        //final String postId = this.getSnapshots().getSnapshot(position).getKey();
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCreater("Read",model);

            }
        });
        currentUser = auth.getCurrentUser().getUid();
        if(currentUser.equals(model.getUserId())){
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.bind(model);
         /*   holder.editBtn.setOnClickListener(new ClickListener() {
                @Override
                public void onItemClick(View v,int position) {


                    final AlertDialog alertDialog = dialogCreater("Edit",model);
                    Button etBtn = alertDialog.findViewById(R.id.createBtn);
                    Button cancelBtn = alertDialog.findViewById(R.id.cancelBtn);
                    etBtn.setVisibility(View.VISIBLE);
                    cancelBtn.setVisibility(View.VISIBLE);
                    etBtn.setText("Edit");
                    etBtn.setOnClickListener(new View.OnClickListener() {
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
            });*/
            holder.deleteBtn.setVisibility(View.VISIBLE);
        }else {
            holder.editBtn.setVisibility(View.INVISIBLE);
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onChildUpdate(Texts model, ChangeEventType type, DataSnapshot snapshot, int newIndex, int oldIndex) {
        model.setPostId(snapshot.getKey());
        super.onChildUpdate(model, type, snapshot, newIndex, oldIndex);
    }

    @Override
    protected boolean filterCondition(Texts model, String filterPattern) {
        return model.getPostTitle().toLowerCase().contains(filterPattern) || model.getPostText().toLowerCase().contains(filterPattern);
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

    class PostViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView postTitle;
        public TextView postText;
        ImageButton editBtn,deleteBtn;
        ViewHolder view;

        public PostViewHolder(View itemView){
            super(itemView);

            root = itemView.findViewById(R.id.rootLayout);
            postTitle = itemView.findViewById(R.id.threadTitle);
            postText = itemView.findViewById(R.id.threadText);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);


        }
        public void bind(final Texts model){
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.editButtonClick(model.getPostId(),model,getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Texts model = getItem(pos);
                    mClickListener.deleteButtonClick(model.getPostId(),model,pos);
                    notifyDataSetChanged();
                }
            });
        }


        public void setPostTitle(String title){
            postTitle.setText(title);
        }
        public void setPostText(String text){ postText.setText(text);}
    }
}
