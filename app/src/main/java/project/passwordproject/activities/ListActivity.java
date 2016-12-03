package project.passwordproject.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.passwordproject.R;
import project.passwordproject.classes.AccountDetails;
import project.passwordproject.classes.Site;
import project.passwordproject.classes.SiteList;
import project.passwordproject.classes.SitesAdapter;

public class ListActivity extends AppCompatActivity {

    private int REQUEST_ADD = 10;

    private FloatingActionButton fab;
    public static List<Site> mySites = null;
    private ListView sitesListView;
    private SitesAdapter sitesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_list_activity);

        if (savedInstanceState != null) {
            SiteList sites = (SiteList) savedInstanceState.getSerializable("CurrentSites");
            mySites = sites.getSites();
        } else {
            mySites = new ArrayList<>();
        }

//        mySites.add(new Site("Site1", "URL1", new ArrayList<AccountDetails>()));
//        mySites.add(new Site("Site3", "URL1", new ArrayList<AccountDetails>()));


        sitesListView = (ListView) findViewById(R.id.sitesListView);
        sitesAdapter = new SitesAdapter(getApplicationContext(), R.layout.site_row, mySites);

        sitesListView.setAdapter(sitesAdapter);

        sitesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, SiteDetailsActivity.class);
                intent.putExtra("SitePosition", position);
                startActivity(intent);
            }
        });

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
        }
    }
}
