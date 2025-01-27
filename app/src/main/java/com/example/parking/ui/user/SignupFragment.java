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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.parking.R;
import com.example.parking.client.Client;
import commons.entities.User;
import commons.requests.Message;
import commons.requests.RequestType;

public class SignupFragment extends Fragment {

    private EditText firstNameField, lastNameField, emailField, passwordField, vehicleNumberField;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);

        // Initialize input fields
        firstNameField = view.findViewById(R.id.signup_first_name);
        lastNameField = view.findViewById(R.id.signup_last_name);
        emailField = view.findViewById(R.id.signup_email);
        passwordField = view.findViewById(R.id.signup_password);
        vehicleNumberField = view.findViewById(R.id.signup_vehicle_number);


        // ViewModel for validation (Optional, used for modular logic)
        SignupViewModel viewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        // Sign up button
        Button signupButton = view.findViewById(R.id.signup_button);
        signupButton.setOnClickListener(v -> validateInputs(viewModel));

        // Login redirect text
        TextView loginRedirectText = view.findViewById(R.id.loginRedirectText);
        loginRedirectText.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.from_signup_to_login);
        });

        return view;
    }

    private void validateInputs(SignupViewModel viewModel) {
        // Validate inputs using ViewModel
        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String vehicleNumber = vehicleNumberField.getText().toString();

        if (!viewModel.validateInputs(firstName, lastName, email, password, vehicleNumber)) {
            // Handle field-specific errors
            if (TextUtils.isEmpty(firstName)) firstNameField.setError("First name is required");
            if (TextUtils.isEmpty(lastName)) lastNameField.setError("Last name is required");
            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
                emailField.setError("Enter a valid email");
            if (TextUtils.isEmpty(password) || password.length() < 6)
                passwordField.setError("Password must be at least 6 characters");
            if (TextUtils.isEmpty(vehicleNumber) || vehicleNumber.length() != 8 || !vehicleNumber.matches("\\d+"))
                vehicleNumberField.setError("Enter a valid 8-digit vehicle number");

            return;
        }
        User u = new User(firstName, lastName, email, password);
        Client.getClient().sendMessageToServer(new Message(RequestType.REGISTER, u));
    }
}
