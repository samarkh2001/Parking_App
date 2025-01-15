package com.example.parking.ui.parking;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.parking.R;
import com.example.parking.client.Client;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import commons.entities.Park;
import commons.entities.Slot;
import commons.requests.Message;
import commons.requests.RequestType;


public class ParkingSimulatorFragment extends Fragment {

    public static Park park;
    public static boolean reserveSuccess = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parking_simulator_layout, container, false);
        TextView cityView = view.findViewById(R.id.cityText);
        TextView parkView = view.findViewById(R.id.parkText);

        Button backButton = view.findViewById(R.id.back_park);
        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.from_simulator_to_selection);
        });

       if (getArguments() == null){
           Toast.makeText(getContext(), "Error passing arguments..." , Toast.LENGTH_SHORT).show();

       }else{
           park = null;
           reserveSuccess = false;
           String city = getArguments().getString("city") + ",";
           String parkText = getArguments().getString("park") + ".";

           cityView.setText(city);
           parkView.setText(parkText);

           city = getArguments().getString("city");
           parkText = getArguments().getString("park");

           Client.forceWait = true;
           Client.getClient().sendMessageToServer(new Message(RequestType.GET_PARK_SLOTS, new Park(city, parkText)));
           while(Client.forceWait){
               System.out.print("");
           }

           if (park == null){
               Toast.makeText(getContext(), "Error loading park slots" , Toast.LENGTH_SHORT).show();

               NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
               navController.navigate(R.id.from_simulator_to_selection);
               return null;
           }

       }

        return view;
    }

    private void showConfirmationDialog(String city, String park, int i, int j) {
        // Create an AlertDialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Action")
                .setMessage("Do you want to select the slot at position (" + i + ", " + j + ")?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Handle the positive action
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String currentDate = dateFormat.format(calendar.getTime());

                    Slot s = new Slot(-1, i, j, currentDate, 1, 1, false);
                    s.setPark(new Park(city, park));

                    Client.forceWait = true;
                    Client.getClient().sendMessageToServer(new Message(RequestType.RESERVE_SLOT, s));
                    while(Client.forceWait){
                        System.out.print("");
                    }

                    if (reserveSuccess){
                        Bundle args = new Bundle(); // used to pass arguments to another fragment.
                        args.putString("city", city);
                        args.putString("park", park);
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                        navController.navigate(R.id.refresh_simulator, args);
                    }

                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Handle the negative action (if needed)
                    dialog.dismiss();
                })
                .show();
    }

}
