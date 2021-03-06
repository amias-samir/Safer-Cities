package np.com.naxa.safercities.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.safercities.database.entity.CommonPlacesAttrb;
import np.com.naxa.safercities.database.entity.Contact;
import np.com.naxa.safercities.database.entity.ReportDetailsEntity;


@Dao
public interface ReportDetailsDao {
    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from ReportDetailsEntity ORDER BY rid ASC")
    Flowable<List<ReportDetailsEntity>> getAllReportDetailsList();

    @Query("SELECT * from ReportDetailsEntity WHERE verify LIKE '0' AND edited LIKE '0'")
    Flowable<List<ReportDetailsEntity>> getUnVerifiedReportDetailsList();

    @Query("SELECT * from ReportDetailsEntity WHERE verify LIKE '0' AND edited LIKE '1'")
    Flowable<List<ReportDetailsEntity>> getUnVerifiedReportDetailsEditedList();


    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ReportDetailsEntity reportDetailsEntity);

    @Query("DELETE FROM ReportDetailsEntity")
    void deleteAll();

    @Query("DELETE FROM ReportDetailsEntity WHERE unique_id LIKE :unique_id")
    void deleteSpecific(String unique_id);

}
