package ua.primedeym.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SLDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "shoppingListDB";
    public static final int DB_VERSION = 7;

    public static final String TABLE_NAME = "Products";
    public static final String COL_NAME = "NAME";
    public static final String COL_MAGAZINE = "MAGAZINE";
    public static final String COL_BOUGHT = "BOUGHT";

    //new
    public static final String MAGAZINE_TABLE_NAME = "Magazine";
    public static final String MAGAZINE_COL_NAME = "NAME";

    //new
    public SLDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryProduct = "create table " + TABLE_NAME
                + "(_id integer primary key autoincrement, "
                + COL_NAME + " TEXT, "
                + COL_MAGAZINE + " TEXT, "
                + COL_BOUGHT + " TEXT);";
        sqLiteDatabase.execSQL(queryProduct);
        String queryList = "create table " + MAGAZINE_TABLE_NAME
                + "(_id integer primary key autoincrement, "
                + MAGAZINE_COL_NAME + " TEXT);";
        sqLiteDatabase.execSQL(queryList);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + MAGAZINE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertProduct(String name, String magazine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_MAGAZINE, magazine);
        contentValues.put(COL_BOUGHT, "NO");
        db.insert(TABLE_NAME, null, contentValues);

    }

    public void insertShoppingList(String magazine){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MAGAZINE_COL_NAME, magazine);
        db.insert(MAGAZINE_TABLE_NAME, null, cv);

    }

    public void updateStatus(long rowID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOUGHT, "YES");
        sqLiteDatabase.update(TABLE_NAME, contentValues, "_id = ?", new String[] {String.valueOf(rowID)});
    }

    public void dropProductTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
    }
    public void dropListTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(MAGAZINE_TABLE_NAME, null, null);
    }
}
