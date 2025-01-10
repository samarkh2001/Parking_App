package com.example.parking.ui.user;

import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    // Mock login logic
    public boolean login(String email, String password) {
        // Replace this with real authentication logic
        return "user@example.com".equals(email) && "password123".equals(password);
    }
}
