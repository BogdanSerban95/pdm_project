package project.passwordproject.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import project.passwordproject.R;
import project.passwordproject.classes.AccountDetails;
import project.passwordproject.classes.GeneratePassTask;
import project.passwordproject.classes.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAccountFragment extends DialogFragment {


    private AddAccountListener listener;
    private Button addButton;
    private Button generatePassButton;
    private EditText passEditText;
    private LinearLayout passOptionsLayout;
    private EditText commentsEditText;
    private SeekBar passLengthSeekBar;
    private EditText passLengthEditText;
    private AccountDetails details;
    private CheckBox generatePassCheckBox;
    private EditText emailEditText;
    private EditText userNameEditText;

    public AddAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_add_account, container, false);

        passEditText = (EditText) view.findViewById(R.id.passwordEditText);
        passLengthEditText = (EditText) view.findViewById(R.id.passLengthEditText);
        passLengthSeekBar = (SeekBar) view.findViewById(R.id.passLengthSeekBar);
        passOptionsLayout = (LinearLayout) view.findViewById(R.id.passOptionsLayout);
        commentsEditText = (EditText) view.findViewById(R.id.commentsEditText);
        generatePassCheckBox = (CheckBox) view.findViewById(R.id.generatePassCheckBox);
        userNameEditText = (EditText) view.findViewById(R.id.userNameEditText);
        emailEditText = (EditText) view.findViewById(R.id.emailEditText);
        addButton = (Button) view.findViewById(R.id.addAccountButton);

        passLengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                passLengthEditText.setText(String.valueOf(passLengthSeekBar.getProgress()));
                if (progress < 8) {
                    Toast.makeText(view.getContext(), "I recommend that your password will be longer than 8 characters.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Utilities.isValidEmail(s.toString())) {
                    emailEditText.setError("You have entered an invalid email...");
                }
            }
        });
        generatePassCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (generatePassCheckBox.isChecked()) {
                    passOptionsLayout.setVisibility(View.VISIBLE);
//                    commentsEditText.setAlpha(0.0f);
//                    commentsEditText.animate().translationY(passOptionsLayout.getHeight()).alpha(1.0f);
                } else {
                    passOptionsLayout.setVisibility(View.GONE);
//                    commentsEditText.animate().translationY(0);
                }
            }
        });

        generatePassButton = (Button) view.findViewById(R.id.generatePassButton);

        generatePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int upperBit = 0;
                int lowerBit = 0;
                int digitBit = 0;
                int specialBit = 0;

                CheckBox upperCheck = (CheckBox) view.findViewById(R.id.upperCheckBox);
                CheckBox lowerCheck = (CheckBox) view.findViewById(R.id.lowerCheckBox);
                CheckBox digitCheck = (CheckBox) view.findViewById(R.id.digitsCheckBox);
                CheckBox specialCheck = (CheckBox) view.findViewById(R.id.specialCheckBox);

                if (upperCheck.isChecked()) {
                    upperBit = 1;
                }
                if (lowerCheck.isChecked()) {
                    lowerBit = 1;
                }
                if (digitCheck.isChecked()) {
                    digitBit = 1;
                }
                if (specialCheck.isChecked()) {
                    specialBit = 1;
                }

                if (lowerBit == 1 && specialBit == 1) {
                    specialBit = 0;
                } else if (lowerBit == 1 && specialBit == 0) {
                    specialBit = 1;
                }
                String x = String.valueOf(specialBit) + String.valueOf(digitBit) + String.valueOf(lowerBit) + String.valueOf(upperBit);
                String n = String.valueOf(passLengthSeekBar.getProgress());
                GeneratePassTask passTask = new GeneratePassTask(view);
                String[] params = new String[2];
                params[0] = n;
                params[1] = x;
                passTask.execute(params);
            }
        });
        Bundle extraContent = getArguments();
        if (extraContent != null) {
            AccountDetails accountToEdit = (AccountDetails) extraContent.getSerializable(SiteDetailsActivity.EDIT_ACCOUNT);
            if (accountToEdit != null) {
                editAccountBehaviour(accountToEdit, view);
            }
        } else {
            addAccountBehaviour(view);
        }


        return view;
    }

    private void addAccountBehaviour(View templateView) {
        final View view = templateView;


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username = userNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passEditText.getText().toString();
                String comments = commentsEditText.getText().toString();
                AccountDetails details = new AccountDetails(username, email, password, comments);
                listener.OnAccountAdded(details);
                dismiss();
            }
        });
    }

    private void editAccountBehaviour(final AccountDetails accountToEdit, View templateView) {
        View view = templateView;
//        generatePassCheckBox.setVisibility(View.GONE);

        TextView labelTextView = (TextView) view.findViewById(R.id.accountEditLabel);
        labelTextView.setText(R.string.edit_account_label);

        userNameEditText.setText(accountToEdit.getUserName());
        emailEditText.setText(accountToEdit.getEmail());
        passEditText.setText(accountToEdit.getPassword());
        commentsEditText.setText(accountToEdit.getComments());

        addButton.setText(R.string.save);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passEditText.getText().toString();
                String comments = commentsEditText.getText().toString();
                accountToEdit.setUserName(username);
                accountToEdit.setEmail(email);
                accountToEdit.setPassword(password);
                accountToEdit.setComments(comments);
                AddAccountFragment.this.dismiss();
            }
        });


    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AddAccountListener) {
            listener = (AddAccountListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AddAccountListener");
        }
    }

    public interface AddAccountListener {
        public void OnAccountAdded(AccountDetails account);
    }
}
