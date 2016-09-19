package ua.primedeym.shoppinglist;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SLDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "shoppingListDB";
    public static final int DB_VERSION = 11;

    public static final String PRODUCTS_TABLE_NAME = "Products";
    public static final String COL_NAME = "NAME";
    public static final String COL_MAGAZINE = "MAGAZINE";
    public static final String COL_BOUGHT = "BOUGHT";

    public static final String MAGAZINE_TABLE_NAME = "Magazine";
    public static final String MAGAZINE_COL_NAME = "NAME";
    public static final String MAGAZINE_COL_DATA = "DATA";

    public SLDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryProduct = "create table " + PRODUCTS_TABLE_NAME
                + "(_id integer primary key autoincrement, "
                + COL_NAME + " TEXT, "
                + COL_MAGAZINE + " TEXT, "
                + COL_BOUGHT + " TEXT);";
        sqLiteDatabase.execSQL(queryProduct);
        String queryList = "create table " + MAGAZINE_TABLE_NAME
                + "(_id integer primary key autoincrement, "
                + MAGAZINE_COL_DATA + " DATA, "
                + MAGAZINE_COL_NAME + " TEXT);";
        sqLiteDatabase.execSQL(queryList);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + PRODUCTS_TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + MAGAZINE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void insertProduct(String name, String magazine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_MAGAZINE, magazine);
        contentValues.put(COL_BOUGHT, "NO");
        db.insert(PRODUCTS_TABLE_NAME, null, contentValues);
        db.close();

    }

    @TargetApi(Build.VERSION_CODES.N)
    public void insertShoppingList(String magazine){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MAGAZINE_COL_NAME, magazine);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        //"yyyy-MM-dd"
        cv.put(MAGAZINE_COL_DATA, dateFormat.format(new Date()));
        sqLiteDatabase.insert(MAGAZINE_TABLE_NAME, null, cv);
        sqLiteDatabase.close();

    }

    public void updateStatus(long rowID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOUGHT, "YES");
        sqLiteDatabase.update(PRODUCTS_TABLE_NAME, contentValues, "_id = ?",
                new String[] {String.valueOf(rowID)});
        sqLiteDatabase.close();
    }

    public void updateList(long rowId, String newName, String oldName ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MAGAZINE_COL_NAME, newName);
        sqLiteDatabase.update(MAGAZINE_TABLE_NAME, cv, "_id = " + rowId, null);
        updateProduct(newName, oldName);
        sqLiteDatabase.close();
    }

    public void updateProduct(String newName, String oldName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MAGAZINE, newName);
        sqLiteDatabase.update(PRODUCTS_TABLE_NAME, cv, COL_MAGAZINE + " = ?", new String[] {oldName});
        sqLiteDatabase.close();
    }


    public void updateBoughtProduct(long rowId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_BOUGHT, "NO");
        sqLiteDatabase.update(PRODUCTS_TABLE_NAME, cv, "_id = ?",
                new String[] {String.valueOf(rowId)});
        sqLiteDatabase.close();
    }

    public void dropProductTable(String magazine){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selections = COL_MAGAZINE + " = ? and " + COL_BOUGHT + " = ? ";
        String[] selectionArgs = new String[] {magazine, "YES"};
        sqLiteDatabase.delete(PRODUCTS_TABLE_NAME, selections, selectionArgs);
    }
    public void dropListTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(MAGAZINE_TABLE_NAME, null, null);
    }

    public void deleteList(long rowId, String magazine) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(MAGAZINE_TABLE_NAME, "_id = " + String.valueOf(rowId), null);
        sqLiteDatabase.delete(PRODUCTS_TABLE_NAME, COL_MAGAZINE + " = ?", new String[] {magazine});
        sqLiteDatabase.close();
    }

    public void deleteProduct(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(PRODUCTS_TABLE_NAME, "_id = " + id, null);
        sqLiteDatabase.close();
    }
}
