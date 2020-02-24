package np.com.naxa.safercities.database.viewmodel;

import android.app.Application;

import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import np.com.naxa.safercities.database.databaserepository.ContactRepository;
import np.com.naxa.safercities.database.entity.Contact;
import np.com.naxa.safercities.database.entity.OpenSpace;

/**
 * Created by samir on 4/22/2018.
 */

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Contact>> mAllContacts;
    private LiveData<List<OpenSpace>> mAllOpenSpaces;

    public ContactViewModel(Application application) {
        super(application);
        mRepository = new ContactRepository(application);

        mAllContacts = mRepository.getAllContacts();
    }
    //    contact
    public LiveData<List<Contact>> getAllContacts() { return mAllContacts; }

    public void insert(Contact contact) {
        Log.d("VIewholder", "insert: "+contact.getFirstName());
        mRepository.insert(contact); }
}