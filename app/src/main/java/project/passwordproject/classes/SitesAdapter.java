package project.passwordproject.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import project.passwordproject.R;

/**
 * Created by Serban on 02/12/2016.
 */

public class SitesAdapter extends ArrayAdapter<Site> {
    private Context myContext;
    private int myResourceId;
    private List<Site> mySites;
    private  DatabaseAdapter databaseAdapter;

    public SitesAdapter(Context context, int resource, List<Site> objects, DatabaseAdapter databaseAdapter) {
        super(context, resource, objects);
        myContext = context;
        myResourceId = resource;
        mySites = objects;
        this.databaseAdapter = databaseAdapter;
    }

    public void refreshSites(List<Site> sites) {
        this.mySites.clear();
        this.mySites.addAll(sites);
        notifyDataSetChanged();
    }

    public void setData(List<Site> siteList) {
        this.mySites = siteList;
        notifyDataSetChanged();
    }

    public List<Site> getData() {
        return mySites;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SiteHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(myContext);
            row = inflater.inflate(myResourceId, parent, false);

            holder = new SiteHolder();
            holder.siteName = (TextView) row.findViewById(R.id.rowSiteNameTextView);
            holder.siteInfo = (TextView) row.findViewById(R.id.rowInfoTextView);
            holder.deleteImageButton = (ImageView) row.findViewById(R.id.deleteSiteButton);

            row.setTag(holder);

        } else {
            holder = (SiteHolder) row.getTag();
        }


        Site currentSite = mySites.get(position);

        holder.siteName.setText(currentSite.getName());
        String infoString = "Total Accounts: " + currentSite.getAccountList().size();
        holder.siteInfo.setText(infoString);

        Integer pos = position;
        holder.deleteImageButton.setTag(pos);
        holder.deleteImageButton.setOnClickListener(onClickListener);


        return row;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final View clickedView = v;
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setMessage("You will remove this site and all the passwords assigned to it... Are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int position = (Integer) clickedView.getTag();
                            databaseAdapter.deleteSite(mySites.get(position));
                            mySites.remove(position);
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

    private class SiteHolder {
        public TextView siteName;
        public TextView siteInfo;
        public ImageView deleteImageButton;
    }
}
