package com.nic.thooimaikaavalar_old.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.nic.thooimaikaavalar_old.R;
import com.nic.thooimaikaavalar_old.adapter.AdditionalListAdapter;
import com.nic.thooimaikaavalar_old.constant.AppConstant;
import com.nic.thooimaikaavalar_old.dataBase.DBHelper;
import com.nic.thooimaikaavalar_old.dataBase.dbData;
import com.nic.thooimaikaavalar_old.databinding.ActivityAdditionalListBinding;
import com.nic.thooimaikaavalar_old.model.RealTimeMonitoringSystem;
import com.nic.thooimaikaavalar_old.session.PrefManager;
import com.nic.thooimaikaavalar_old.support.MyDividerItemDecoration;

import java.util.ArrayList;

public class AdditionalWorkScreen extends AppCompatActivity implements View.OnClickListener{
    private ActivityAdditionalListBinding activityAdditionalListBinding;
    private ShimmerRecyclerView recyclerView;
    private AdditionalListAdapter additionalListAdapter;
    public dbData dbData = new dbData(this);
    private SearchView searchView;
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    private String work_id;
    ArrayList<RealTimeMonitoringSystem> additionalList = new ArrayList<>();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdditionalListBinding = DataBindingUtil.setContentView(this, R.layout.activity_additional_list);
        activityAdditionalListBinding.setActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefManager = new PrefManager(this);
        setSupportActionBar(activityAdditionalListBinding.toolbar);
        initRecyclerView();
    }

    private void initRecyclerView() {
        work_id = getIntent().getStringExtra(AppConstant.WORK_ID);
        activityAdditionalListBinding.workList.setVisibility(View.VISIBLE);
        recyclerView = activityAdditionalListBinding.workList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        additionalListAdapter = new AdditionalListAdapter(AdditionalWorkScreen.this, additionalList,dbData);
        recyclerView.setAdapter(additionalListAdapter);

        new fetchAdditionaltask().execute();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new fetchScheduletask().execute();
//            }
//        }, 2000);
    }

    @SuppressLint("StaticFieldLeak")
    public class fetchAdditionaltask extends AsyncTask<Void, Void,
            ArrayList<RealTimeMonitoringSystem>> {
        @Override
        protected ArrayList<RealTimeMonitoringSystem> doInBackground(Void... params) {
            dbData.open();
            additionalList = new ArrayList<>();
            additionalList = dbData.getAllAdditionalWork(work_id,prefManager.getFinancialyearName(),prefManager.getDistrictCode(),prefManager.getBlockCode(),prefManager.getPvCode(),prefManager.getKeySpinnerSelectedSchemeSeqId());
            Log.d("ADDITIONAL_COUNT", String.valueOf(additionalList.size()));
            return additionalList;
        }

        @Override
        protected void onPostExecute(ArrayList<RealTimeMonitoringSystem> additionalList) {
            super.onPostExecute(additionalList);
            additionalListAdapter = new AdditionalListAdapter(AdditionalWorkScreen.this, additionalList,dbData);
            recyclerView.setAdapter(additionalListAdapter);
            recyclerView.showShimmerAdapter();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCards();
                }
            }, 2000);
        }

        private void loadCards() {

            recyclerView.hideShimmerAdapter();
        }
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

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                additionalListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                additionalListAdapter.getFilter().filter(query);
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


    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        additionalListAdapter.notifyDataSetChanged();
    }
}

