package com.example.parking.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.parking.R;
import com.example.parking.ui.user.LoginFragment;
import com.example.parking.ui.user.SignupFragment;

public class ProfileFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Find the button by its ID
        Button signupButton = view.findViewById(R.id.signupBtn);
        // Set click listener for the signup button
        signupButton.setOnClickListener(v -> {

            // Start SignupActivity
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SignupFragment()) // `R.id.fragment_container` should be the container for fragments in your activity layout
                    .addToBackStack(null) // Optional: Add to back stack so user can navigate back
                    .commit();
        });

        Button loginButton = view.findViewById(R.id.loginBtn);
        // Set click listener for the signup button
        loginButton.setOnClickListener(v -> {

            // Start SignupActivity
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new LoginFragment()) // `R.id.fragment_container` should be the container for fragments in your activity layout
                    .addToBackStack(null) // Optional: Add to back stack so user can navigate back
                    .commit();
        });

        return view;
    }
}
