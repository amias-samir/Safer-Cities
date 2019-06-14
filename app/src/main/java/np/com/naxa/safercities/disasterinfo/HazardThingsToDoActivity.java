package np.com.naxa.safercities.disasterinfo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.beready.BeReadyDetails;
import np.com.naxa.safercities.beready.BeReadyInfoDetailsActivity;
import np.com.naxa.safercities.database.viewmodel.DisasterInfoDetailsViewModel;
import np.com.naxa.safercities.disasterinfo.imagesliderviewpager.ImageSliderViewPagerAdapter;
import np.com.naxa.safercities.disasterinfo.model.DisasterInfoDetailsEntity;
import np.com.naxa.safercities.utils.sectionmultiitemUtils.DataServer;
import np.com.naxa.safercities.utils.sectionmultiitemUtils.SectionMultipleItem;

import static android.text.Html.fromHtml;

public class HazardThingsToDoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_general)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btnBeforeHappens)
    Button btnBeforeHappens;
    @BindView(R.id.btnWhenHappens)
    Button btnWhenHappens;
    @BindView(R.id.btnAfterHappens)
    Button btnAfterHappens;

    //    HazardListModel hazardListModel;
    @BindView(R.id.tvThingsToDoDetails)
    TextView tvThingsToDoDetails;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.sliderLayout)
    LinearLayout sliderLayout;
    @BindView(R.id.btnPointsToConsider)
    Button btnPointsToConsider;
    @BindView(R.id.webViewThingsToDo)
    WebView web;


    private List<SectionMultipleItem> mData;
    private static final String TAG = "HazardThingsToDo";
    String category = "", subcatname = "";

    DisasterInfoDetailsViewModel disasterInfoDetailsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_things_to_do);
        ButterKnife.bind(this);

        disasterInfoDetailsViewModel = ViewModelProviders.of(this).get(DisasterInfoDetailsViewModel.class);

        Intent intent = getIntent();
        category = intent.getStringExtra("OBJ");
        subcatname = intent.getStringExtra("OBJ1");

        sliderLayout.setVisibility(View.GONE);

        setupToolBar(category);
        setThingsToDo(subcatname);


        // 1. create entityList which item data extend SectionMultiEntity
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mData = DataServer.getThingsToDoBefore();
//        setupRecyclerView();

    }

    private void setupToolBar(String category) {
        initButtonVisibilityStatus(category);
        setSupportActionBar(toolbar);
        if (category == null) {
            getSupportActionBar().setTitle("Things To Do");
        } else {
            getSupportActionBar().setTitle(category);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    private void initButtonVisibilityStatus(String category) {
        btnBeforeHappens.setVisibility(View.GONE);
        btnAfterHappens.setVisibility(View.GONE);
        btnWhenHappens.setVisibility(View.GONE);
        btnPointsToConsider.setVisibility(View.GONE);

        disasterInfoDetailsViewModel.getDisasterInfoDetailsByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<DisasterInfoDetailsEntity>>() {
                    @Override
                    public void onNext(List<DisasterInfoDetailsEntity> disasterInfoDetailsEntities) {

                        if(disasterInfoDetailsEntities != null) {
                            for (DisasterInfoDetailsEntity disasterInfoDetailsEntity : disasterInfoDetailsEntities) {
                                switch (disasterInfoDetailsEntity.getSubcatname()){
                                    case "पुर्व तयारी":
                                        btnBeforeHappens.setVisibility(View.VISIBLE);
                                        break;
                                    case "विपद् को समयमा":
                                        btnWhenHappens.setVisibility(View.VISIBLE);
                                        break;
                                    case "विपद् पश्चात":
                                        btnAfterHappens.setVisibility(View.VISIBLE);
                                        break;
                                    case "ध्यान दिनुपर्ने कुराहरु":
                                        btnPointsToConsider.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
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


    private void setupImageSliderViewPager() {

        String[] imageUrls = imageList.toArray(new String[0]);

//         String[] imageUrls = new String[]{
//                "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
//                "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
//                "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
//                "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
//                "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
//        };

//        String[] imageUrls = imageList.toArray(new String[imageList.size()]);

        ImageSliderViewPagerAdapter adapter = new ImageSliderViewPagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);


        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(imageUrls.length * density);


        sliderLayout.setVisibility(View.VISIBLE);
    }


    HazardListModel hazardListModel1 = new HazardListModel();

    @OnClick({R.id.btnBeforeHappens, R.id.btnWhenHappens, R.id.btnAfterHappens, R.id.btnPointsToConsider})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBeforeHappens:
                setThingsToDo("पुर्व तयारी");
                break;
            case R.id.btnWhenHappens:
                setThingsToDo("विपद् को समयमा");
                break;
            case R.id.btnAfterHappens:
                setThingsToDo("विपद् पश्चात");
                break;
            case R.id.btnPointsToConsider:
                setThingsToDo("ध्यान दिनुपर्ने कुराहरु");
                break;
        }
    }

    DataServer dataServer = new DataServer();

    private void setThingsToDo(String when) {
        tvThingsToDoDetails.setVisibility(View.VISIBLE);
        web.setVisibility(View.GONE);
        tvThingsToDoDetails.setText("No Data Found.");



        if (when != null) {
            switch (when) {
                case "पुर्व तयारी":
                    getSupportActionBar().setTitle("पुर्व तयारी");
                    break;

                case "विपद् को समयमा":
                    getSupportActionBar().setTitle( "विपद् को समयमा");
                    break;

                case "विपद् पश्चात":
                    getSupportActionBar().setTitle("विपद् पश्चात");
                    break;

                case "ध्यान दिनुपर्ने कुराहरु":
                    getSupportActionBar().setTitle("ध्यान दिनुपर्ने कुराहरु" );
                    break;
            }
        }


        disasterInfoDetailsViewModel.getSpecificDisasterInfo(category, when)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<DisasterInfoDetailsEntity>() {
                    @Override
                    public void onNext(DisasterInfoDetailsEntity disasterInfoDetailsEntity) {
                        imageList = new ArrayList<String>();
                        if (TextUtils.isEmpty(disasterInfoDetailsEntity.getDesc())) {
                            tvThingsToDoDetails.setText("No Data Found.");
                            web.setVisibility(View.GONE);
                            tvThingsToDoDetails.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onNext: Empty" + disasterInfoDetailsEntity.getDesc());

                        } else {
                            setupWebView(disasterInfoDetailsEntity);
                            web.setVisibility(View.VISIBLE);
                            tvThingsToDoDetails.setVisibility(View.GONE);
                            Log.d(TAG, "onNext: "+disasterInfoDetailsEntity.getDesc());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                tvThingsToDoDetails.setText(fromHtml(disasterInfoDetailsEntity.getDesc(), 0, new ImageGetter(), null), TextView.BufferType.SPANNABLE);

                                if (imageList != null && imageList.size() > 0) {
                                    Log.d(TAG, "onComplete: Image list " + imageList.size());
                                    setupImageSliderViewPager();
                                } else {
                                    sliderLayout.setVisibility(View.GONE);
                                }

                                imageList = null;

                            } else {
                                tvThingsToDoDetails.setText(fromHtml(disasterInfoDetailsEntity.getDesc()), TextView.BufferType.SPANNABLE);

                            }
                        }

                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: Desc " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    List<String> imageList;

    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;
            if (!source.equals("")) {
//                id = R.drawable.hughjackman;
                imageList.add(source);
                Log.d(TAG, "getDrawable: " + imageList.size() + " " + source);
            } else {
                return null;
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    private void setupWebView(@NotNull DisasterInfoDetailsEntity disasterInfoDetailsEntity) {
        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadDataWithBaseURL(null, disasterInfoDetailsEntity.getDesc(),"text/html", "utf-8", null);
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
