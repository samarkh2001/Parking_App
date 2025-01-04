package com.example.parking.ui.parking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.parking.R;

public class ParkingSimulatorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parking_simulator_layout, container, false);
        TextView cityView = view.findViewById(R.id.cityText);
        TextView parkView = view.findViewById(R.id.parkText);
        TextView avblSlots = view.findViewById(R.id.avbl_slots);
        TextView takenSlots = view.findViewById(R.id.taken_slots);

        GridLayout grid = view.findViewById(R.id.gridSlots);

       if (getArguments() == null){
           Toast.makeText(getContext(), "Error passing arguments..." , Toast.LENGTH_SHORT).show();

       }else{
           String city = getArguments().getString("city") + ",";
           String park = getArguments().getString("park") + ".";
           cityView.setText(city);
           parkView.setText(park);

           int rows = ParkData.slots.length;
           int cols = ParkData.slots[0].length;

           int avbl = 0;
           int taken = 0;

           grid.setRowCount(rows);
           grid.setColumnCount(cols);
            int[] idealCords = ParkData.findClosestSlot(ParkData.slots);
           // Loop through each slot and create an ImageView dynamically
           for (int i = 0; i < rows; i++) {
               for (int j = 0; j < cols; j++) {
                   ImageView slotImageView = new ImageView(requireContext());

                   if (ParkData.slots[i][j] == 1) {
                       avbl++;
                       if (i == idealCords[0] && j == idealCords[1])
                           slotImageView.setImageResource(R.drawable.ideal_slot);
                        else
                            slotImageView.setImageResource(R.drawable.avbl_slot);  // Green square for available
                   } else if (ParkData.slots[i][j] == 0){
                       taken++;
                       slotImageView.setImageResource(R.drawable.taken_slot);  // Red square for taken
                   }else
                       slotImageView.setImageResource(R.drawable.unavbl_slot);  // Red square for taken


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
           String s = avbl + ".";
           if (avbl > 0)
               avblSlots.setText(s);

           s = taken + ".";
           if (taken > 0)
               takenSlots.setText(s);
       }

        return view;
    }
}
