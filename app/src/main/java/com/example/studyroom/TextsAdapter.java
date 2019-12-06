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





public class TextsAdapter extends FirebaseRecyclerAdapter<Texts, ViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<Texts> threadList;
    private List<Texts> arrayList;
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mFirebaseDatabase = database.getReference("posts");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    EditText txtTitle,txtText;
    CustomFilter mCustomFilter;
    List<Texts> list,backupList;
    private final ObservableSnapshotArray<Texts> mSnapshots;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_activity,parent,false);
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
        mSnapshots = options.getSnapshots();
        list = new ArrayList<>();
        backupList = new ArrayList<>();
        if (options.getOwner() != null) {
            options.getOwner().getLifecycle().addObserver(this);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Texts model) {
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startListening() {
        if (!mSnapshots.isListening(this)) {
            mSnapshots.addChangeEventListener(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stopListening() {
        mSnapshots.removeChangeEventListener(this);
        notifyDataSetChanged();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void cleanup(LifecycleOwner source) {
        source.getLifecycle().removeObserver(this);
    }

    @Override
    public void onChildChanged(ChangeEventType type,
                               DataSnapshot snapshot,
                               int newIndex,
                               int oldIndex) {
        Texts model = mSnapshots.get(newIndex);
        onChildUpdate(model, type, snapshot, newIndex, oldIndex);
    }

    protected void onChildUpdate(Texts model, ChangeEventType type,
                                 DataSnapshot snapshot,
                                 int newIndex,
                                 int oldIndex) {

        switch (type) {
            case ADDED:
                addItem(snapshot.getKey(), model);
                notifyItemInserted(newIndex);
                break;
            case CHANGED:
                addItem(snapshot.getKey(), model, newIndex);
                notifyItemChanged(newIndex);
                break;
            case REMOVED:
                removeItem(newIndex);
                notifyItemRemoved(newIndex);
                break;
            case MOVED:
                moveItem(snapshot.getKey(), model, newIndex, oldIndex);
                notifyItemMoved(oldIndex, newIndex);
                break;
            default:
                throw new IllegalStateException("Incomplete case statement");
        }
    }

    private void moveItem(String key, Texts t, int newIndex, int oldIndex) {
        list.remove(oldIndex);
        list.add(newIndex, t);
        if (true) {
            backupList.remove(oldIndex);
            backupList.add(newIndex, t);
        }
    }

    private void removeItem(int newIndex) {
        list.remove(newIndex);
        if (true)
            backupList.remove(newIndex);
    }

    private void addItem(String key, Texts t, int newIndex) {
        list.remove(newIndex);
        list.add(newIndex, t);
        if (true) {
            backupList.remove(newIndex);
            backupList.add(newIndex, t);
        }
    }

    private void addItem(String id, Texts t) {
        list.add(t);
        if (true)
            backupList.add(t);
    }

    @Override
    public void onDataChanged() {
    }

    @Override
    public void onError(DatabaseError error) {

    }

    @Override
    public ObservableSnapshotArray<Texts> getSnapshots() {
        return mSnapshots;
    }
    protected boolean filterCondition(Texts model, String filterPattern) {
        return true;
    }

    public void filter(String searchText){
        searchText = searchText.toLowerCase();
        threadList.clear();
        if(searchText.length() == 0){
            threadList.addAll(arrayList);
        }else {
            try {
            for(Texts posts : arrayList){
                if(posts.getPostTitle().toLowerCase().contains(searchText) || posts.getPostText().toLowerCase().contains(searchText)){
                    threadList.add(posts);
                }
            }}catch (Exception e){}
        }
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (mCustomFilter == null) {
            mCustomFilter = new CustomFilter();
        }
        return mCustomFilter;
    }


    public class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                results.values = backupList;
                results.count = backupList.size();
            } else {
                List<Texts> filteredList = new ArrayList<>();
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (Texts t : backupList) {
                    if(filterCondition(t,filterPattern))
                        filteredList.add(t);
                }
                results.values = filteredList;
                results.count = filteredList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends Texts>) results.values);
            notifyDataSetChanged();
        }
    }
}/*
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
*/