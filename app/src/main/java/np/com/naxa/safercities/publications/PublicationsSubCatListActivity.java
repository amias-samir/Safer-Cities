package np.com.naxa.safercities.publications;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.database.viewmodel.PublicationsListDetailsViewModel;
import np.com.naxa.safercities.network.UrlClass;
import np.com.naxa.safercities.network.retrofit.NetworkApiClient;
import np.com.naxa.safercities.network.retrofit.NetworkApiInterface;
import np.com.naxa.safercities.publications.adapter.PublicationSubcatListAdapter;
import np.com.naxa.safercities.publications.entity.PublicationsListDetails;
import np.com.naxa.safercities.publications.entity.PublicationsListResponse;
import np.com.naxa.safercities.utils.DialogFactory;
import np.com.naxa.safercities.utils.NetworkUtils;

public class PublicationsSubCatListActivity extends AppCompatActivity {

    private static final String TAG = "PublicationSubCat";
    @BindView(R.id.toolbar_general)
    Toolbar toolbarGeneral;
    @BindView(R.id.publicationSubcatListrecycler)
    RecyclerView publicationSubcatListrecycler;

    String fileType = "";


    NetworkApiInterface apiInterface;
    PublicationsListDetailsViewModel publicationsListDetailsViewModel;
    PublicationSubcatListAdapter publicationSubcatListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publications_sub_cat_list);
        ButterKnife.bind(this);

        apiInterface = NetworkApiClient.getAPIClient().create(NetworkApiInterface.class);
        publicationsListDetailsViewModel = ViewModelProviders.of(this).get(PublicationsListDetailsViewModel.class);

        setupToolBar(getIntent());
        setupListRecycler();


        if (NetworkUtils.isNetworkAvailable()) {
            fetchPublicationsDetails();
        } else {
            getDistinctNameList();
        }



    }

    private void setupToolBar(Intent intent) {
        if(intent != null){
            setSupportActionBar(toolbarGeneral);
            getSupportActionBar().setTitle(intent.getStringExtra("toolbar_title"));
            fileType = getFileType(intent.getStringExtra("title"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }else {
            setSupportActionBar(toolbarGeneral);
            getSupportActionBar().setTitle("Knowledge Base Files");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupListRecycler() {
        publicationSubcatListAdapter = new PublicationSubcatListAdapter(R.layout.hazard_list_item_row_layout, null, fileType);
        publicationSubcatListrecycler.setLayoutManager(new LinearLayoutManager(this));
        publicationSubcatListrecycler.setAdapter(publicationSubcatListAdapter);

    }

    private String getFileType (@NotNull String fileTitleType){
        String fileType = "";
        switch (fileTitleType){
            case "Audio":
//                fileType = "audio";
                fileType = "Audio";
                break;

            case "Video":
//                fileType = "video";
                fileType = "Video";
                break;

            case "Brochure":
//                fileType = "files";
//                fileType = "Brochure";
                fileType = "Brouchure";
                break;

            case "Documents":
//                fileType = "files";
                fileType = "Documents";
                break;
        }

        return fileType;
    }



    private void fetchPublicationsDetails() {
        Dialog dialog = DialogFactory.createProgressDialog(PublicationsSubCatListActivity.this, "Loading...");
        dialog.show();
        apiInterface.getPublicationsListDetailsResponse(UrlClass.API_ACCESS_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<PublicationsListResponse>() {
                    @Override
                    public void onNext(PublicationsListResponse publicationsListResponse) {
                        dialog.dismiss();
                        if (publicationsListResponse.getError() == 1) {
                            DialogFactory.createCustomErrorDialog(PublicationsSubCatListActivity.this, publicationsListResponse.getMessage(), new DialogFactory.CustomDialogListener() {
                                @Override
                                public void onClick() {

                                }
                            }).show();
                        }
                        if (publicationsListResponse.getError() == 0) {
                            if (publicationsListResponse.getData() == null) {
                                DialogFactory.createCustomErrorDialog(PublicationsSubCatListActivity.this, "No new data found", new DialogFactory.CustomDialogListener() {
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
                        DialogFactory.createCustomErrorDialog(PublicationsSubCatListActivity.this, e.getMessage(), new DialogFactory.CustomDialogListener() {
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
                        getDistinctNameList();
                    }
                });

    }

    private void getDistinctNameList() {
        publicationsListDetailsViewModel.getDistinctFilecategoryNameFromType(fileType)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {

                        List<String> stringList = new ArrayList<>();
                        for (String string : strings){
                            if(string != null){
                                stringList.add(string);
                            }
                        }
                        if(stringList != null) {
                            ((PublicationSubcatListAdapter) publicationSubcatListrecycler.getAdapter()).replaceData(stringList);
                        }

                        Log.d(TAG, "getDistinctCategoryCategoryList: " + strings.size());

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
