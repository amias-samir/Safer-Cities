
package np.com.naxa.safercities.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizAnswerOptions {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("right_answer")
    @Expose
    private String rightAnswer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

}
