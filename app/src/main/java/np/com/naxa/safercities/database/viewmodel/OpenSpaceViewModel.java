package np.com.naxa.safercities.database.viewmodel;

import android.app.Application;

import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.safercities.database.combinedentity.OpenAndCommon;
import np.com.naxa.safercities.database.databaserepository.ContactRepository;
import np.com.naxa.safercities.database.databaserepository.OpenSpaceRepository;
import np.com.naxa.safercities.database.entity.OpenSpace;

/**
 * Created by samir on 4/23/2018.
 */

public class OpenSpaceViewModel extends AndroidViewModel {
    private ContactRepository mRepository;
    private OpenSpaceRepository openSpaceRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<OpenSpace>> mAllOpenSpaces;

    public OpenSpaceViewModel(Application application) {
        super(application);
        openSpaceRepository = new OpenSpaceRepository(application);

        mAllOpenSpaces = openSpaceRepository.getAllOpenSpaces();

    }

    public LiveData<List<OpenSpace>> getmAllOpenSpaces() {
        return mAllOpenSpaces;
    }

    public void insert(OpenSpace openSpace) {
        Log.d("VIewholder", "insert: " + openSpace.getAccess_roa());
        openSpaceRepository.insert(openSpace);
    }

    public Flowable<List<OpenAndCommon>> getAllOpenSpaceList() {
        return openSpaceRepository.getAll();
    }
}

