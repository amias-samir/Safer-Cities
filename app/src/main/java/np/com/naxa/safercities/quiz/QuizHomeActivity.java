package np.com.naxa.safercities.quiz;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
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
import np.com.naxa.safercities.quiz.model.QuizQuestionAnswerDetail;
import np.com.naxa.safercities.utils.DialogFactory;
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
    Dialog dialog;

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

    }
    //populate recycler view

    private void populateRecyclerView( List<QuizCategory> quizCategoryList) {
        ArrayList<SectionQuizModel> sectionModelArrayList = new ArrayList<SectionQuizModel>();

//        sectionModelArrayList.addAll(quizCategoryList);
        SectionRecyclerViewQuizAdapter adapter = new SectionRecyclerViewQuizAdapter(this, recyclerViewType, quizCategoryList);
        recyclerView.setAdapter(adapter);
    }



    private void fetchQuizCategory() {

        dialog = DialogFactory.createProgressDialog(QuizHomeActivity.this, "Loading...");
        dialog.show();

        if(NetworkUtils.isNetworkAvailable()){
            fetchQuizCategoryFromServer();
        }else {
            fetchQuizCategoryFromLocal();
        }

    }

    private void fetchQuizCategoryFromServer() {
        final String[] quizId = {""};
        final String[] quizSlug = {""};
        apiInterface.getQuizcategoryResponse(UrlClass.API_ACCESS_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<QuizCategoryResponse, ObservableSource<List<QuizCategory>>>() {
                    @Override
                    public ObservableSource<List<QuizCategory>> apply(QuizCategoryResponse quizCategoryResponse) throws Exception {
                        if(quizCategoryResponse.getError() == 1){
                            showDialog(quizCategoryResponse.getMessage());
//                            return  null;
                            throw new Exception(quizCategoryResponse.getMessage());
                        }else {
                            return Observable.just(quizCategoryResponse.getData());
                        }
                    }
                })
                .flatMapIterable(new Function<List<QuizCategory>, Iterable<QuizCategory>>() {
                    @Override
                    public Iterable<QuizCategory> apply(List<QuizCategory> quizCategories) throws Exception {
                        Log.d(TAG, "apply:  categories size "+quizCategories.size()  );
                        if(quizCategories == null){
//                            showDialog("कुनै डाटा भेटिएन");
//                            return  null;
                            throw new Exception("कुनै डाटा भेटिएन");
                        }else {
                            sharedPreferenceUtils.setValue(SharedPreferenceUtils.KEY_QUIZ_CATEGORY_LIST, gson.toJson(quizCategories));
                            return quizCategories;
                        }
                    }
                })
                .map(new Function<QuizCategory, QuizCategory>() {
                    @Override
                    public QuizCategory apply(QuizCategory quizCategory) throws Exception {
                        Log.d(TAG, "apply: category "+ quizCategory.getDescription());
                        return quizCategory;
                    }
                })
                .subscribe(new DisposableObserver<QuizCategory>() {
                    @Override
                    public void onNext(QuizCategory quizCategory) {
                        quizId[0] = quizCategory.getId();
                        quizSlug[0] = quizCategory.getSlug();

                        fetchQuizQuestionAnswerDetails(quizCategory, quizId[0], quizSlug[0]);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showDialog(e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        dialog.dismiss();
                        fetchQuizCategoryFromLocal();
                    }
                });
    }

    private void fetchQuizQuestionAnswerDetails(@NotNull QuizCategory quizCategory, String quizID, String quizSlug) {
        apiInterface.getQuestionAnswerDetailsResponse(UrlClass.API_ACCESS_TOKEN, quizCategory.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<QuizQuestionAnswerDetail>>() {
                    @Override
                    public void onNext(List<QuizQuestionAnswerDetail> quizQuestionAnswerDetails) {

                        if(quizQuestionAnswerDetails != null){
                            sharedPreferenceUtils.setValue(quizID, gson.toJson(quizQuestionAnswerDetails));
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


    private void fetchQuizCategoryFromLocal() {
        dialog.dismiss();
        List<QuizCategory> quizCategoryList = gson.fromJson(sharedPreferenceUtils.getStringValue(SharedPreferenceUtils.KEY_QUIZ_CATEGORY_LIST, null),
                new TypeToken<List<QuizCategory>>(){}.getType());
        if(quizCategoryList != null) {
            populateRecyclerView(quizCategoryList);
            Log.d(TAG, "fetchQuizCategoryFromLocal: "+ quizCategoryList.size());
        }

    }

    private void showDialog (String message){
        DialogFactory.createCustomErrorDialog(QuizHomeActivity.this, message, new DialogFactory.CustomDialogListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        }).show();
    }
}
