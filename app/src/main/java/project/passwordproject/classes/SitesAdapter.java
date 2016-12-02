package project.passwordproject.classes;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    public SitesAdapter(Context context, int resource, List<Site> objects) {
        super(context, resource, objects);
        myContext = context;
        myResourceId = resource;
        mySites = objects;
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

            row.setTag(holder);
        } else {
            holder = (SiteHolder) row.getTag();
        }

        Site currentSite = mySites.get(position);

        holder.siteName.setText(currentSite.getName());
        String infoString = "Total Accounts: " + currentSite.getAccountList().size();
        holder.siteInfo.setText(infoString);

        return row;
    }

    private class SiteHolder {
        public TextView siteName;
        public TextView siteInfo;
    }
}
