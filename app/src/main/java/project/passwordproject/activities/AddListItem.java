package project.passwordproject.activities;

import android.app.*;
import android.content.Intent;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import project.passwordproject.R;
import project.passwordproject.classes.AccountDetails;
import project.passwordproject.classes.DatabaseAdapter;
import project.passwordproject.classes.Site;
import project.passwordproject.classes.Utilities;

public class AddListItem extends AppCompatActivity {

    private Button addSiteButton;
    private EditText siteNameEditText;
    private EditText siteAddressEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list_item);


        addSiteButton = (Button) findViewById(R.id.addSiteButton);
        siteNameEditText = (EditText) findViewById(R.id.siteNameEditText);
        siteAddressEditText = (EditText) findViewById(R.id.urlEditText);

        siteAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Utilities.isValidUrl(s.toString())) {
                    siteAddressEditText.setError("You must enter a valid URL...");
                }
            }
        });

        addSiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = siteNameEditText.getText().toString();
                String address = siteAddressEditText.getText().toString();

                if (name.isEmpty()) {
                    siteNameEditText.setError("Name is required!");
                    return;
                }
                if (!address.isEmpty()) {
                    if (!Utilities.isValidUrl(address)) {
                        siteAddressEditText.setError("Invalid address!");
                        return;
                    }
                }
                Site site = new Site(name, address, new ArrayList<AccountDetails>());
                Intent intent = new Intent();
                intent.putExtra("newSite", site);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
