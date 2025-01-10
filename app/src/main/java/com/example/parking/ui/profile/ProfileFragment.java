package com.example.parking.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.parking.R;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Example dynamic data
        String firstName = "Samar"; // Replace with actual server data
        String lastName = "Khalil"; // Replace with actual server data
        String email = "samar.khalil@example.com"; // Replace with actual server data
        String vehicleNumber = "12345678"; // Replace with actual server data
        String vehicleLocation = "Slot A1"; // Replace with ParkingSelectionFragment data
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
