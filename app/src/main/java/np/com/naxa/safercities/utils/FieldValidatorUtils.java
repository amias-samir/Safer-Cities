package np.com.naxa.safercities.utils;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import np.com.naxa.safercities.home.ISET;

public class FieldValidatorUtils {

    public static boolean validateEditText(@NonNull EditText editText){
        String userName = editText.getText().toString().trim();
        if (userName.equals("")) {
            Toast.makeText(ISET.getInstance(), "Text field is empty", Toast.LENGTH_SHORT).show();
            editText.requestFocus();
            editText.setError("Text field is empty");
            return false;
        }
            return  true;
    }

    public static boolean validateMobileNoEditText(@NonNull EditText etContact){
        String mobileNumber = etContact.getText().toString().trim();
        if (mobileNumber.equals("")) {
            Toast.makeText(ISET.getInstance(), "Contact field is empty", Toast.LENGTH_SHORT).show();
            etContact.requestFocus();
            etContact.setError("Contact field is empty");
            return false;
        }
        if (mobileNumber.length() != 10) {
            Toast.makeText(ISET.getInstance(), "Invalid mobile number", Toast.LENGTH_SHORT).show();
            etContact.requestFocus();
            etContact.setError("Invalid mobile number");

            return false;
        }

        return true;
    }

    public static boolean validateEmailPattern (@NonNull EditText etEmail){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email = etEmail.getText().toString().trim();
        if (!email.equals("")) {
            if (email.matches(emailPattern)) {
//                Toasty.success(getApplicationContext(), "Valid email address", Toast.LENGTH_SHORT, true).show();
            } else {
                Toast.makeText(ISET.getInstance(), "Invalid email address. \n Try again.", Toast.LENGTH_SHORT).show();
                etEmail.requestFocus();
                etEmail.setError("Invalid email address. \n Try again.");
                return false;
            }
        } else {
            Toast.makeText(ISET.getInstance(), "Email field is empty", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            etEmail.setError("Email field is empty");
            return false;
        }

        return true;
    }

    public static boolean validatepasswordConfirmation (@NonNull EditText etPassword, @NonNull EditText etConfirmPassword){

        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (confirmPassword.equals("")) {
            Toast.makeText(ISET.getInstance(), "Confirm password field is empty", Toast.LENGTH_SHORT).show();
            etConfirmPassword.requestFocus();
            etConfirmPassword.setError("Confirm password field is empty");
            return false;
        } else {
            if (confirmPassword.equals(password)) {
                return true ;
            } else {
                Toast.makeText(ISET.getInstance(), "Password didn't match.", Toast.LENGTH_SHORT).show();
                etConfirmPassword.requestFocus();
                etConfirmPassword.setError("Password didn't match.");
                return false;
            }
        }
    }

    public static boolean validateSpinnerItemIsselected (@NonNull Spinner spinner, String errorMsg){

        if(spinner.getSelectedItemId() == 0){
            Toast.makeText(ISET.getInstance(), errorMsg, Toast.LENGTH_SHORT).show();
            spinner.requestFocus();
            return false;
        }

        return true;
    }
}
