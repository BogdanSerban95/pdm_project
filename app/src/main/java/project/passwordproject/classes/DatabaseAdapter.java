package project.passwordproject.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serban on 07/01/2017.
 */

public class DatabaseAdapter {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private Context myContext;
    private String userName;

    public DatabaseAdapter(Context myContext, String userName) {
        this.myContext = myContext;
        this.userName = userName;
        databaseHelper = new DatabaseHelper(myContext, "password_database", null, 2);
    }

    public void openConnection() {
        database = databaseHelper.getWritableDatabase();
//        Toast.makeText(myContext, "Database opened...", Toast.LENGTH_SHORT).show();
    }

    public void closeConnection() {
        database.close();
//        Toast.makeText(myContext, "Database closed...", Toast.LENGTH_SHORT).show();
    }

    public void saveData(List<Site> mySites) {
        database.execSQL("delete from " + DatabaseHelper.TableInfo.TABLE_SITES);
        database.execSQL("delete from " + DatabaseHelper.TableInfo.TABLE_ACCOUNTS);

        for (Site site : mySites) {
            String siteID = userName + "_" + site.getName();
            ContentValues siteValues = new ContentValues();
            siteValues.put(DatabaseHelper.TableInfo.SITE_NAME, site.getName());
            siteValues.put(DatabaseHelper.TableInfo.SITE_ADDRESS, site.getUrl());
            siteValues.put(DatabaseHelper.TableInfo.SITE_OWNER, userName);
            siteValues.put(DatabaseHelper.TableInfo.SITE_ID, siteID);
            database.insert(DatabaseHelper.TableInfo.TABLE_SITES, null, siteValues);

            for (AccountDetails account : site.getAccountList()) {
                ContentValues accountValues = new ContentValues();
                accountValues.put(DatabaseHelper.TableInfo.ACCOUNT_USERNAME, account.getUserName());
                accountValues.put(DatabaseHelper.TableInfo.ACCOUNT_EMAIL, account.getEmail());
                accountValues.put(DatabaseHelper.TableInfo.ACCOUNT_PASSWORD, account.getPassword());
                accountValues.put(DatabaseHelper.TableInfo.ACCOUNT_COMMENTS, account.getComments());
                accountValues.put(DatabaseHelper.TableInfo.ACCOUNT_SITE_ID, siteID);
                database.insert(DatabaseHelper.TableInfo.TABLE_ACCOUNTS, null, accountValues);
            }
        }
//        Toast.makeText(myContext, "Data saved...", Toast.LENGTH_SHORT).show();
    }

    public List<Site> restoreData() {
        List<Site> mySites = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TableInfo.TABLE_SITES,
                new String[]{
                        DatabaseHelper.TableInfo.SITE_ID,
                        DatabaseHelper.TableInfo.SITE_NAME,
                        DatabaseHelper.TableInfo.SITE_ADDRESS
                },
                DatabaseHelper.TableInfo.SITE_OWNER + "=?",
                new String[]{userName},
                null,
                null,
                null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                Site site = new Site();
                site.setName(cursor.getString(1));
                site.setUrl(cursor.getString(2));
                String siteId = cursor.getString(0);

                Cursor accountCursor = database.query(DatabaseHelper.TableInfo.TABLE_ACCOUNTS,
                        new String[]{
                                DatabaseHelper.TableInfo.ACCOUNT_USERNAME,
                                DatabaseHelper.TableInfo.ACCOUNT_EMAIL,
                                DatabaseHelper.TableInfo.ACCOUNT_PASSWORD,
                                DatabaseHelper.TableInfo.ACCOUNT_COMMENTS
                        },
                        DatabaseHelper.TableInfo.ACCOUNT_SITE_ID + "=?",
                        new String[]{siteId},
                        null,
                        null,
                        null);
                List<AccountDetails> accounts = new ArrayList<>();
                accountCursor.moveToFirst();
                if (!accountCursor.isAfterLast()) {
                    do {
                        AccountDetails accountDetails = new AccountDetails();
                        accountDetails.setUserName(accountCursor.getString(0));
                        accountDetails.setEmail(accountCursor.getString(1));
                        accountDetails.setPassword(accountCursor.getString(2));
                        accountDetails.setComments(accountCursor.getString(3));
                        accounts.add(accountDetails);
                    } while (accountCursor.moveToNext());
                }
                site.setAccountList(accounts);
                mySites.add(site);
            } while (cursor.moveToNext());
        }
//        Toast.makeText(myContext, "Data restored...", Toast.LENGTH_SHORT).show();
        return mySites;
    }

    public void insertSite(Site site) {
        String siteID = userName + "_" + site.getName();
        ContentValues siteValues = new ContentValues();
        siteValues.put(DatabaseHelper.TableInfo.SITE_NAME, site.getName());
        siteValues.put(DatabaseHelper.TableInfo.SITE_ADDRESS, site.getUrl());
        siteValues.put(DatabaseHelper.TableInfo.SITE_OWNER, userName);
        siteValues.put(DatabaseHelper.TableInfo.SITE_ID, siteID);
        database.insert(DatabaseHelper.TableInfo.TABLE_SITES, null, siteValues);
//        Toast.makeText(myContext, "Site inserted...", Toast.LENGTH_SHORT).show();
    }

    public void insertAccount(AccountDetails account, String siteName) {
        ContentValues accountValues = new ContentValues();
        String siteID = userName + "_" + siteName;
        accountValues.put(DatabaseHelper.TableInfo.ACCOUNT_USERNAME, account.getUserName());
        accountValues.put(DatabaseHelper.TableInfo.ACCOUNT_EMAIL, account.getEmail());
        accountValues.put(DatabaseHelper.TableInfo.ACCOUNT_PASSWORD, account.getPassword());
        accountValues.put(DatabaseHelper.TableInfo.ACCOUNT_COMMENTS, account.getComments());
        accountValues.put(DatabaseHelper.TableInfo.ACCOUNT_SITE_ID, siteID);
        database.insert(DatabaseHelper.TableInfo.TABLE_ACCOUNTS, null, accountValues);
//        Toast.makeText(myContext, "Account inserted...", Toast.LENGTH_SHORT).show();
    }

    public void deleteAccount(AccountDetails accountDetails, String siteName) {
        String siteID = userName + "_" + siteName;
        String whereClause = DatabaseHelper.TableInfo.ACCOUNT_SITE_ID + "=? and " + DatabaseHelper.TableInfo.ACCOUNT_USERNAME + "=?";
        try{
            database.delete(DatabaseHelper.TableInfo.TABLE_ACCOUNTS, whereClause, new String[]{siteID, accountDetails.getUserName()});
//            Toast.makeText(myContext, "Account deleted...", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(myContext, "A problem occurred while deleting account from database...", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteSite(Site site) {
        String whereClause = DatabaseHelper.TableInfo.SITE_NAME + "=? and " + DatabaseHelper.TableInfo.SITE_OWNER + "=?";
        try{
            database.delete(DatabaseHelper.TableInfo.TABLE_SITES,whereClause,new String[]{site.getName(),userName});
//            Toast.makeText(myContext, "Account deleted...", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(myContext, "A problem occurred while deleting site from database...", Toast.LENGTH_SHORT).show();
        }
    }
}
