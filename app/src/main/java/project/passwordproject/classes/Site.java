package project.passwordproject.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serban on 02/12/2016.
 */

public class Site implements Serializable {
    private String name;
    private String url;
    private List<AccountDetails> accountList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Site() {
        accountList = new ArrayList<>();
    }

    public List<AccountDetails> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountDetails> accountList) {
        this.accountList = accountList;
    }

    public Site(String name, String url, List<AccountDetails> accountList) {

        this.name = name;
        this.url = url;
        this.accountList = accountList;
    }

    public void addAcoount(AccountDetails account) {
        accountList.add(account);
    }

    public void removeAccount(AccountDetails account) {
        accountList.remove(account);
    }

    public AccountDetails getAccount(int index) {
        return accountList.get(index);
    }
}
