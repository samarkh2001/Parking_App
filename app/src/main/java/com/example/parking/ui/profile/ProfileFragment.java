package com.example.parking.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.parking.R;
import com.example.parking.client.Client;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (Client.loggedInUser == null) {
            // If no user is logged in, navigate to the signup/login fragment
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.from_profile_to_signup_login);
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Populate user data
        String firstName = Client.loggedInUser.getFirstName();
        String lastName = Client.loggedInUser.getLastName();
        String email = Client.loggedInUser.getEmail();
        String vehicleNumber = Client.loggedInUser.getVehicleNum();
        String vehicleLocation = "Slot A1 City1 Park12";
        String parkingTime = "10:30 AM";
        String paymentCost = "$10.00";

        // Populate the TextViews with user data
        ((TextView) view.findViewById(R.id.welcome_message)).setText(
                String.format("Welcome to our app, %s %s", firstName, lastName));
        ((TextView) view.findViewById(R.id.user_email)).setText(email);
        ((TextView) view.findViewById(R.id.vehicle_number)).setText(vehicleNumber);
        ((TextView) view.findViewById(R.id.vehicle_location)).setText(vehicleLocation);
        ((TextView) view.findViewById(R.id.parking_time)).setText(parkingTime);
        ((TextView) view.findViewById(R.id.payment_cost)).setText(paymentCost);

        // Logout Icon Click Listener
        ImageButton logoutIcon = view.findViewById(R.id.logout_icon);
        logoutIcon.setOnClickListener(v -> {
            // Clear user session
            Client.loggedInUser = null;

            // Navigate to the signup/login fragment
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.from_profile_to_signup_login);
        });

        return view;
    }
}
