package project.passwordproject.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import project.passwordproject.R;

/**
 * Created by Serban on 03/12/2016.
 */

public class AccountAdapter extends ArrayAdapter<AccountDetails> {
    private final DatabaseAdapter databaseAdapter;
    private final String siteName;
    private Context myContext;
    private int myResourceId;
    private List<AccountDetails> myAccounts;

    public AccountAdapter(Context context, int resource, List<AccountDetails> objects,DatabaseAdapter databaseAdapter, String siteName) {
        super(context, resource, objects);
        myContext = context;
        myResourceId = resource;
        myAccounts = objects;
        this.databaseAdapter = databaseAdapter;
        this.siteName = siteName;
    }

    @Nullable
    @Override
    public AccountDetails getItem(int position) {
        return myAccounts.get(position);
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
            holder.deleteAccountButton = (ImageView)row.findViewById(R.id.deleteAccountButton);

            row.setTag(holder);
        } else {
            holder = (AccountHolder) row.getTag();
        }

        AccountDetails currentAccount = myAccounts.get(position);

        holder.userNameTextView.setText(currentAccount.getUserName());
        holder.emailTextView.setText(currentAccount.getEmail());

        Integer pos = position;
        holder.deleteAccountButton.setTag(pos);
        holder.deleteAccountButton.setOnClickListener(onClickListener);

        return row;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final View clickedView = v;
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setMessage("You will remove this account from your list... Are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int position = (Integer) clickedView.getTag();
                            try{
                                databaseAdapter.deleteAccount(myAccounts.get(position),siteName);
                            }catch (Exception e){

                            }
                            myAccounts.remove(position);
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
    };

    private class AccountHolder {
        public TextView userNameTextView;
        public TextView emailTextView;
        public ImageView deleteAccountButton;
    }
}
