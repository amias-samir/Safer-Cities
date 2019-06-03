
package np.com.naxa.safercities.publications.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "PublicationsListDetails",
        indices = {@Index(value = "id",
                unique = true)})
public class PublicationsListDetails implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int pid;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    @Expose
    private String type;

    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private String id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "summary")
    @SerializedName("summary")
    @Expose
    private String summary;

    @ColumnInfo(name = "photo")
    @SerializedName("photo")
    @Expose
    private String photo;

    @ColumnInfo(name = "file")
    @SerializedName("file")
    @Expose
    private String file;

    @ColumnInfo(name = "videolink")
    @SerializedName("videolink")
    @Expose
    private String videolink;

    @ColumnInfo(name = "hazard_name")
    @SerializedName("hazard_name")
    @Expose
    private String hazard_name;


    @ColumnInfo(name = "filecat")
    @SerializedName("filecat")
    @Expose
    private String filecat;


    @ColumnInfo(name = "subcat")
    @SerializedName("subcat")
    @Expose
    private String subcat;


    @ColumnInfo(name = "filecateroryname")
    @SerializedName("filecateroryname")
    @Expose
    private String filecateroryname;


    @ColumnInfo(name = "subfilecategory")
    @SerializedName("subfilecategory")
    @Expose
    private String subfilecategory;

    public PublicationsListDetails(String type, String id, String title, String summary, String photo, String file, String videolink, String hazard_name, String filecat, String subcat, String filecateroryname, String subfilecategory) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.photo = photo;
        this.file = file;
        this.videolink = videolink;
        this.hazard_name = hazard_name;
        this.filecat = filecat;
        this.subcat = subcat;
        this.filecateroryname = filecateroryname;
        this.subfilecategory = subfilecategory;
    }

//    public PublicationsListDetails(String type, String id, String title, String summary, String photo, String file, String videolink, String hazard_name) {
//        this.type = type;
//        this.id = id;
//        this.title = title;
//        this.summary = summary;
//        this.photo = photo;
//        this.file = file;
//        this.videolink = videolink;
//        this.name = name;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pid);
        dest.writeString(this.type);
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.summary);
        dest.writeString(this.photo);
        dest.writeString(this.file);
        dest.writeString(this.videolink);
        dest.writeString(this.hazard_name);
        dest.writeString(this.filecat);
        dest.writeString(this.subcat);
        dest.writeString(this.filecateroryname);
        dest.writeString(this.subfilecategory);
    }

    protected PublicationsListDetails(Parcel in) {
        this.pid = in.readInt();
        this.type = in.readString();
        this.id = in.readString();
        this.title = in.readString();
        this.summary = in.readString();
        this.photo = in.readString();
        this.file = in.readString();
        this.videolink = in.readString();
        this.hazard_name = in.readString();
        this.filecat = in.readString();
        this.subcat = in.readString();
        this.filecateroryname = in.readString();
        this.subfilecategory = in.readString();
    }

    public static final Parcelable.Creator<PublicationsListDetails> CREATOR = new Parcelable.Creator<PublicationsListDetails>() {
        @Override
        public PublicationsListDetails createFromParcel(Parcel source) {
            return new PublicationsListDetails(source);
        }

        @Override
        public PublicationsListDetails[] newArray(int size) {
            return new PublicationsListDetails[size];
        }
    };

    public String getHazard_name() {
        return hazard_name;
    }

    public void setHazard_name(String hazard_name) {
        this.hazard_name = hazard_name;
    }

    public String getFilecat() {
        return filecat;
    }

    public void setFilecat(String filecat) {
        this.filecat = filecat;
    }

    public String getSubcat() {
        return subcat;
    }

    public void setSubcat(String subcat) {
        this.subcat = subcat;
    }

    public String getFilecateroryname() {
        return filecateroryname;
    }

    public void setFilecateroryname(String filecateroryname) {
        this.filecateroryname = filecateroryname;
    }

    public String getSubfilecategory() {
        return subfilecategory;
    }

    public void setSubfilecategory(String subfilecategory) {
        this.subfilecategory = subfilecategory;
    }
}
