package project.passwordproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import project.passwordproject.R;
import project.passwordproject.classes.PieChart;
import project.passwordproject.classes.SiteList;

public class PieChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pie_chart);
        SiteList siteList = (SiteList) getIntent().getSerializableExtra("SiteList");
        setContentView(new PieChart(getApplicationContext(),siteList.getSites()));
    }
}
