package np.com.naxa.safercities.database.combinedentity;


import androidx.room.Embedded;

import np.com.naxa.safercities.database.entity.CommonPlacesAttrb;
import np.com.naxa.safercities.database.entity.EducationalInstitutes;

public class EducationAndCommon {

    @Embedded
    EducationalInstitutes educationalInstitutes;

    @Embedded
    CommonPlacesAttrb commonPlacesAttrb;

    public EducationalInstitutes getEducationalInstitutes() {
        return educationalInstitutes;
    }

    public void setEducationalInstitutes(EducationalInstitutes educationalInstitutes) {
        this.educationalInstitutes = educationalInstitutes;
    }

    public CommonPlacesAttrb getCommonPlacesAttrb() {
        return commonPlacesAttrb;
    }

    public void setCommonPlacesAttrb(CommonPlacesAttrb commonPlacesAttrb) {
        this.commonPlacesAttrb = commonPlacesAttrb;
    }
}
