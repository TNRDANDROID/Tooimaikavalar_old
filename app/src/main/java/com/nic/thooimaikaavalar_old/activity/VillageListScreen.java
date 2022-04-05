package com.nic.thooimaikaavalar_old.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.VillageListAdapter;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.VillageListActivityBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;

import java.util.ArrayList;

public class VillageListScreen extends AppCompatActivity implements View.OnClickListener {
    private VillageListActivityBinding villageListActivityBinding;
    private ShimmerRecyclerView recyclerView;
    private VillageListAdapter villageListAdapter;
    public dbData dbData = new dbData(this);
    private SearchView searchView;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        villageListActivityBinding = DataBindingUtil.setContentView(this, R.layout.village_list_activity);
        villageListActivityBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(villageListActivityBinding.toolbar);
        intializeUI();
        initRecyclerView();
    }
    private void initRecyclerView() {
        recyclerView = villageListActivityBinding.villageList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new fetchScheduletask().execute();
            }
        },2000);
    }

    public class fetchScheduletask extends AsyncTask<Void, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(Void... params) {
            dbData.open();
            ArrayList<RealTimeMonitoringSystem> villageList = new ArrayList<>();
            villageList = dbData.getAll_Village();
            Log.d("VILLAGE_COUNT", String.valueOf(villageList.size()));
            return villageList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealTimeMonitoringSystem> villageList) {
            super.onPostExecute(villageList);
            villageListAdapter = new VillageListAdapter(VillageListScreen.this, villageList);
            recyclerView.setAdapter(villageListAdapter);
            recyclerView.showShimmerAdapter();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCards();
                }
            }, 1000);
        }
    }

    private void loadCards() {

        recyclerView.hideShimmerAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        int id2 = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_button", null, null);
        int id3 = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        ImageView img = (ImageView) searchView.findViewById(id2);
        ImageView close = (ImageView) searchView.findViewById(id3);
        img.setColorFilter(Color.WHITE);
        close.setColorFilter(Color.WHITE);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(this.getResources().getColor(R.color.grey_6));



        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                villageListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                villageListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
    public void intializeUI(){

    }

    @Override
    public void onClick(View view) {

    }
}
