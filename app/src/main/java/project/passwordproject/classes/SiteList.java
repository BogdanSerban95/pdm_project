package project.passwordproject.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serban on 03/12/2016.
 */

public class SiteList implements Serializable {
    private List<Site> sites;

    public SiteList() {
        sites = new ArrayList<>();
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public SiteList(List<Site> sites) {

        this.sites = sites;
    }

    public Site get(int index) {
        return sites.get(index);
    }

    public void add(Site site) {
        sites.add(site);
    }
}
