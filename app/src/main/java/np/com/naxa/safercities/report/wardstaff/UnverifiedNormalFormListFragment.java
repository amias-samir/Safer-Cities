package np.com.naxa.safercities.report.wardstaff;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.database.entity.ReportDetailsEntity;
import np.com.naxa.safercities.database.viewmodel.ReportDetailsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnverifiedNormalFormListFragment extends Fragment {

    private static final String TAG = "UnverifiedFormList";

    ReportDetailsViewModel reportDetailsViewModel;
//    @BindView(R.id.recyclerViewUnverifiedNormalFormList)
    RecyclerView recyclerViewUnverifiedNormalFormList;
//    Unbinder unbinder;


    public UnverifiedNormalFormListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unverified_normal_forms, container, false);
        reportDetailsViewModel = ViewModelProviders.of(this).get(ReportDetailsViewModel.class);


        setupRecycleView(view);
        // Inflate the layout for this fragment
//        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void setupRecycleView(View view) {
        recyclerViewUnverifiedNormalFormList = (RecyclerView) view.findViewById(R.id.recyclerViewUnverifiedNormalFormList);
        UnverifiedNormalListAdapter unverifiedNormalListAdapter = new UnverifiedNormalListAdapter(R.layout.saved_forms_list_row_item_layout, null);
        recyclerViewUnverifiedNormalFormList.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerViewUnverifiedNormalFormList.setAdapter(unverifiedNormalListAdapter);

        getUnverifiedNormalFormList();
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }

    private void getUnverifiedNormalFormList() {

        reportDetailsViewModel.getAllUnVerifiedReportDetailsList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSubscriber<List<ReportDetailsEntity>>() {
                    @Override
                    public void onNext(List<ReportDetailsEntity> reportDetailsEntities) {
//                        Gson gson = new Gson();
//                        String json = gson.toJson(reportDetailsEntities.get(0));
//                        Log.d(TAG, "onNext: insert" + json);

                        ((UnverifiedNormalListAdapter) recyclerViewUnverifiedNormalFormList.getAdapter()).replaceData(reportDetailsEntities);

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
