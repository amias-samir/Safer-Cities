package np.com.naxa.safercities.event;

public class EmergenctContactCallEvent {

    public static class ContactItemClick{
        private String contactNo;

        public ContactItemClick(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getContactNo() {
            return contactNo;
        }
    }
}
