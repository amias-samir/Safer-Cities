
package np.com.naxa.safercities.quiz.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizQuestionAnswerDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("wrong_answer")
    @Expose
    private String wrongAnswer;
    @SerializedName("right_answer")
    @Expose
    private String rightAnswer;
    @SerializedName("options")
    @Expose
    private List<QuizAnswerOptions> quizAnswerOptions = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(String wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public List<QuizAnswerOptions> getQuizAnswerOptions() {
        return quizAnswerOptions;
    }

    public void setQuizAnswerOptions(List<QuizAnswerOptions> quizAnswerOptions) {
        this.quizAnswerOptions = quizAnswerOptions;
    }

}
