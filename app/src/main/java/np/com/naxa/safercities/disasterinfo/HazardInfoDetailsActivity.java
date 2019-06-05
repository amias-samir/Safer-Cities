package np.com.naxa.safercities.disasterinfo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.database.viewmodel.DisasterInfoDetailsViewModel;
import np.com.naxa.safercities.disasterinfo.model.DisasterInfoDetailsEntity;
import np.com.naxa.safercities.quiz.quiznew.McqQuizTestActivity;
import np.com.naxa.safercities.utils.sectionmultiitemUtils.DataServer;

import static android.text.Html.fromHtml;

public class HazardInfoDetailsActivity extends AppCompatActivity {

    private static final String TAG = "HazardInfoDetails";
    @BindView(R.id.toolbar_general)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tvBody)
    TextView tvBody;
    @BindView(R.id.btnPlayQuiz)
    Button btnPlayQuiz;
    @BindView(R.id.btnBeforeHappens)
    Button btnBeforeHappens;
    @BindView(R.id.btnWhenHappens)
    Button btnWhenHappens;
    @BindView(R.id.btnAfterHappens)
    Button btnAfterHappens;

    HazardListModel hazardListModel;
    String category = "";

    DisasterInfoDetailsViewModel disasterInfoDetailsViewModel;
    @BindView(R.id.btnPointsToConsider)
    Button btnPointsToConsider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazard_info_details);
        ButterKnife.bind(this);
        disasterInfoDetailsViewModel = ViewModelProviders.of(this).get(DisasterInfoDetailsViewModel.class);

        Intent intent = getIntent();
        if (intent != null) {
            category = intent.getStringExtra("OBJ");

            initUI(category);

            initButtonVisibilityStatus(category);
        }
        setupToolBar();
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

                                    case "before":
                                        btnBeforeHappens.setVisibility(View.VISIBLE);
                                    break;
                                    case "during":
                                        btnWhenHappens.setVisibility(View.VISIBLE);
                                        break;
                                    case "after":
                                        btnAfterHappens.setVisibility(View.VISIBLE);
                                        break;
                                    case "points to consider":
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

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        if (category == null) {
            getSupportActionBar().setTitle("Hazard Details");
        } else {
            getSupportActionBar().setTitle(category);
            btnBeforeHappens.setText("Before " + category);

            tvTitle.setText(category);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    DataServer dataServer = new DataServer();
    HazardListModel hazardListModel1 = new HazardListModel();

    private void initUI(String category) {
        tvBody.setText("No Data Found");
        imageView.setVisibility(View.GONE);
        disasterInfoDetailsViewModel.getSpecificDisasterInfo(category, "introduction")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<DisasterInfoDetailsEntity>() {
                    @Override
                    public void onNext(DisasterInfoDetailsEntity disasterInfoDetailsEntity) {

                        if (TextUtils.isEmpty(disasterInfoDetailsEntity.getPhoto())) {
                            imageView.setVisibility(View.GONE);
                        } else {
                            imageView.setVisibility(View.VISIBLE);
                            WindowManager mWinMgr = (WindowManager) HazardInfoDetailsActivity.this.getSystemService(Context.WINDOW_SERVICE);
                            int displayWidth = mWinMgr.getDefaultDisplay().getWidth();
                            Glide.with(HazardInfoDetailsActivity.this)
                                    .load(disasterInfoDetailsEntity.getPhoto())
                                    .override(displayWidth, 200)
                                    .into(imageView);
                        }

                        if (TextUtils.isEmpty(disasterInfoDetailsEntity.getDesc())) {
                            tvBody.setText("No Data Found");

                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                tvBody.setText(fromHtml(disasterInfoDetailsEntity.getDesc(), 0, new ImageGetter(), null));
                            } else {
                                tvBody.setText(fromHtml(disasterInfoDetailsEntity.getDesc()));
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

    List<String> imageList = new ArrayList<String>();

    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;
            if (!source.equals("")) {
//                id = R.drawable.hughjackman;
                imageList.add(source);
                Log.d(TAG, "getDrawable: " + imageList.size());
            } else {
                return null;
            }

//            Drawable d = getResources().getDrawable(id);
//            d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
//            return d;
            return null;
        }
    }

    ;


    @OnClick({R.id.btnPlayQuiz, R.id.btnBeforeHappens, R.id.btnWhenHappens, R.id.btnAfterHappens, R.id.btnPointsToConsider})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnPlayQuiz:
                startActivity(new Intent(HazardInfoDetailsActivity.this, McqQuizTestActivity.class));
                break;
            case R.id.btnBeforeHappens:
                startNewActivity("before");
                break;
            case R.id.btnWhenHappens:
                startNewActivity("during");
                break;
            case R.id.btnAfterHappens:
                startNewActivity("after");
                break;
            case R.id.btnPointsToConsider:
                startNewActivity("points to consider");
                break;
        }
    }

    public void startNewActivity(String subcatname) {
        Intent intent = new Intent(HazardInfoDetailsActivity.this, HazardThingsToDoActivity.class);
        intent.putExtra("OBJ", category);
        intent.putExtra("OBJ1", subcatname);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
