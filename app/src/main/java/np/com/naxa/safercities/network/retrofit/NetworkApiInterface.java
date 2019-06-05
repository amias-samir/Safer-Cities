package np.com.naxa.safercities.network.retrofit;


import java.util.List;

import io.reactivex.Observable;
import np.com.naxa.safercities.beready.BeReadyResponse;
import np.com.naxa.safercities.disasterinfo.model.DisasterInfoListResponse;
import np.com.naxa.safercities.drr_dictionary.data_glossary.TerminologiesListResponse;
import np.com.naxa.safercities.emergencyContacts.model.ContactCategoryListResponse;
import np.com.naxa.safercities.inventory.model.InventoryListResponse;
import np.com.naxa.safercities.mycircle.MyCircleContactListResponse;
import np.com.naxa.safercities.mycircle.registeruser.LoginResponse;
import np.com.naxa.safercities.mycircle.registeruser.NormalResponse;
import np.com.naxa.safercities.network.model.AskForHelpResponse;
import np.com.naxa.safercities.network.model.GeoJsonCategoryDetails;
import np.com.naxa.safercities.publications.entity.PublicationsListResponse;
import np.com.naxa.safercities.quiz.model.QuizCategoryResponse;
import np.com.naxa.safercities.quiz.model.QuizQuestionAnswerDetail;
import np.com.naxa.safercities.report.wardstaff.model.UnverifiedFormListResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Samir on 5/7/2018.
 */

public interface NetworkApiInterface {


    @Multipart
    @POST("ReportController/report_insert_api")
    Call<AskForHelpResponse> getAskForHelpResponse(@Part MultipartBody.Part file,
                                                   @Part("data") RequestBody jsonToSend);

    @GET("MapApi/get_category")
    Observable<GeoJsonCategoryDetails> getGeoJsonCategoryDetails();

    @POST("MapApi/geojson")
    @FormUrlEncoded
    Observable<ResponseBody> getGeoJsonDetails(@Field("table") String table_name);


    @POST("check_registration")
    @FormUrlEncoded
    Observable<NormalResponse> getRegisterResponse(@Field("api_key") String api_key,
                                                   @Field("data") String jsonData);

    @POST("loginCheck")
    @FormUrlEncoded
    Observable<LoginResponse> getLoginResponse(@Field("api_key") String api_key,
                                               @Field("data") String jsonData);

    @POST("check_registered_num")
    @FormUrlEncoded
    Observable<MyCircleContactListResponse> getContactListResponse(@Field("api_key") String api_key,
                                                                   @Field("data") String jsonData);

    @POST("add_my_circle")
    @FormUrlEncoded
    Observable<NormalResponse> getCircleListUpdateResponse(@Field("api_key") String api_key,
                                                           @Field("email") String email,
                                                           @Field("data") String jsonData);

    @POST("delete_circle")
    @FormUrlEncoded
    Observable<NormalResponse> getCircleListDeleteResponse(@Field("api_key") String api_key,
                                                           @Field("email") String email,
                                                           @Field("mobile_no") String mobile_no);


    @POST("insert_incident_report")
    @FormUrlEncoded
    Observable<NormalResponse> getReportSendResponse(@Field("api_key") String api_key,
                                                           @Field("data") String jsonData);

    @POST("get_unverified_report")
    @FormUrlEncoded
    Observable<UnverifiedFormListResponse> getUnverifiedReportResponse(@Field("api_key") String api_key);

    @POST("get_term_data")
    @FormUrlEncoded
    Observable<TerminologiesListResponse> getTerminologiesResponse(@Field("api_key") String api_key);

    @POST("get_drrinfo_data")
    @FormUrlEncoded
    Observable<DisasterInfoListResponse> getDisasterInfoResponse(@Field("api_key") String api_key);

    @POST("get_publication_data")
    @FormUrlEncoded
    Observable<PublicationsListResponse> getPublicationsListDetailsResponse(@Field("api_key") String api_key);

    @POST("contact_category")
    @FormUrlEncoded
    Observable<ContactCategoryListResponse> getContactCategoryListResponse(@Field("api_key") String api_key);

    @POST("inventory_data")
    @FormUrlEncoded
    Observable<InventoryListResponse> getInventoryListResponse(@Field("api_key") String api_key);

    @POST("readyapi")
    @FormUrlEncoded
    Observable<BeReadyResponse> getBeReadyResponse(@Field("api_key") String api_key);

    @POST("quizapi_category")
    @FormUrlEncoded
    Observable<QuizCategoryResponse> getQuizcategoryResponse(@Field("api_key") String api_key);

    @POST("quizapi")
    @FormUrlEncoded
    Observable<List<QuizQuestionAnswerDetail>> getQuestionAnswerDetailsResponse(@Field("api_key") String api_key, @Field("id") String id);

}

