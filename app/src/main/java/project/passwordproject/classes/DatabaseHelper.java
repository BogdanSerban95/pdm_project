package project.passwordproject.classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;

/**
 * Created by Serban on 07/01/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private String SQL_CREATE_SITE_TABLE = "create table " + TableInfo.TABLE_SITES + "(" +
            TableInfo.SITE_NAME + " text," +
            TableInfo.SITE_OWNER + " text," +
            TableInfo.SITE_ADDRESS + " text," +
            TableInfo.SITE_ID + " text not null ," +
            "primary key(" + TableInfo.SITE_NAME + "," + TableInfo.SITE_OWNER + "))";

    private String SQL_CREATE_ACCOUNTS_TABLE = "create table " + TableInfo.TABLE_ACCOUNTS + "(" +
            TableInfo.ACCOUNT_USERNAME + " text primary key," +
            TableInfo.ACCOUNT_EMAIL + " text unique," +
            TableInfo.ACCOUNT_PASSWORD + " text," +
            TableInfo.ACCOUNT_COMMENTS + " text," +
            TableInfo.ACCOUNT_SITE_ID + " text," +
            "foreign key(" + TableInfo.ACCOUNT_SITE_ID + ") references " +
            TableInfo.TABLE_SITES + "(" + TableInfo.SITE_ID + ") on delete cascade)";

    private String SQL_DROP_ACCOUNTS = "drop table if exists " + TableInfo.TABLE_ACCOUNTS;

    private String SQL_DROP_SITES = "drop table if exists " + TableInfo.TABLE_SITES;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SITE_TABLE);
        db.execSQL(SQL_CREATE_ACCOUNTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_SITES);
        db.execSQL(SQL_CREATE_SITE_TABLE);
        db.execSQL(SQL_DROP_ACCOUNTS);
        db.execSQL(SQL_CREATE_ACCOUNTS_TABLE);
    }

    public final static class TableInfo {
        public static String TABLE_SITES = "sites";
        public static String SITE_NAME = "site_name";
        public static String SITE_OWNER = "site_owner";
        public static String SITE_ADDRESS = "site_address";
        public static String SITE_ID = "site_id";

        public static String TABLE_ACCOUNTS = "accounts";
        public static String ACCOUNT_USERNAME = "account_username";
        public static String ACCOUNT_EMAIL = "account_email";
        public static String ACCOUNT_PASSWORD = "account_password";
        public static String ACCOUNT_COMMENTS = "account_comments";
        public static String ACCOUNT_SITE_ID = "site_id";

    }
}
