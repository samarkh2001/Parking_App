package com.example.parking.ui.parking;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.parking.R;

public class PaymentFragment extends Fragment {

    private EditText cardholderName;
    private EditText cardNumber;
    private EditText expiryDate;
    private EditText cvv;
    private Button payButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        // Initialize views
        cardholderName = view.findViewById(R.id.cardholder_name);
        cardNumber = view.findViewById(R.id.card_number);
        expiryDate = view.findViewById(R.id.expiry_date);
        cvv = view.findViewById(R.id.cvv);
        payButton = view.findViewById(R.id.pay_button);

        // Set click listener for the Pay button
        payButton.setOnClickListener(v -> {
            if (validatePaymentDetails()) {
                // Process the payment (replace this with your actual payment logic)
                Toast.makeText(requireContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private boolean validatePaymentDetails() {
        // Check if fields are not empty
        if (TextUtils.isEmpty(cardholderName.getText().toString())) {
            cardholderName.setError("Cardholder name is required");
            return false;
        }
        if (TextUtils.isEmpty(cardNumber.getText().toString()) || cardNumber.getText().toString().length() != 16) {
            cardNumber.setError("Valid card number is required");
            return false;
        }
        if (TextUtils.isEmpty(expiryDate.getText().toString())) {
            expiryDate.setError("Expiry date is required");
            return false;
        }
        if (TextUtils.isEmpty(cvv.getText().toString()) || cvv.getText().toString().length() != 3) {
            cvv.setError("Valid CVV is required");
            return false;
        }

        return true;
    }
}
