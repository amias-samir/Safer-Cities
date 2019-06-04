package np.com.naxa.safercities.beready;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.disasterinfo.HazardInfoActivity;
import np.com.naxa.safercities.network.UrlClass;
import np.com.naxa.safercities.network.retrofit.NetworkApiClient;
import np.com.naxa.safercities.network.retrofit.NetworkApiInterface;
import np.com.naxa.safercities.utils.DialogFactory;
import np.com.naxa.safercities.utils.NetworkUtils;
import np.com.naxa.safercities.utils.SharedPreferenceUtils;

public class BeReadyInfoDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_general)
    Toolbar toolbar;
    @BindView(R.id.webViewBeReady)
    WebView webViewBeReady;

    NetworkApiInterface apiInterface;
    SharedPreferenceUtils sharedPreferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_ready_info_details);
        ButterKnife.bind(this);
        apiInterface = NetworkApiClient.getAPIClient().create(NetworkApiInterface.class);
        sharedPreferenceUtils = new SharedPreferenceUtils(this);

        setupToolBar(getIntent());

        fetchReadyData();

    }

    private void setupToolBar(Intent intent) {
        setSupportActionBar(toolbar);
        if(intent == null) {
            getSupportActionBar().setTitle("Hazard Info");
        }else {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void fetchReadyData() {

        if(NetworkUtils.isNetworkAvailable()){
            if(sharedPreferenceUtils.getBoolanValue(SharedPreferenceUtils.IS_BE_READY_DATA_EXISTS, false)){
                fetchFromSharedPrefs();
            }else {
                fetchFromServer();
            }
        }else {
            fetchFromSharedPrefs();
        }
    }

    private void fetchFromSharedPrefs() {
    }

    private void fetchFromServer() {
        Dialog dialog = DialogFactory.createProgressDialog(BeReadyInfoDetailsActivity.this, "Loading...");
        dialog.show();
        apiInterface.getBeReadyResponse(UrlClass.API_ACCESS_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(5)
                .subscribe(new DisposableObserver<BeReadyResponse>() {
                    @Override
                    public void onNext(BeReadyResponse beReadyResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
