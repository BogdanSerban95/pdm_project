package project.passwordproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.passwordproject.R;
import project.passwordproject.classes.Site;
import project.passwordproject.classes.SiteList;
import project.passwordproject.classes.SitesAdapter;
import project.passwordproject.classes.SyncAccountsTask;
import project.passwordproject.classes.UploadToCloudTask;
import project.passwordproject.classes.Utilities;

public class ListActivity extends AppCompatActivity {

    private int REQUEST_ADD = 10;

    private FloatingActionButton fab;
    public static List<Site> mySites = null;
    private ListView sitesListView;
    private SitesAdapter sitesAdapter;
    private SharedPreferences preferences;
    private FirebaseStorage storage;
    private StorageReference mainStorageReference = null;
    private String username;
    public boolean firstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_list_activity);

        username = getIntent().getStringExtra("username");

        storage = FirebaseStorage.getInstance();
        mainStorageReference = storage.getReference("pdmproject-6f9ad.appspot.com");


        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedInstanceState != null) {
            SiteList sites = (SiteList) savedInstanceState.getSerializable("CurrentSites");
            mySites = sites.getSites();
        } else {
            mySites = new ArrayList<>();
        }


//        mySites.add(new Site("Site1", "URL1", new ArrayList<AccountDetails>()));
//        mySites.add(new Site("Site3", "URL1", new ArrayList<AccountDetails>()));

        sitesListView = (ListView) findViewById(R.id.sitesListView);
        sitesAdapter = new SitesAdapter(ListActivity.this, R.layout.site_row, mySites);


        sitesListView.setAdapter(sitesAdapter);

        sitesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, SiteDetailsActivity.class);
                intent.putExtra("SitePosition", position);
                startActivity(intent);
            }
        });

        if (firstStart) {
            syncData();
            firstStart = false;
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddListItem.class);
                startActivityForResult(intent, REQUEST_ADD);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        SiteList siteList = new SiteList(mySites);
        outState.putSerializable("CurrentSites", siteList);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onStart() {
        super.onStart();

        sitesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            Site newSite = (Site) data.getSerializableExtra("newSite");
            sitesAdapter.add(newSite);
//            mySites.add(newSite);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                // Settings was selected
                return true;
            case R.id.menu_log_out:
                FirebaseAuth.getInstance().signOut();
                logOutListener();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();

                return true;
            case R.id.menu_sync_accounts:
                syncData();
                return true;
            case R.id.menu_upload_cloud:
                try {
                    String xmlText = Utilities.createXml(mySites);
                    UploadToCloudTask uploadToCloudTask = new UploadToCloudTask(mainStorageReference, ListActivity.this, username);
                    uploadToCloudTask.execute(xmlText);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logOutListener() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("stateDetector", false);
        editor.apply();
    }

    public void syncData() {
        SyncAccountsTask syncAccountsTask = new SyncAccountsTask(ListActivity.this, mainStorageReference, sitesAdapter);
        syncAccountsTask.execute(username);
    }

}
