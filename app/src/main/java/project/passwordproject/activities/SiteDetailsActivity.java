package project.passwordproject.activities;

import android.accounts.Account;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import project.passwordproject.R;
import project.passwordproject.classes.AccountAdapter;
import project.passwordproject.classes.AccountDetails;
import project.passwordproject.classes.DatabaseAdapter;
import project.passwordproject.classes.Site;

public class SiteDetailsActivity extends AppCompatActivity implements AddAccountFragment.AddAccountListener {
    public static String EDIT_ACCOUNT = "accountToEdit";

    private TextView titleTextView;
    private TextView siteNameTextView;
    private TextView siteAddressTextView;
    private ListView accountsListView;
    private FloatingActionButton addAccountActionButton;
    private Site currentSite;
    private AccountAdapter accountAdapter;
    private DatabaseAdapter databaseAdapter;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_details);

        userName = getIntent().getStringExtra("UserName");
        databaseAdapter = new DatabaseAdapter(getApplicationContext(), userName);
        databaseAdapter.openConnection();

        titleTextView = (TextView) findViewById(R.id.siteNameTextView);
        siteNameTextView = (TextView) findViewById(R.id.detailsSiteNameTextView);
        siteAddressTextView = (TextView) findViewById(R.id.detailsSiteAddressTextView);
        accountsListView = (ListView) findViewById(R.id.accountsListView);
        addAccountActionButton = (FloatingActionButton) findViewById(R.id.addAccountFab);

        int position = getIntent().getIntExtra("SitePosition", 0);
        currentSite = ListActivity.mySites.get(position);
        titleTextView.setText(currentSite.getName());
        siteNameTextView.setText(currentSite.getName());
        siteAddressTextView.setText(currentSite.getUrl());

        addAccountActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                AddAccountFragment accountFragment = new AddAccountFragment();
                accountFragment.show(fragmentTransaction, "dialog");
            }
        });

        accountAdapter = new AccountAdapter(SiteDetailsActivity.this, R.layout.account_row, currentSite.getAccountList(), databaseAdapter, currentSite.getName());
        accountsListView.setAdapter(accountAdapter);

        accountsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                AddAccountFragment accountFragment = new AddAccountFragment();
                Bundle extraData = new Bundle();
                extraData.putSerializable(EDIT_ACCOUNT, accountAdapter.getItem(position));
                accountFragment.setArguments(extraData);
                accountFragment.show(fragmentTransaction, "dialog");
            }
        });
    }

    @Override
    public void OnAccountAdded(AccountDetails account) {
        for (AccountDetails acc : currentSite.getAccountList()) {
            if (acc.getUserName().equals(account.getUserName())) {
                Toast.makeText(getApplicationContext(), "You already have an account with this username!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        accountAdapter.add(account);
        databaseAdapter.insertAccount(account, currentSite.getName());
    }

    @Override
    public void OnAccountEdited(AccountDetails newAccountDetails, AccountDetails oldAccountDetails) {
        try {
            databaseAdapter.deleteAccount(oldAccountDetails, currentSite.getName());
            databaseAdapter.insertAccount(newAccountDetails, currentSite.getName());
            accountAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseAdapter.closeConnection();
    }
}
