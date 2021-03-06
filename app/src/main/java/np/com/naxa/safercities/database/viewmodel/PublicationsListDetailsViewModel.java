package np.com.naxa.safercities.database.viewmodel;

import android.app.Application;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.safercities.database.databaserepository.PublicationsListDetailsRepository;
import np.com.naxa.safercities.publications.entity.PublicationsListDetails;


public class PublicationsListDetailsViewModel extends AndroidViewModel {

    private PublicationsListDetailsRepository mRepository;
    private Flowable<List<PublicationsListDetails>> mAllPublicationsListtDetailsList;
    private Flowable<List<PublicationsListDetails>> mAllNameWiseList;

    Flowable<List<PublicationsListDetails>> mAllTypeWiseList;
    Flowable<List<PublicationsListDetails>> mAllNameTypeWiseList;
    Flowable<List<String>> mAllDistinctName;
    Flowable<List<String>> mAllDistinctType;
    Flowable<List<String>> mAllDistinctTypeFromName;

    public PublicationsListDetailsViewModel(@NonNull Application application) {
        super(application);

        mRepository = new PublicationsListDetailsRepository(application);

        mAllPublicationsListtDetailsList = mRepository.getAllPublicationslsList();
        mAllDistinctName = mRepository.getDistinctNameList();
        mAllDistinctType = mRepository.getDistinctTypeist();

    }

    public Flowable<List<PublicationsListDetails>> getAllReportDetailsList() {
        return mAllPublicationsListtDetailsList;
    }

    public Flowable<List<String>> getDistinctTypeist() {
        return mAllDistinctType;
    }

    public Flowable<List<String>> getDistinctNameList() {
        return mAllDistinctName;
    }

    public Flowable<List<PublicationsListDetails>> getNameWiseList(String name) {
        mAllNameWiseList = mRepository.getNameWiseList(name);
        return mAllNameWiseList;
    }

    public Flowable<List<String>> getDistinctTypeLIstFromName(String name) {
        mAllDistinctTypeFromName = mRepository.getDistinctTypeLIstFromName(name);

        return mAllDistinctTypeFromName;
    }

    public Flowable<List<String>> getDistinctFilecategoryNameFromType(String type) {
        mAllDistinctType = mRepository.getDistinctFilecategoryNameFromType(type);

        return mAllDistinctType;
    }


    public Flowable<List<PublicationsListDetails>> getTypeWiseList(String type) {
        mAllTypeWiseList = mRepository.getTypeWiseList(type);

        return mAllTypeWiseList;
    }

    public Flowable<List<PublicationsListDetails>> getNameTypeWiseList(String name, String type, String categoryName) {

        if(name.equals("All") && type.equals("All") && categoryName.equals("All")){
            mAllNameTypeWiseList = mRepository.getAllPublicationslsList();
        }

        if(!name.equals("All") && type.equals("All")){
            mAllNameTypeWiseList = mRepository.getNameWiseList(name);
        }

        if(!name.equals("All") && !type.equals("All") && !categoryName.equals("All")){
            mAllNameTypeWiseList = mRepository.getNameTypeCatWiseList(name, type , categoryName);
        }

        if(name.equals("All") && !type.equals("All") && !categoryName.equals("All")){
            mAllNameTypeWiseList = mRepository.getTypeSubCatWiseList( type, categoryName );
        }

        return mAllNameTypeWiseList;
    }

    public void deleteSpecificRow(String unique_id) {
        mRepository.deleteSpecific(unique_id);
    }


    public long insert(PublicationsListDetails PublicationsListDetails) {
        Log.d("VIewholder", "insert: " + PublicationsListDetails.getTitle());
        return mRepository.insert(PublicationsListDetails);
    }
}
