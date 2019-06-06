
package np.com.naxa.safercities.quiz.entity;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

//@Entity (tableName = "QuizCategory")
public class QuizCategory implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("description")
    @Expose
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.slug);
        dest.writeString(this.createdAt);
        dest.writeString(this.description);
    }

    public QuizCategory() {
    }

    protected QuizCategory(@NotNull Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.slug = in.readString();
        this.createdAt = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<QuizCategory> CREATOR = new Parcelable.Creator<QuizCategory>() {
        @Override
        public QuizCategory createFromParcel(Parcel source) {
            return new QuizCategory(source);
        }

        @Override
        public QuizCategory[] newArray(int size) {
            return new QuizCategory[size];
        }
    };
}
