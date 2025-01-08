package com.example.parking.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.parking.R;
import com.example.parking.ui.profile.ProfileFragment;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ProfileFragment()) // `R.id.fragment_container` should be the container for fragments in your activity layout
                    .addToBackStack(null) // Optional: Add to back stack so user can navigate back
                    .commit();
        });

        return view;
    }
}
