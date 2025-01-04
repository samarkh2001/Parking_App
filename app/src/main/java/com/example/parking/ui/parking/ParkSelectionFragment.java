package com.example.parking.ui.parking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.parking.R;

import java.util.Arrays;

public class ParkSelectionFragment extends Fragment {
    private Spinner parksSpinner;

    private ParkSelectionView selectParkView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_park_selection, container, false);
        selectParkView = new ViewModelProvider(this).get(ParkSelectionView.class);
        Spinner citySpinner = view.findViewById(R.id.cityList);
        parksSpinner = view.findViewById(R.id.parks_list);
        Button continueBtn = view.findViewById(R.id.continue_btn);

        /*
         * temp - delete later
         * */
        for (int i = 0; i < ParkData.CITIES.size(); i++){
            ParkData.PARK_MAP.put(ParkData.CITIES.get(i), Arrays.asList("park"+(i+1)+"1", "park"+(i+1)+"2", "park"+(i+1)+"3"));
        }

        // End of temp
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ParkData.CITIES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);


        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                selectParkView.setSelectedCity(selected);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ParkData.PARK_MAP.get(selected));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                parksSpinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectParkView.setSelectedCity(null);
            }
        });

        parksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                selectParkView.setSelectedPark(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectParkView.setSelectedPark(null);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "City: " + selectParkView.getSelectedCity() + ", Park: " + selectParkView.getSelectedPark(), Toast.LENGTH_SHORT).show();
                ParkingSimulatorFragment sim = new ParkingSimulatorFragment();

                Bundle args = new Bundle(); // used to pass arguments to another fragment.
                args.putString("city", selectParkView.getSelectedCity());
                args.putString("park", selectParkView.getSelectedPark());

                sim.setArguments(args);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, sim) // `R.id.fragment_container` should be the container for fragments in your activity layout
                        .addToBackStack(null) // Optional: Add to back stack so user can navigate back
                        .commit();
            }
        });

        return view;
    }
}
