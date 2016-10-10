package ua.primedeym.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "shoppingListDB";
    private static final int DB_VERSION = 12;

    public static final String PRODUCTS_TABLE_NAME = "Products";
    public static final String COL_NAME = "NAME";
    public static final String COL_MAGAZINE = "MAGAZINE";
    public static final String COL_BOUGHT = "BOUGHT";

    public static final String MAGAZINE_TABLE_NAME = "Magazine";
    public static final String MAGAZINE_COL_NAME = "NAME";
    public static final String MAGAZINE_COL_DATA = "DATA";

    public static final String NOTE_TABLE_NAME = "Note";
    public static final String NOTE_COL_NAME = "NAME";
    public static final String NOTE_COL_DESCRIPTION = "DESCRIPTION";
    public static final String NOTE_COL_DATA = "DATA";

    public DBHelper(Context context) {
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

        String queryNote = "create table " + NOTE_TABLE_NAME
                + "(_id integer primary key autoincrement, "
                + NOTE_COL_DESCRIPTION + " TEXT, "
                + NOTE_COL_DATA + " DATA, "
                + NOTE_COL_NAME + " TEXT);";
        sqLiteDatabase.execSQL(queryNote);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + PRODUCTS_TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + MAGAZINE_TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + NOTE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void insertNote(String name, String description) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String data = day + "/" + month + "/" + year;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_COL_NAME, name);
        cv.put(NOTE_COL_DESCRIPTION, description);
        cv.put(NOTE_COL_DATA, data);
        db.insert(NOTE_TABLE_NAME, null, cv);
        db.close();
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


    public void insertShoppingList(String magazine) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String data = day + "/" + month + "/" + year;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MAGAZINE_COL_NAME, magazine);
        cv.put(MAGAZINE_COL_DATA, data);
        sqLiteDatabase.insert(MAGAZINE_TABLE_NAME, null, cv);
        sqLiteDatabase.close();
    }

    public void updateStatus(long rowID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOUGHT, "YES");
        sqLiteDatabase.update(PRODUCTS_TABLE_NAME, contentValues, "_id = ?",
                new String[]{String.valueOf(rowID)});
        sqLiteDatabase.close();
    }

    public void updateNote(long id, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_COL_NAME, title);
        cv.put(NOTE_COL_DESCRIPTION, description);
        db.update(NOTE_TABLE_NAME, cv, "_id = " + id, null);
        db.close();
    }

    public void updateList(long rowId, String newName, String oldName) {
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
        sqLiteDatabase.update(PRODUCTS_TABLE_NAME, cv, COL_MAGAZINE + " = ?", new String[]{oldName});
        sqLiteDatabase.close();
    }


    public void updateBoughtProduct(long rowId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_BOUGHT, "NO");
        sqLiteDatabase.update(PRODUCTS_TABLE_NAME, cv, "_id = ?",
                new String[]{String.valueOf(rowId)});
        sqLiteDatabase.close();
    }

    public void dropProductTable(String magazine) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selections = COL_MAGAZINE + " = ? and " + COL_BOUGHT + " = ? ";
        String[] selectionArgs = new String[]{magazine, "YES"};
        sqLiteDatabase.delete(PRODUCTS_TABLE_NAME, selections, selectionArgs);
    }

    public void dropListTable() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(MAGAZINE_TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }

    public void deleteList(long rowId, String magazine) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(MAGAZINE_TABLE_NAME, "_id = " + String.valueOf(rowId), null);
        sqLiteDatabase.delete(PRODUCTS_TABLE_NAME, COL_MAGAZINE + " = ?", new String[]{magazine});
        sqLiteDatabase.close();
    }

    public void deleteProduct(long id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(PRODUCTS_TABLE_NAME, "_id = " + id, null);
        sqLiteDatabase.close();
    }

    public void deleteNoteAllListForTest() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(NOTE_TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }
}
