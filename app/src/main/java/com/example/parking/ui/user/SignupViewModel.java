package com.example.parking.ui.user;

import androidx.lifecycle.ViewModel;

public class SignupViewModel extends ViewModel {

    public boolean validateInputs(String firstName, String lastName, String email, String password, String vehicleNumber) {
        if (firstName == null || firstName.isEmpty()) return false;
        if (lastName == null || lastName.isEmpty()) return false;
        if (email == null || email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return false;
        if (password == null || password.length() < 6) return false;
        return vehicleNumber != null && vehicleNumber.length() == 8 && vehicleNumber.matches("\\d+");
    }
}
