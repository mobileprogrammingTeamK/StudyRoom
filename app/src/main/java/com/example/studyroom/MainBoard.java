package com.example.studyroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyroom.R;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MainBoard extends AppCompatActivity {

    EditText txtTitle,txtText;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("posts");
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter recyclerAdapter;
    String currentUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_main);
        /*
        listView = findViewById(R.id.listView);
        adapter = new TextsAdapter(this,textsList);
        listView.setAdapter(adapter);
        mFirebaseDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Texts value = dataSnapshot.getValue(Texts.class);
                String key = dataSnapshot.getKey();
                Log.v("keys",key);
                textsList.add(value);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Texts value = dataSnapshot.getValue(Texts.class);
                String key = dataSnapshot.getKey();
                textsList.add(value);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Texts value = dataSnapshot.getValue(Texts.class);
                String key = dataSnapshot.getKey();
                for(Texts text : textsList){
                    if(!text.getPostId().equals(key))
                        textsList.add(text);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogView(position,"Read",false);
            }
        });


        TextView emptyText = findViewById(R.id.emptyView);
        listView.setEmptyView(emptyText);
*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView = findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        getData();



    }
    private void getData(){
        Query query = FirebaseDatabase.getInstance().getReference().child("posts");

        FirebaseRecyclerOptions<Texts> options = new FirebaseRecyclerOptions.Builder<Texts>().setQuery(query, new SnapshotParser<Texts>() {
            @NonNull
            @Override
            public Texts parseSnapshot(DataSnapshot snapshot){
                return new Texts(snapshot.child("postTitle").getValue().toString(),snapshot.child("postText").getValue().toString(),snapshot.child("userId").getValue().toString());
            }
        }).build();

        recyclerAdapter = new FirebaseRecyclerAdapter<Texts,ViewHolder>(options) {
        //recyclerAdapter = new TextsAdapter(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final Texts model) {
                holder.setPostTitle(model.getPostTitle());
                holder.setPostText(model.getPostText());
                final String postId = this.getSnapshots().getSnapshot(position).getKey();

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainBoard.this,"Post Deleted",Toast.LENGTH_SHORT).show();
                    }
                });
                currentUser = auth.getCurrentUser().getUid();
                if(currentUser.equals(model.getUserId())){
                    holder.editBtn.setVisibility(View.VISIBLE);
                    holder.editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainBoard.this);
                            LayoutInflater inflater = (MainBoard.this).getLayoutInflater();
                            View dialog = inflater.inflate(R.layout.dialog_layout,null);
                            txtTitle = dialog.findViewById(R.id.editTxtTitle);
                            txtText = dialog.findViewById(R.id.editTxtText);

                            builder.setTitle("Edit");
                            builder.setView(dialog);
                            txtTitle.setText(model.getPostTitle());
                            txtText.setText(model.getPostText());
                            final AlertDialog alertDialog = builder.show();
                            Button editBtn = alertDialog.findViewById(R.id.createBtn);
                            Button cancelBtn = alertDialog.findViewById(R.id.cancelBtn);
                            editBtn.setVisibility(View.VISIBLE);
                            cancelBtn.setVisibility(View.VISIBLE);
                            editBtn.setText("Edit");
                            editBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Texts post = new Texts(txtTitle.getText().toString(),txtText.getText().toString(),currentUser);
                                    mFirebaseDatabase.child(postId).setValue(post);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainBoard.this);
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
                                    Toast.makeText(MainBoard.this,"Post Deleted",Toast.LENGTH_SHORT).show();
                                    mFirebaseDatabase.child(postId).removeValue();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_activity,parent,false);
                return new ViewHolder(view);

            }
        };
        recyclerView.setAdapter(recyclerAdapter);
    }
    public AlertDialog DialogCreater(String dialogTitle){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainBoard.this);
        LayoutInflater inflater = (MainBoard.this).getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.dialog_layout,null);
        txtTitle = dialog.findViewById(R.id.editTxtTitle);
        txtText = dialog.findViewById(R.id.editTxtText);
        builder.setTitle(dialogTitle);
        builder.setView(dialog);
        return builder.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }



    public void onClick(View v){
        final AlertDialog alertDialog = DialogCreater("Create");
        Button createBtn = alertDialog.findViewById(R.id.createBtn);
        createBtn.setVisibility(View.VISIBLE);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postTitle = txtTitle.getText().toString();
                String postText = txtText.getText().toString();

                currentUser = auth.getCurrentUser().getUid();
                String postId = mFirebaseDatabase.push().getKey();
                Texts post = new Texts(postTitle,postText,currentUser);
                mFirebaseDatabase.child(postId).setValue(post);
                alertDialog.dismiss();
            }
        });
        Button cancelBtn = alertDialog.findViewById(R.id.cancelBtn);
        cancelBtn.setVisibility(View.VISIBLE);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
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

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
