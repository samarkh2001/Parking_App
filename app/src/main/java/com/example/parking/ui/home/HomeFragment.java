package com.example.parking.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.parking.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set up instruction icon click listener
        ImageButton instructionIcon = view.findViewById(R.id.instruction_icon);
        instructionIcon.setOnClickListener(v -> showInstructionsDialog());

        return view;
    }

    private void showInstructionsDialog() {
        // Create and display the instructions dialog
        new AlertDialog.Builder(requireContext())
                .setTitle("How to Use the Application")
                .setMessage(
                        "1. First, you need to Sign Up or Log In.\n" +
                                "2. Navigate to the Parking page.\n" +
                                "3. Choose your parking location.\n" +
                                "4. Select an available slot (green square) as shown in the Parking Simulator.\n" +
                                "5. Go to the Profile page to view your parking details, email, and payment information.\n" +
                                "6. Use the Payment page to complete your parking payment.\n" +
                                "7. A timer on the Profile page shows how long your vehicle has been parked."
                )
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
