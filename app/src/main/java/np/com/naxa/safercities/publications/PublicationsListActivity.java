package np.com.naxa.safercities.publications;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.database.viewmodel.PublicationsListDetailsViewModel;
import np.com.naxa.safercities.event.PublicationListItemEvent;
import np.com.naxa.safercities.network.UrlClass;
import np.com.naxa.safercities.network.retrofit.NetworkApiClient;
import np.com.naxa.safercities.network.retrofit.NetworkApiInterface;
import np.com.naxa.safercities.publications.adapter.PublicationsListItemAdapter;
import np.com.naxa.safercities.publications.entity.PublicationsListDetails;
import np.com.naxa.safercities.publications.entity.PublicationsListResponse;
import np.com.naxa.safercities.publications.youtubeplayer.helper.YoutubeConstants;
import np.com.naxa.safercities.utils.DialogFactory;
import np.com.naxa.safercities.utils.NetworkUtils;
import np.com.naxa.safercities.utils.ToastUtils;

public class PublicationsListActivity extends AppCompatActivity {

    private static final String TAG = "PublicationsList";
    @BindView(R.id.toolbar_general)
    Toolbar toolbar;
    @BindView(R.id.recyclerViewPublicationList)
    RecyclerView recyclerViewPublicationList;

    NetworkApiInterface apiInterface;
    PublicationsListDetailsViewModel publicationsListDetailsViewModel;
    @BindView(R.id.fab_filter)
    FloatingActionButton fabFilter;

    List<String> categoryName;
    List<String> subCategoryName;


    String fileType = "";
    String fileCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publications_list);
        ButterKnife.bind(this);

        apiInterface = NetworkApiClient.getAPIClient().create(NetworkApiInterface.class);
        publicationsListDetailsViewModel = ViewModelProviders.of(this).get(PublicationsListDetailsViewModel.class);

        setupToolBar(getIntent());
        setupListRecycler();

