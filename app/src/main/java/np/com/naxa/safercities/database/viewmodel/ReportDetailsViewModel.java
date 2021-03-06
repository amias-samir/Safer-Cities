package np.com.naxa.safercities.database.viewmodel;

import android.app.Application;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.safercities.database.databaserepository.ReportDetailsRepository;
import np.com.naxa.safercities.database.entity.ReportDetailsEntity;

public class ReportDetailsViewModel extends AndroidViewModel {

    private ReportDetailsRepository reportDetailsRepository;
    private Flowable<List<ReportDetailsEntity>> mAllReportDetailsList;
    private Flowable<List<ReportDetailsEntity>> mAllUnVerifiedReportDetailsList;
    private Flowable<List<ReportDetailsEntity>> mAllUnVerifiedReportDetailsEditedList;

    public ReportDetailsViewModel(@NonNull Application application) {
        super(application);

        reportDetailsRepository = new ReportDetailsRepository(application);

        mAllReportDetailsList = reportDetailsRepository.getAllReportDetailsList();
        mAllUnVerifiedReportDetailsList = reportDetailsRepository.getAllUnVerifiedReportDetailsList();
        mAllUnVerifiedReportDetailsEditedList = reportDetailsRepository.getAllUnVerifiedReportDetailsEditedList();
    }

    public Flowable<List<ReportDetailsEntity>> getAllReportDetailsList() { return mAllReportDetailsList; }
    public Flowable<List<ReportDetailsEntity>> getAllUnVerifiedReportDetailsList() { return mAllUnVerifiedReportDetailsList; }
    public Flowable<List<ReportDetailsEntity>> getAllUnVerifiedReportDetailsEditedList() { return mAllUnVerifiedReportDetailsEditedList; }

    public void deleteSpecificRow(String unique_id){
        reportDetailsRepository.deleteSpecific(unique_id);
    }


    public long insert(ReportDetailsEntity reportDetailsEntity) {
        Log.d("VIewholder", "insert: "+reportDetailsEntity.getIncident_type());
        return reportDetailsRepository.insert(reportDetailsEntity); }
}
