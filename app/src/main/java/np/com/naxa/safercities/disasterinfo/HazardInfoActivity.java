package np.com.naxa.safercities.disasterinfo;

import android.app.Dialog;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;

import com.arlib.floatingsearchview.FloatingSearchView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.database.viewmodel.DisasterInfoDetailsViewModel;
import np.com.naxa.safercities.disasterinfo.model.DisasterInfoDetailsEntity;
import np.com.naxa.safercities.disasterinfo.model.DisasterInfoListResponse;
import np.com.naxa.safercities.network.UrlClass;
import np.com.naxa.safercities.network.retrofit.NetworkApiClient;
import np.com.naxa.safercities.network.retrofit.NetworkApiInterface;
import np.com.naxa.safercities.utils.DialogFactory;

public class HazardInfoActivity extends AppCompatActivity {

    private static final String TAG = "HazardInfoActivity";
    @BindView(R.id.toolbar_general)
    Toolbar toolbar;
    @BindView(R.id.floating_search_hazards)
    FloatingSearchView floatingSearchHazards;
    @BindView(R.id.hazardListrecycler)
    RecyclerView hazardListrecycler;

    NetworkApiInterface apiInterface;
    DisasterInfoDetailsViewModel disasterInfoDetailsViewModel;
    @BindView(R.id.search_hazard)
    SearchView searchHazard;

    HazardListAdapter hazardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_info);
        ButterKnife.bind(this);

        apiInterface = NetworkApiClient.getAPIClient().create(NetworkApiInterface.class);
        disasterInfoDetailsViewModel = ViewModelProviders.of(this).get(DisasterInfoDetailsViewModel.class);
        setupToolBar();
        setupListRecycler();

        getDRRData();
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("विपद् जोखिम जानकारी");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    private void setupListRecycler() {
        hazardListAdapter = new HazardListAdapter(R.layout.hazard_list_item_row_layout, null);
        hazardListrecycler.setLayoutManager(new LinearLayoutManager(this));
        hazardListrecycler.setAdapter(hazardListAdapter);

        loadDataToList();
    }

    private void getDRRData() {
        Dialog dialog = DialogFactory.createProgressDialog(HazardInfoActivity.this, "Loading...");
        dialog.show();
        apiInterface.getDisasterInfoResponse(UrlClass.API_ACCESS_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<DisasterInfoListResponse, Iterable<DisasterInfoDetailsEntity>>() {
                    @Override
                    public Iterable<DisasterInfoDetailsEntity> apply(DisasterInfoListResponse disasterInfoListResponse) throws Exception {
                        if (disasterInfoListResponse.getError() == 0) {
                            return disasterInfoListResponse.getData();

                        } else {
                            DialogFactory.createCustomErrorDialog(HazardInfoActivity.this, disasterInfoListResponse.getMessage(), new DialogFactory.CustomDialogListener() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            }).show();
                            return null;
                        }
                    }
                })
                .map(new Function<DisasterInfoDetailsEntity, DisasterInfoDetailsEntity>() {
                    @Override
                    public DisasterInfoDetailsEntity apply(DisasterInfoDetailsEntity disasterInfoDetailsEntity) throws Exception {
                        return disasterInfoDetailsEntity;
                    }
                })
                .subscribe(new DisposableObserver<DisasterInfoDetailsEntity>() {
                    @Override
                    public void onNext(DisasterInfoDetailsEntity disasterInfoDetailsEntity) {
                        if (disasterInfoDetailsEntity != null) {
                            long id = disasterInfoDetailsViewModel.insert(disasterInfoDetailsEntity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    private void loadDataToList() {

        disasterInfoDetailsViewModel.getAllDistinctCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {
//                        Log.d(TAG, "onNext: "+strings.size());
                        ((HazardListAdapter) hazardListrecycler.getAdapter()).replaceData(strings);

                        setupSearchView(strings);

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setupSearchView(List<String> stringList) {
        // Associate searchable configuration with the SearchView
        hazardListAdapter = new HazardListAdapter(R.layout.hazard_list_item_row_layout, stringList);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchHazard.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchHazard.setMaxWidth(Integer.MAX_VALUE);

        searchHazard.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {

                if (TextUtils.isEmpty(query)) {
                    setupFilterListView(hazardListAdapter);
                } else {
                    // filter recycler view when query submitted
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 0.55s = 500ms
                            hazardListAdapter.getFilter().filter(query);

                            if (hazardListAdapter != null) {
                                setupFilterListView(hazardListAdapter);
                            }
                        }
                    }, 500);


                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                // filter recycler view when text is changed
//                mAdapterFiltered.getFilter().filter(query);
                if (TextUtils.isEmpty(query)) {
                    setupFilterListView(hazardListAdapter);
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 0.55s = 500ms
                            hazardListAdapter.getFilter().filter(query);
                            if (hazardListAdapter != null) {
                                setupFilterListView(hazardListAdapter);
                            }
                        }
                    }, 500);
                }
                return false;
            }
        });

    }

    private void setupFilterListView ( HazardListAdapter hazardListAdapter){
        hazardListrecycler.setLayoutManager(new LinearLayoutManager(this));
        hazardListrecycler.setAdapter(hazardListAdapter);
        hazardListAdapter.notifyDataSetChanged();
    }
}
