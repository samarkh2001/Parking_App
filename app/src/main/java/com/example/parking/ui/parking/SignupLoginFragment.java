package com.example.parking.ui.parking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.parking.R;
import com.example.parking.ui.user.LoginFragment;
import com.example.parking.ui.user.SignupFragment;

public class SignupLoginFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_login, container, false);
        // Find the button by its ID
        Button signupButton = view.findViewById(R.id.signupBtn);
        // Set click listener for the signup button
        signupButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_to_sign_up);
        });

        Button loginButton = view.findViewById(R.id.loginBtn);
        // Set click listener for the signup button
        loginButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_to_log_in);

        });

        return view;
    }
}
