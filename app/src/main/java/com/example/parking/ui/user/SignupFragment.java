package com.example.parking.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.parking.R;
import com.example.parking.client.Client;
import com.example.parking.ui.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

import requests.Message;
import requests.RequestType;

public class SignupFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ProfileFragment()) // `R.id.fragment_container` should be the container for fragments in your activity layout
                .addToBackStack(null) // Optional: Add to back stack so user can navigate back
                .commit());

        Button signUpBtn = view.findViewById(R.id.signup_button);
        signUpBtn.setOnClickListener(event -> {
            EditText emailComp = view.findViewById(R.id.signup_email);
            EditText passComp = view.findViewById(R.id.signup_password);

            String email = emailComp.getText().toString();
            String password = passComp.getText().toString();

            List<String> data = new ArrayList<>();
            data.add(email);
            data.add(password);
            Client.getClient().sendMessageToServer(new Message(RequestType.REGISTER, data));


            Toast.makeText(getContext(), "email: " + email + ", pass: " + password, Toast.LENGTH_SHORT).show();
        });

        return view;
    }

}
