package np.com.naxa.safercities.report;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.database.entity.ReportDetailsEntity;
import np.com.naxa.safercities.database.viewmodel.ReportDetailsViewModel;

public class SavedFormListActivity extends AppCompatActivity {


    private static final String TAG = "SavedFormListActivity";
    ReportDetailsViewModel reportDetailsViewModel;
    @BindView(R.id.toolbar_general)
    Toolbar toolbar;
    @BindView(R.id.recyclerViewSavedFormList)
    RecyclerView recyclerViewSavedFormList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_form_list);
        ButterKnife.bind(this);

        reportDetailsViewModel = ViewModelProviders.of(this).get(ReportDetailsViewModel.class);

        setupToolBar();

        setupListRecycler();
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Saved Forms");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupListRecycler() {
        ReportSavedFormListAdapter reportSavedFormListAdapter = new ReportSavedFormListAdapter(R.layout.saved_forms_list_row_item_layout, null);
        recyclerViewSavedFormList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSavedFormList.setAdapter(reportSavedFormListAdapter);

        getSAvedFormList();
    }

    private void getSAvedFormList() {

        reportDetailsViewModel.getAllReportDetailsList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSubscriber<List<ReportDetailsEntity>>() {
                    @Override
                    public void onNext(List<ReportDetailsEntity> reportDetailsEntities) {
                        Gson gson = new Gson();
//                        String json = gson.toJson(reportDetailsEntities.get(0));
//                        Log.d(TAG, "onNext: insert" + json);

                        ((ReportSavedFormListAdapter) recyclerViewSavedFormList.getAdapter()).replaceData(reportDetailsEntities);

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
