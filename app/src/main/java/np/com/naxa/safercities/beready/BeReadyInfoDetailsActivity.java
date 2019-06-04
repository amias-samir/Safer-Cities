package np.com.naxa.safercities.beready;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.calendar.CalendarActivity;
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
    WebView web;

    NetworkApiInterface apiInterface;
    SharedPreferenceUtils sharedPreferenceUtils;
    Gson gson;

    private String typeSlug = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_ready_info_details);
        ButterKnife.bind(this);
        apiInterface = NetworkApiClient.getAPIClient().create(NetworkApiInterface.class);
        gson = new Gson();
        sharedPreferenceUtils = new SharedPreferenceUtils(this);


        setupToolBar(getIntent());

        fetchReadyData();

    }

    private void setupToolBar(Intent intent) {
        setSupportActionBar(toolbar);
        if (intent == null) {
            getSupportActionBar().setTitle("Be Ready");
        } else {
            typeSlug = intent.getStringExtra("title");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void fetchReadyData() {

        if (NetworkUtils.isNetworkAvailable()) {
            if (sharedPreferenceUtils.getBoolanValue(SharedPreferenceUtils.IS_BE_READY_DATA_EXISTS, false)) {
                fetchFromSharedPrefs();
            } else {
                fetchFromServer();
            }
        } else {
            fetchFromSharedPrefs();
        }
    }

    private void fetchFromSharedPrefs() {

        BeReadyResponse beReadyResponse = gson.fromJson(sharedPreferenceUtils.getStringValue(SharedPreferenceUtils.KEY_BE_READY_DATA, null), BeReadyResponse.class);
        Observable.just(beReadyResponse.getData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<BeReadyDetails>>() {
                    @Override
                    public void onNext(List<BeReadyDetails> beReadyDetailsList) {

                        for (BeReadyDetails beReadyDetails : beReadyDetailsList){
                            if(beReadyDetails.getSlug().equals(typeSlug)){
                                getSupportActionBar().setTitle(beReadyDetails.getName());
                                setupWebView(beReadyDetails);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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

                        if(beReadyResponse.getError() != 0) {
                            DialogFactory.createCustomErrorDialog(BeReadyInfoDetailsActivity.this, beReadyResponse.getMessage(), new DialogFactory.CustomDialogListener() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            }).show();
                        }else {
                            sharedPreferenceUtils.setValue(SharedPreferenceUtils.IS_BE_READY_DATA_EXISTS, true);
                            sharedPreferenceUtils.setValue(SharedPreferenceUtils.KEY_BE_READY_DATA, gson.toJson(beReadyResponse));
                            fetchFromSharedPrefs();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogFactory.createCustomErrorDialog(BeReadyInfoDetailsActivity.this, e.getMessage(), new DialogFactory.CustomDialogListener() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        }).show();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }



    private void setupWebView(BeReadyDetails beReadyDetails) {
        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadDataWithBaseURL(null,beReadyDetails.getDescription(),"text/html", "utf-8", null);
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
