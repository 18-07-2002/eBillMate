package com.Swayam.gstbill.gstbill;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetupPasswordActivity extends AppCompatActivity {

    public static final String SETUP_BUSINESS_NAME_KEY = "setup-business-name-key";
    public static final String SETUP_BUSINESS_ADDRESS_KEY = "setup-business-address-key";
    public static final String SETUP_BUSINESS_CONTACT_KEY = "setup-business-contact-key";
    public static final String SETUP_PASSWORD_KEY = "setup-password-key";
    EditText businessName;
    EditText businessAddress;
    EditText businessContact;
    EditText newPass;
    EditText confirmPass;
    Button setupPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_password);

        businessName = (EditText) findViewById(R.id.setup_business_value);
        businessAddress = (EditText) findViewById(R.id.setup_business_address_value);
        businessContact = (EditText) findViewById(R.id.setup_business_contact_value);
        newPass = (EditText) findViewById(R.id.setup_new_password_value);
        confirmPass = (EditText) findViewById(R.id.setup_confirm_password_value);
        setupPass = (Button) findViewById(R.id.setup_password_btn);

        setupPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String businessNameValue = businessName.getText().toString();
                String businessAddressValue = businessAddress.getText().toString();
                String businessContactValue = businessContact.getText().toString();
                String newPassword = newPass.getText().toString();
                String confirmPassword = confirmPass.getText().toString();
                if (newPassword.equals(confirmPassword)) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SetupPasswordActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SETUP_BUSINESS_NAME_KEY, businessNameValue);
                    editor.putString(SETUP_BUSINESS_ADDRESS_KEY, businessAddressValue);
                    editor.putString(SETUP_BUSINESS_CONTACT_KEY, businessContactValue);
                    editor.putString(SETUP_PASSWORD_KEY, newPassword);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), getString(R.string.setup_password_set), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SetupPasswordActivity.this, BillsActivity.class));

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.setup_password_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
