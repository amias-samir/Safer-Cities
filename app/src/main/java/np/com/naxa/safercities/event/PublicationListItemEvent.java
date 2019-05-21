package np.com.naxa.safercities.event;

import np.com.naxa.safercities.publications.entity.PublicationsListDetails;

public class PublicationListItemEvent {

    public static final String KEY_FILES = "files";
    public static final String KEY_IMAGE = "images";
    public static final String KEY_VIDEO = "video";

    public static class PublicationListitemClick{

        private PublicationsListDetails publicationsListDetails;

        public PublicationListitemClick(PublicationsListDetails publicationsListDetails) {
            this.publicationsListDetails = publicationsListDetails;
        }

        public PublicationsListDetails getPublicationsListDetails() {
            return publicationsListDetails;
        }

        public void setPublicationsListDetails(PublicationsListDetails publicationsListDetails) {
            this.publicationsListDetails = publicationsListDetails;
        }
    }
}
