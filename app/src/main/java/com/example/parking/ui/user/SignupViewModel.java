package com.example.parking.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignupViewModel extends ViewModel {

    // LiveData to observe email and password
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<Boolean> signupSuccess = new MutableLiveData<>();

    // Getters to expose LiveData
    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getPassword() {
        return password;
    }

    public LiveData<Boolean> getSignupSuccess() {
        return signupSuccess;
    }

    // Method to update email and password
    public void setEmail(String userEmail) {
        email.setValue(userEmail);
    }

    public void setPassword(String userPassword) {
        password.setValue(userPassword);
    }

    // Simulate Signup Process
    public void performSignup() {
        if (validateInputs()) {
            // Simulate signup success
            signupSuccess.setValue(true);
        } else {
            signupSuccess.setValue(false);
        }
    }

    // Simple validation logic
    private boolean validateInputs() {
        return email.getValue() != null && email.getValue().contains("@") &&
                password.getValue() != null && password.getValue().length() >= 6;
    }
}
