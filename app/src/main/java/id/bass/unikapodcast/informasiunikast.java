package id.bass.unikapodcast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class informasiunikast extends AppCompatActivity {

     RecyclerView Mrecyclerview;//
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasiunikast);


        Mrecyclerview = findViewById(R.id.recyclerviewaudio);
        Mrecyclerview.setHasFixedSize(true);
        Mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("audio");

    }
    private void firebaseSearch(String searchtext){
        String query = searchtext.toLowerCase();
        Query firebaseQuery = reference.orderByChild("search").startAt(query).endAt(query + "\uf8ff");
        FirebaseRecyclerOptions<memberunikainfo> options =
                new FirebaseRecyclerOptions.Builder<memberunikainfo>()
                        .setQuery(firebaseQuery, memberunikainfo.class)
                        .build();

        FirebaseRecyclerAdapter<memberunikainfo, ViewHolderinfounika> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<memberunikainfo, ViewHolderinfounika>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderinfounika holder, int position, @NonNull memberunikainfo model) {
                        holder.setAudio(getApplication(), model.getTitle(), model.getUrl());
                    }

                    @NonNull
                    @Override
                    public ViewHolderinfounika onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.row, parent, false);
                        return new ViewHolderinfounika(view);

                    }
                };
        firebaseRecyclerAdapter.startListening();
        Mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<memberunikainfo> options =
                new FirebaseRecyclerOptions.Builder<memberunikainfo>()
                        .setQuery(reference, memberunikainfo.class)
                        .build();

        FirebaseRecyclerAdapter<memberunikainfo, ViewHolderinfounika> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<memberunikainfo, ViewHolderinfounika>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderinfounika holder, int position, @NonNull memberunikainfo model) {
                        holder.setAudio(getApplication(), model.getTitle(), model.getUrl());
                    }

                    @NonNull
                    @Override
                    public ViewHolderinfounika onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.row, parent, false);
                        return new ViewHolderinfounika(view);

                    }
                };
        firebaseRecyclerAdapter.startListening();
        Mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    //tambahan



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_firebase);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
