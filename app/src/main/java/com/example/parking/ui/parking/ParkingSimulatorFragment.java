package com.example.parking.ui.parking;

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

import commons.entities.Park;
import commons.requests.Message;
import commons.requests.RequestType;


public class ParkingSimulatorFragment extends Fragment {

    public static Park park;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parking_simulator_layout, container, false);
        TextView cityView = view.findViewById(R.id.cityText);
        TextView parkView = view.findViewById(R.id.parkText);
        TextView avblSlots = view.findViewById(R.id.avbl_slots);
        TextView takenSlots = view.findViewById(R.id.taken_slots);
        TextView unAvblSlots = view.findViewById(R.id.unavbl_slots);

        GridLayout grid = view.findViewById(R.id.gridSlots);

        Button backButton = view.findViewById(R.id.back_park);
        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.from_simulator_to_selection);
        });

        Button paymentButton = view.findViewById(R.id.paymentBtn);
        paymentButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new PaymentFragment()) // `R.id.fragment_container` should be the container for fragments in your activity layout
                    .addToBackStack(null) // Optional: Add to back stack so user can navigate back
                    .commit();
        });

       if (getArguments() == null){
           Toast.makeText(getContext(), "Error passing arguments..." , Toast.LENGTH_SHORT).show();

       }else{
           park = null;
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

           int rows = park.getSlots().length;
           int cols = park.getSlots()[0].length;

           int avbl = 0;
           int taken = 0;
           int unAvbl = 0;

           grid.setRowCount(rows);
           grid.setColumnCount(cols);
           // Loop through each slot and create an ImageView dynamically
           for (int i = 0; i < rows; i++) {
               for (int j = 0; j < cols; j++) {
                   ImageView slotImageView = new ImageView(requireContext());

                   if (park.getSlots()[i][j] == null) {
                       avbl++;
                       if (i == 0 && j == 0)
                           slotImageView.setImageResource(R.drawable.ideal_slot);
                        else
                            slotImageView.setImageResource(R.drawable.avbl_slot);  // Green square for available
                   } else if (!park.getSlots()[i][j].isDisabled()){
                       taken++;
                       slotImageView.setImageResource(R.drawable.taken_slot);  // Red square for taken
                   }else{
                       slotImageView.setImageResource(R.drawable.unavbl_slot);  // Red square for taken
                        unAvbl++;
                   }


                   // Set layout parameters (width and height for each slot)
                   GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                   params.width = 200;
                   params.height = 200;
                   params.rowSpec = GridLayout.spec(i);
                   params.columnSpec = GridLayout.spec(j);

                   slotImageView.setLayoutParams(params);

                   grid.addView(slotImageView);
               }
           }
           StringBuilder sb = new StringBuilder();
           sb.append(avbl);
           sb.append(".");
           if (avbl > 0)
               avblSlots.setText(sb.toString());

           sb = new StringBuilder();
           sb.append(taken);
           sb.append(".");
           if (taken > 0)
               takenSlots.setText(sb.toString());

           sb = new StringBuilder();
           sb.append(unAvbl);
           sb.append(".");
           if (unAvbl > 0)
               unAvblSlots.setText(sb.toString());
       }

        return view;
    }
}
