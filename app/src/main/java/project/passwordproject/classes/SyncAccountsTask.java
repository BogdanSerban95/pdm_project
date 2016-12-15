package project.passwordproject.classes;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Serban on 14/12/2016.
 */

public class SyncAccountsTask extends AsyncTask<String, Void, List<Site>> {
    private Context myContext;
    private StorageReference mainStorageReference;
    private List<Site> mySites;
    private SitesAdapter myAdapter;
    private ProgressDialog progressDialog = null;

    public SyncAccountsTask(Context myContext, StorageReference mainStorageReference, SitesAdapter myAdapter) {
        this.myContext = myContext;
        this.mainStorageReference = mainStorageReference;
        this.myAdapter = myAdapter;
        progressDialog = new ProgressDialog(myContext);
    }

    @Override
    protected List<Site> doInBackground(String... params) {
        final StorageReference myAccounts = mainStorageReference.child("userAccounts/" + params[0] + "_passwords.xml");
//        mySites = new ArrayList<>();
        final long ONE_MEGABYTE = 1024 * 1024;

        myAccounts.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String xmlString = new String(bytes);
                try {
                    mySites = Utilities.parseXml(xmlString);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mySites = project.passwordproject.activities.ListActivity.mySites;
            }
        });
        while (mySites == null) {

        }
        return mySites;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Fetching data from cloud...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(List<Site> siteList) {
        super.onPostExecute(siteList);
        progressDialog.dismiss();

//        project.passwordproject.activities.ListActivity.mySites = siteList;

//        myAdapter.setData(siteList);
        myAdapter.clear();
        for (Site s : mySites) {
            myAdapter.add(s);
        }


    }
}