//        if (NetworkUtils.isNetworkAvailable()) {
//            fetchPublicationsDetails();
//        } else {
//            getDataFromDatabase();
//            getDistinctNameList();
//        }

    }

    private void setupToolBar(Intent intent) {
        if(intent != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(intent.getStringExtra("title"));
//            fileType = getFileType(intent.getStringExtra("title"));
            fileType = intent.getStringExtra("type");
            fileCategory = intent.getStringExtra("title");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Log.e(TAG, "setupToolBar: "+ fileType + " , "+fileCategory);
            getAllCatSubCatFIlteredDataFromDatabase("All", fileType, fileCategory);

        }else {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Multimedia Files");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupListRecycler() {
        PublicationsListItemAdapter publicationsListItemAdapter = new PublicationsListItemAdapter(R.layout.publications_list_item_row_layout, null);
        recyclerViewPublicationList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPublicationList.setAdapter(publicationsListItemAdapter);

    }

    private void fetchPublicationsDetails() {
        Dialog dialog = DialogFactory.createProgressDialog(PublicationsListActivity.this, "Loading...");
        dialog.show();
        apiInterface.getPublicationsListDetailsResponse(UrlClass.API_ACCESS_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<PublicationsListResponse>() {
                    @Override
                    public void onNext(PublicationsListResponse publicationsListResponse) {
                        dialog.dismiss();
                        if (publicationsListResponse.getError() == 1) {
                            DialogFactory.createCustomErrorDialog(PublicationsListActivity.this, publicationsListResponse.getMessage(), new DialogFactory.CustomDialogListener() {
                                @Override
                                public void onClick() {

                                }
                            }).show();
                        }
                        if (publicationsListResponse.getError() == 0) {
                            if (publicationsListResponse.getData() == null) {
                                DialogFactory.createCustomErrorDialog(PublicationsListActivity.this, "No new data found", new DialogFactory.CustomDialogListener() {
                                    @Override
                                    public void onClick() {

                                    }
                                }).show();
                            } else {
                                savePublicationSetails(publicationsListResponse.getData());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        DialogFactory.createCustomErrorDialog(PublicationsListActivity.this, e.getMessage(), new DialogFactory.CustomDialogListener() {
                            @Override
                            public void onClick() {

                            }
                        }).show();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });


    }


    private void savePublicationSetails(@NonNull List<PublicationsListDetails> publicationsListDetailsList) {

        Log.d(TAG, "savePublicationSetails: " + publicationsListDetailsList.size());

        Observable.just(publicationsListDetailsList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<List<PublicationsListDetails>, List<PublicationsListDetails>>() {
                    @Override
                    public List<PublicationsListDetails> apply(List<PublicationsListDetails> publicationsListDetails) throws Exception {
                        return publicationsListDetails;
                    }
                })
                .map(new Function<PublicationsListDetails, PublicationsListDetails>() {
                    @Override
                    public PublicationsListDetails apply(PublicationsListDetails publicationsListDetails) throws Exception {
                        return publicationsListDetails;
                    }
                })
                .subscribe(new DisposableObserver<PublicationsListDetails>() {
                    @Override
                    public void onNext(PublicationsListDetails publicationsListDetails) {

                        Log.d(TAG, "onNext: " + publicationsListDetails.getHazard_name());
                        publicationsListDetailsViewModel.insert(publicationsListDetails);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

//                        getDataFromDatabase();
                        getAllCatSubCatFIlteredDataFromDatabase("All", fileType, fileCategory);
                        getDistinctNameList();
                    }
                });

    }

    
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRVItemClick(PublicationListItemEvent.PublicationListitemClick itemClick) {
        String name = itemClick.getPublicationsListDetails().getTitle();

        Intent intent = new Intent(PublicationsListActivity.this, PublicationDetailsActivity.class);
        intent.putExtra(YoutubeConstants.VIDEO_KEY, itemClick.getPublicationsListDetails());
        startActivity(intent);

        ToastUtils.showShortToast(name);
    }



    @OnClick(R.id.fab_filter)
    public void onViewClicked() {
        final Dialog dialog = new Dialog(PublicationsListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.inventory_filter_dialog_layout);


        TextView text = (TextView) dialog.findViewById(R.id.tv_message);
        Spinner spnCategory = (Spinner) dialog.findViewById(R.id.spn_category_type);
        Spinner spnSubCategory = (Spinner) dialog.findViewById(R.id.spn_sub_category_type);
        dialogSpinnerSetup(spnCategory, spnSubCategory);


        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btnSearch = (Button) dialog.findViewById(R.id.btn_search);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                getAllCatSubCatFIlteredDataFromDatabase(spnCategory.getSelectedItem().toString(), fileType, fileCategory);
            }
        });

        dialog.show();
    }

    ArrayAdapter<String> nameAdapter, typeAdapter;
    private void dialogSpinnerSetup(Spinner spnCategory, Spinner spnSubCategory){
        if(categoryName != null) {
            nameAdapter = new ArrayAdapter<String>(PublicationsListActivity.this,
                    R.layout.item_spinner, categoryName);
            nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCategory.setAdapter(nameAdapter);

            spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here

                    publicationsListDetailsViewModel.getDistinctTypeLIstFromName(spnCategory.getSelectedItem().toString())
                            .observeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableSubscriber<List<String>>() {
                                @Override
                                public void onNext(List<String> strings) {
                                    subCategoryName = new ArrayList<String>();
                                    subCategoryName.add("All");
                                    for (int i = 0 ; i<strings.size() ; i++){
                                        if(strings.get(i)!= null){
                                            subCategoryName.add(strings.get(i));

                                        }
                                    }
                                    Log.d(TAG, "getDistinctCategoryList: " + subCategoryName.size());
                                    if(subCategoryName != null) {
                                        typeAdapter = new ArrayAdapter<String>(PublicationsListActivity.this,
                                                R.layout.item_spinner, subCategoryName);
                                        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spnSubCategory.setAdapter(typeAdapter);
                                    }
                                }



                                @Override
                                public void onError(Throwable t) {

                                }

                                @Override
                                public void onComplete() {


                                }
                            });

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        }

    }

    private void getAllCatSubCatFIlteredDataFromDatabase(String category, String fileType, String subCategory) {

        Log.d(TAG, "getAllCatSubCatFIlteredDataFromDatabase: "+category +" , "+subCategory);

//        publicationsListDetailsViewModel.getNameTypeWiseList(category,fileType, subCategory)
        publicationsListDetailsViewModel.getNameTypeWiseList(category,fileType, subCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<PublicationsListDetails>>() {
                    @Override
                    public void onNext(List<PublicationsListDetails> publicationsListDetails) {
                        ((PublicationsListItemAdapter) recyclerViewPublicationList.getAdapter()).replaceData(publicationsListDetails);
                        Log.d(TAG, "getAllCatSubCatFIlteredDataFromDatabase size: " + publicationsListDetails.size());

                        getDistinctNameList();

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void getDistinctNameList() {
        publicationsListDetailsViewModel.getDistinctNameList()
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {
                        categoryName = new ArrayList<String>();
                        categoryName.add("All");
                        for (int i = 0 ; i<strings.size() ; i++){
                            if(strings.get(i)!= null){
                                categoryName.add(strings.get(i));

                            }
                        }
                        Log.d(TAG, "getDistinctCategoryCategoryList: " + categoryName.size());

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }



    private String getFileType (@NotNull String fileTitleType){

        String fileType = "";
        switch (fileTitleType){
            case "Audio":
                fileType = "audio";
                break;

            case "Video":
                fileType = "video";
                break;

            case "Brochure":
                fileType = "files";
                break;

            case "Documents":
                fileType = "files";
                break;
        }

        return fileType;
    }


}
