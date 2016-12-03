package project.passwordproject.activities;


import android.content.Context;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import project.passwordproject.R;
import project.passwordproject.classes.AccountDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAccountFragment extends DialogFragment {


    private AddAccountListener listener;
    private Button addButton;


    public AddAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_add_account, container, false);
        addButton = (Button) view.findViewById(R.id.addAccountButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameEditText = (EditText) view.findViewById(R.id.userNameEditText);
                EditText emailEditText = (EditText) view.findViewById(R.id.emailEditText);
                EditText passEditText = (EditText) view.findViewById(R.id.passwordEditText);
                EditText commentsEditText = (EditText) view.findViewById(R.id.commentsEditText);

                String username = userNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passEditText.getText().toString();
                String comments = commentsEditText.getText().toString();
                AccountDetails details = new AccountDetails(username, email, password, comments);
                listener.OnAccountAdded(details);
                dismiss();
            }
        });

        return view;
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
