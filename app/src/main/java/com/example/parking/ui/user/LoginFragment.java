package com.example.parking.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.parking.R;
import com.example.parking.client.Client;
import com.example.parking.ui.parking.ParkSelectionFragment;
import com.example.parking.ui.parking.SignupLoginFragment;

import commons.entities.User;
import commons.requests.Message;
import commons.requests.RequestType;

public class LoginFragment extends Fragment {

    private EditText emailField, passwordField;
    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        // Initialize fields
        emailField = view.findViewById(R.id.login_email);
        passwordField = view.findViewById(R.id.login_password);

        // Initialize ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Back button
        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SignupLoginFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Login button
        Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            if (validateInputs()) {
                if (Client.loggedInUser != null) {
                    // Navigate to ParkSelectionFragment
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.from_login_to_parking);
                }
            }else {
                Toast.makeText(getContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        // Redirect to Sign Up
        TextView signUpRedirectText = view.findViewById(R.id.signUpRedirectText);
        signUpRedirectText.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.from_login_to_signup);
        });

        return view;
    }

    private boolean validateInputs() {
        // Validate email
        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Enter a valid email");
            return false;
        }

        // Validate password
        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            return false;
        }

        User u = new User(email, password);
        Client.forceWait = true;
        Client.getClient().sendMessageToServer(new Message(RequestType.LOGIN, u));

        while(Client.forceWait){
            System.out.print("");
        }
        System.out.println();
        return Client.loggedInUser != null;
    }
}
