package project.passwordproject.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import project.passwordproject.R;

/**
 * Created by Serban on 03/12/2016.
 */

public class AccountAdapter extends ArrayAdapter<AccountDetails> {
    private Context myContext;
    private int myResourceId;
    private List<AccountDetails> myAccounts;

    public AccountAdapter(Context context, int resource, List<AccountDetails> objects) {
        super(context, resource, objects);
        myContext = context;
        myResourceId = resource;
        myAccounts = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AccountHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(myContext);
            row = inflater.inflate(myResourceId, parent, false);

            holder = new AccountHolder();
            holder.userNameTextView = (TextView) row.findViewById(R.id.rowUserTextView);
            holder.emailTextView = (TextView) row.findViewById(R.id.rowEmailTextView);

            row.setTag(holder);
        } else {
            holder = (AccountHolder) row.getTag();
        }

        AccountDetails currentAccount = myAccounts.get(position);

        holder.userNameTextView.setText(currentAccount.getUserName());
        holder.emailTextView.setText(currentAccount.getEmail());

        return row;
    }

    private class AccountHolder {
        public TextView userNameTextView;
        public TextView emailTextView;
    }
}
