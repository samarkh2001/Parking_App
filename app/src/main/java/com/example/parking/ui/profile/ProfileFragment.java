package com.example.parking.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.parking.R;
import com.example.parking.client.Client;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(Client.loggedInUser == null){
            return inflater.inflate(R.layout.fragment_signup_login, container, false);
        }
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Example dynamic data
        String firstName = Client.loggedInUser.getFirstName(); // Replace with actual server data
        String lastName = Client.loggedInUser.getLastName(); // Replace with actual server data
        String email = Client.loggedInUser.getEmail(); // Replace with actual server data
        String vehicleNumber = Client.loggedInUser.getEmail(); // Replace with actual server data
        String vehicleLocation = "Slot A1 City1 Park12"; // Replace with ParkingSelectionFragment data
        String parkingTime = "10:30 AM"; // Replace with actual server or calculated data
        String paymentCost = "$10.00"; // Replace with calculated cost

        // Populate data into TextViews
        ((TextView) view.findViewById(R.id.welcome_message)).setText(
                String.format("Welcome to our app, %s %s", firstName, lastName));
        ((TextView) view.findViewById(R.id.user_email)).setText(email);
        ((TextView) view.findViewById(R.id.vehicle_number)).setText(vehicleNumber);
        ((TextView) view.findViewById(R.id.vehicle_location)).setText(vehicleLocation);
        ((TextView) view.findViewById(R.id.parking_time)).setText(parkingTime);
        ((TextView) view.findViewById(R.id.payment_cost)).setText(paymentCost);

        return view;
    }
}
