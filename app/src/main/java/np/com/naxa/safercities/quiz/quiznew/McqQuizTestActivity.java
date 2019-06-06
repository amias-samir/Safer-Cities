package np.com.naxa.safercities.quiz.quiznew;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.quiz.QuizConstants;
import np.com.naxa.safercities.quiz.entity.QuizCategory;
import np.com.naxa.safercities.quiz.model.QuizAnswerOptions;
import np.com.naxa.safercities.quiz.model.QuizQuestionAnswerDetail;
import np.com.naxa.safercities.utils.DialogFactory;
import np.com.naxa.safercities.utils.SharedPreferenceUtils;

public class McqQuizTestActivity extends AppCompatActivity {

    private static final String TAG = "McqQuizTestActivity";

    @BindView(R.id.toolbar_general)
    Toolbar toolbarGeneral;
    @BindView(R.id.tv_question_lbl)
    TextView tvQuestionLbl;
    @BindView(R.id.radio_yes_quiz_test)
    RadioButton radioYesQuizTest;
    @BindView(R.id.radio_no_quiz_test)
    RadioButton radioNoQuizTest;
    @BindView(R.id.radio_group_quiz_test)
    RadioGroup radioGroupQuizTest;
    @BindView(R.id.answer_1)
    Button btnAnswer1;
    @BindView(R.id.answer_2)
    Button btnAnswer2;
    @BindView(R.id.answer_3)
    Button btnAnswer3;
    @BindView(R.id.answer_4)
    Button btnAnswer4;
    @BindView(R.id.mcq_group_quiz_test)
    LinearLayout mcqGroupQuizTestLayout;

    int questionPos = 0;
    int totalRightAnswer = 0;
    QuizCategory quizCategory;
    SharedPreferenceUtils sharedPreferenceUtils;
    Gson gson;

    List<QuizQuestionAnswerDetail> quizQuestionAnswerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_quiz_test);
        ButterKnife.bind(this);

        sharedPreferenceUtils = new SharedPreferenceUtils(this);
        gson = new Gson();

        setupToolBar(getIntent());

    }

    private void setupToolBar(Intent intent) {
        setSupportActionBar(toolbarGeneral);
        if(intent == null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.play_quiz));
        }else {
            quizCategory = intent.getParcelableExtra("OBJ");
            getSupportActionBar().setTitle(quizCategory.getName());
            quizQuestionAnswerDetails = gson.fromJson(sharedPreferenceUtils.getStringValue(quizCategory.getId(), null), new TypeToken<List<QuizQuestionAnswerDetail>>(){}.getType());
            initMCQUI(0);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initMCQUI(int questionPosition) {

        if(questionPos > quizQuestionAnswerDetails.size()-1){
            tvQuestionLbl.setText("Total right answer "+totalRightAnswer + " out of "+quizQuestionAnswerDetails.size() +" questions.");
            mcqGroupQuizTestLayout.setVisibility(View.GONE);
            return;
        }
        mcqGroupQuizTestLayout.setVisibility(View.VISIBLE);
        tvQuestionLbl.setText(quizQuestionAnswerDetails.get(questionPosition).getQuestion());
        if(quizQuestionAnswerDetails != null){
            for (int i = 0 ; i<quizQuestionAnswerDetails.get(questionPosition).getQuizAnswerOptions().size(); i++){
                Log.d(TAG, "initMCQUI: ");
                switch (i){
                    case 0:
                        btnAnswer1.setText(quizQuestionAnswerDetails.get(questionPosition).getQuizAnswerOptions().get(0).getName());
                        break;

                    case  1:
                        btnAnswer2.setText(quizQuestionAnswerDetails.get(questionPosition).getQuizAnswerOptions().get(1).getName());

                        break;

                    case  2:
                        btnAnswer3.setText(quizQuestionAnswerDetails.get(questionPosition).getQuizAnswerOptions().get(2).getName());

                        break;

                    case 3:
                        btnAnswer4.setText(quizQuestionAnswerDetails.get(questionPosition).getQuizAnswerOptions().get(3).getName());

                        break;

                }
            }
        }

    }


    String questionElaboration = "A natural disaster is a major adverse event resulting from natural processes of the Earth; examples are floods, hurricanes, tornadoes, volcanic eruptions, earthquakes, tsunamis, and other geologic processes. A natural disaster can cause loss of life or damage property,[1] and typically leaves some economic damage in its wake, the severity of which depends on the affected population's resilience, or ability to recover and also on the infrastructure available";
    @OnClick({R.id.answer_1, R.id.answer_2, R.id.answer_3, R.id.answer_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.answer_1:
                answerValidator(quizQuestionAnswerDetails.get(questionPos).getQuizAnswerOptions(), 0);
                break;

            case R.id.answer_2:
                answerValidator(quizQuestionAnswerDetails.get(questionPos).getQuizAnswerOptions(), 1);

                break;

            case R.id.answer_3:
                answerValidator(quizQuestionAnswerDetails.get(questionPos).getQuizAnswerOptions(), 2);
                break;

            case R.id.answer_4:
                answerValidator(quizQuestionAnswerDetails.get(questionPos).getQuizAnswerOptions(), 3);
                break;
        }
    }

    private void answerValidator(@NotNull List<QuizAnswerOptions> quizAnswerOptions, int option) {
        if(TextUtils.equals(quizAnswerOptions.get(option).getRightAnswer(), "1")){
            totalRightAnswer++;
            questionElaboration = quizQuestionAnswerDetails.get(questionPos).getRightAnswer();
            showElaborationDialog(true);
        }

        if(TextUtils.equals(quizAnswerOptions.get(option).getRightAnswer(), "0")){
            questionElaboration = quizQuestionAnswerDetails.get(questionPos).getWrongAnswer();
            showElaborationDialog(false);
        }
    }


    private void showElaborationDialog(@NonNull Boolean isAnswer){
        String answerStatus = "";
        answerStatus = isAnswer?"Correct":"Incorrect";
        int questionNoProgress = questionPos+1;

        DialogFactory.createQuizAnsElaborationDialog(McqQuizTestActivity.this, "Question "+ questionNoProgress+ "/"+quizQuestionAnswerDetails.size(),
                answerStatus, quizQuestionAnswerDetails.get(questionPos).getQuestion(), questionElaboration, new DialogFactory.CustomDialogListener() {
                    @Override
                    public void onClick() {
                        questionPos++;
                        int questionNo = questionPos+1;

                        initMCQUI(questionPos);
//                        tvQuestionLbl.setText(quizQuestionAnswerDetails.get(questionPos).getQuestion());
//
//                        btnAnswer1.setText("Question no "+questionNo +" QuizAnswerOptions 1");
//                        btnAnswer2.setText("Question no "+questionNo +" QuizAnswerOptions 2");
//                        btnAnswer3.setText("Question no "+questionNo +" QuizAnswerOptions 3");
//                        btnAnswer4.setText("Question no "+questionNo +" QuizAnswerOptions 4");
                    }
                }).show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
