package project.passwordproject.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import project.passwordproject.R;

/**
 * Created by Serban on 04/12/2016.
 */

public class GeneratePassTask extends AsyncTask<String,Void,String> {
    private View myView;
    public GeneratePassTask(View view) {
        myView =view;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        EditText passEditText = (EditText)myView.findViewById(R.id.passwordEditText);
        passEditText.setText(s);
    }

    @Override
    protected String doInBackground(String... params) {
        String generatedPass=null;
        try {
            URL url = new URL("https://helloacm.com/api/random/?n="+params[0]+"&x="+params[1]);
            //n-> lungimea parolei
            //x->componenta parolei: bit 1 = upper case, bit 2 = lower case, bit 4 = digits and bit 8 = special characters.
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
            generatedPass = bufferedReader.readLine();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return generatedPass.substring(1,generatedPass.length()-1);
    }
}
