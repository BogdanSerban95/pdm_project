package project.passwordproject.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Serban on 14/12/2016.
 */

public class UploadToCloudTask extends AsyncTask<String, Void, Boolean> {
    private StorageReference storage;
    private Context myContext;
    private String user;
    private Boolean result;
    private ProgressDialog progressDialog = null;

    public UploadToCloudTask(StorageReference storage, Context myContext, String user) {
        this.storage = storage;
        this.myContext = myContext;
        this.user = user;
        progressDialog = new ProgressDialog(myContext);
    }


    @Override
    protected Boolean doInBackground(String... params) {
        result = null;
        StorageReference uploadTarget = storage.child("userAccounts/" + user + "_passwords.xml");
        UploadTask uploadTask = uploadTarget.putBytes(params[0].getBytes());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                result = true;
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result = false;
                    }
                });


        while (result == null) {

        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Uploading to cloud storage...");
        progressDialog.show();

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        progressDialog.dismiss();
        if (aBoolean) {
            Toast.makeText(myContext, "Data successfully uploaded.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(myContext, "A problem occurred...", Toast.LENGTH_SHORT).show();
        }
    }
}
