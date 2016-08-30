package ua.primedeym.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SLDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "shoppingListDB";
    public static final int DB_VERSION = 3;

    public static final String TABLE_NAME = "Products";
    public static final String COL_NAME = "NAME";
    public static final String COL_MAGAZINE = "MAGAZINE";
    public static final String COL_BOUGHT = "NO";
    public static final String COL_FAVORITE = "NO";

    public SLDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "create table " + TABLE_NAME + "(_id integer primary key autoincrement, "
                + COL_NAME + " TEXT, "
                + COL_MAGAZINE + " TEXT, "
                + COL_FAVORITE + " TEXT, "
                + COL_BOUGHT + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertProduct(String name, String magazine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_MAGAZINE, magazine);
        contentValues.put(COL_BOUGHT, "NO");
        contentValues.put(COL_FAVORITE, "NO");
        //Create check for true or false
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public void updateStatus(long rowID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOUGHT, "YES");
        sqLiteDatabase.update(TABLE_NAME, contentValues, "_id = ?", new String[] {String.valueOf(rowID)});
    }

    public void updateFavoriteProduct(){

    }
}
