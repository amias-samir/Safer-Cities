package np.com.naxa.safercities.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.network.UrlClass;
import np.com.naxa.safercities.network.retrofit.NetworkApiClient;
import np.com.naxa.safercities.network.retrofit.NetworkApiInterface;
import np.com.naxa.safercities.quiz.entity.QuizCategory;
import np.com.naxa.safercities.quiz.model.QuizCategoryResponse;
import np.com.naxa.safercities.utils.NetworkUtils;
import np.com.naxa.safercities.utils.SharedPreferenceUtils;
import np.com.naxa.safercities.utils.recycleviewutils.LinearLayoutManagerWithSmoothScroller;
import np.com.naxa.safercities.utils.recycleviewutils.RecyclerViewType;

public class QuizHomeActivity extends AppCompatActivity {

    private static final String TAG = "QuizHomeActivity";

    @BindView(R.id.toolbar_general)
    Toolbar toolbar;
    @BindView(R.id.txtLBL)
    TextView txtLBL;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RecyclerViewType recyclerViewType;

    NetworkApiInterface apiInterface;
    SharedPreferenceUtils sharedPreferenceUtils;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);
        ButterKnife.bind(this);

        sharedPreferenceUtils = new SharedPreferenceUtils(this);
        gson = new Gson();


        apiInterface = NetworkApiClient.getAPIClient().create(NetworkApiInterface.class);

        setupToolBar();
        setUpRecyclerView();

        fetchQuizCategory();
    }


    private void setupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("हाजिरीजवाफ खेल्नुहोस्");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //setup recycler view

    private void setUpRecyclerView() {
        recyclerViewType = RecyclerViewType.GRID;

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManagerWithSmoothScroller(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        populateRecyclerView();
    }
    //populate recycler view

    private void populateRecyclerView() {
        ArrayList<SectionQuizModel> sectionModelArrayList = new ArrayList<SectionQuizModel>();

        sectionModelArrayList.addAll(SectionQuizModel.getQuizList());
        SectionRecyclerViewQuizAdapter adapter = new SectionRecyclerViewQuizAdapter(this, recyclerViewType, sectionModelArrayList);
        recyclerView.setAdapter(adapter);
    }



    private void fetchQuizCategory() {

        if(NetworkUtils.isNetworkAvailable()){
            fetchQuizCategoryFromServer();
        }else {
            fetchQuizCategoryFromLocal();
        }

    }

    private void fetchQuizCategoryFromServer() {
        apiInterface.getQuizcategoryResponse(UrlClass.API_ACCESS_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<QuizCategoryResponse, ObservableSource<List<QuizCategory>>>() {
                    @Override
                    public ObservableSource<List<QuizCategory>> apply(QuizCategoryResponse quizCategoryResponse) throws Exception {
                        return Observable.just(quizCategoryResponse.getData());
                    }
                })
                .flatMapIterable(new Function<List<QuizCategory>, Iterable<QuizCategory>>() {
                    @Override
                    public Iterable<QuizCategory> apply(List<QuizCategory> quizCategories) throws Exception {
                        return quizCategories;
                    }
                })
                .map(new Function<QuizCategory, QuizCategory>() {
                    @Override
                    public QuizCategory apply(QuizCategory quizCategory) throws Exception {
                        return quizCategory;
                    }
                })
                .subscribe(new DisposableObserver<QuizCategory>() {
                    @Override
                    public void onNext(QuizCategory quizCategory) {
                        Log.d(TAG, "onNext: "+quizCategory.getName());
                        Log.d(TAG, "onNext: "+quizCategory.getDescription());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void fetchQuizCategoryFromLocal() {
    }
}
