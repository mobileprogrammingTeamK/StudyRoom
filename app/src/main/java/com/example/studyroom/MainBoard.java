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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;



public class MainBoard extends AppCompatActivity {

    EditText txtTitle, txtText;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("posts");
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter recyclerAdapter;
    String currentUser;
    PostAdapter adapter;
    FirebaseRecyclerOptions<Texts> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_main);

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

    private void getData() {
        options = new FirebaseRecyclerOptions.Builder<Texts>().setQuery(mFirebaseDatabase, new SnapshotParser<Texts>() {
            @NonNull
            @Override
            public Texts parseSnapshot(DataSnapshot snapshot) {
                return new Texts(snapshot.child("postTitle").getValue().toString(), snapshot.child("postText").getValue().toString(), snapshot.child("userId").getValue().toString());
            }
        }).build();
        /*recyclerAdapter = new FirebaseRecyclerAdapter<Texts,ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final Texts model) {
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
                                            Toast.makeText(MainBoard.this,"Post Edited",Toast.LENGTH_SHORT).show();
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
        };*/
        //adapter = new TextsAdapter(options);

        adapter = new PostAdapter(options);
        adapter.setClickListener(new PostAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, Texts texts, int position) {

            }

            @Override
            public void editButtonClick(final String postId, final Texts texts, final int position) {
                final AlertDialog alertDialog = dialogCreater("Edit",texts);
                Button editBtn = alertDialog.findViewById(R.id.createBtn);
                Button cancelBtn = alertDialog.findViewById(R.id.cancelBtn);
                editBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.VISIBLE);
                editBtn.setText("Edit");
                Log.v("test",postId);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Texts post = new Texts(txtTitle.getText().toString(),txtText.getText().toString(),texts.getUserId());
                        recyclerView.getRecycledViewPool().clear();
                        adapter.notifyDataSetChanged();
                        mFirebaseDatabase.child(postId).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainBoard.this,"Post Edited",Toast.LENGTH_SHORT).show();
                            }
                        });

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

            @Override
            public void deleteButtonClick(String postId, Texts texts, int position) {

            }
        });
        //recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setAdapter(adapter);
    }

    public AlertDialog dialogCreater(String dialogTitle, Texts data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainBoard.this);
        LayoutInflater inflater = (MainBoard.this).getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.dialog_layout, null);
        txtTitle = dialog.findViewById(R.id.editTxtTitle);
        txtText = dialog.findViewById(R.id.editTxtText);
        txtTitle.setText(data.getPostTitle());
        txtText.setText(data.getPostText());
        builder.setTitle(dialogTitle);
        if (dialogTitle.equals("Read")) {
            txtTitle.setFocusable(false);
            txtText.setFocusable(false);
        }
        builder.setView(dialog);
        return builder.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //recyclerAdapter.stopListening();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //recyclerAdapter.startListening();
        adapter.startListening();
    }


    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainBoard.this);
        LayoutInflater inflater = (MainBoard.this).getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.dialog_layout, null);
        txtTitle = dialog.findViewById(R.id.editTxtTitle);
        txtText = dialog.findViewById(R.id.editTxtText);
        builder.setTitle("Create");
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.show();
        Button createBtn = alertDialog.findViewById(R.id.createBtn);
        createBtn.setVisibility(View.VISIBLE);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postTitle = txtTitle.getText().toString();
                String postText = txtText.getText().toString();

                currentUser = auth.getCurrentUser().getUid();
                String postId = mFirebaseDatabase.push().getKey();
                Texts post = new Texts(postTitle, postText, currentUser);
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
        final List<Texts> list = new ArrayList<>();
        list.addAll(options.getSnapshots());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TextView emptyText = findViewById(R.id.emptyView);
                adapter.getFilter().filter(newText);
                if (adapter.getItemCount() != 0 || newText.length() == 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}


