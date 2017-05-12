package ua.primedeym.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "shoppingListDB";
    private static final int DB_VERSION = 15;

    public static final String PRODUCTS_TABLE_NAME = "Products";
    public static final String PRODUCTS_COL_ID = "_id";
    public static final String PRODUCTS_COL_NAME = "NAME";
    public static final String PRODUCTS_COL_MAGAZINE = "MAGAZINE";
    public static final String PRODUCTS_COL_BOUGHT = "BOUGHT";

    public static final String MAGAZINE_TABLE_NAME = "Magazine";
    public static final String MAGAZINE_COL_ID = "_id";
    public static final String MAGAZINE_COL_NAME = "NAME";
    public static final String MAGAZINE_COL_DATA = "DATA";
    public static final String MAGAZINE_COL_TIMESTAMP = "TIMESTAMP";


    public static final String NOTE_TABLE_NAME = "Note";
    public static final String NOTE_COL_ID = "_id";
    public static final String NOTE_COL_NAME = "NAME";
    public static final String NOTE_COL_DESCRIPTION = "DESCRIPTION";
    public static final String NOTE_COL_DATA = "DATA";
    public static final String NOTE_COL_TIMESTAMP = "TIMESTAMP";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryProduct = "create table " + PRODUCTS_TABLE_NAME
                + "(" + PRODUCTS_COL_ID + " integer primary key autoincrement, "
                + PRODUCTS_COL_NAME + " TEXT, "
                + PRODUCTS_COL_MAGAZINE + " TEXT, "
                + PRODUCTS_COL_BOUGHT + " TEXT);";
        sqLiteDatabase.execSQL(queryProduct);

        String queryList = "create table " + MAGAZINE_TABLE_NAME
                + "(" + MAGAZINE_COL_ID + " integer primary key autoincrement, "
                + MAGAZINE_COL_NAME + " TEXT, "
                + MAGAZINE_COL_DATA + " DATA, "
                + MAGAZINE_COL_TIMESTAMP + " integer);";
        sqLiteDatabase.execSQL(queryList);

        String queryNote = "create table " + NOTE_TABLE_NAME
                + "(" + NOTE_COL_ID + " integer primary key autoincrement, "
                + NOTE_COL_NAME + " TEXT, "
                + NOTE_COL_DESCRIPTION + " TEXT, "
                + NOTE_COL_DATA + " DATA, "
                + NOTE_COL_TIMESTAMP + " integer);";
        sqLiteDatabase.execSQL(queryNote);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // при испралении обновления "заметок" нужно изменить версию базы
        //if (i == 12 && i1 == 13) {
//        if (i == 13 && i1 == 14) {
////            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + MAGAZINE_TABLE_NAME, null);
//            Cursor cursorNote = sqLiteDatabase.rawQuery("select * from " + NOTE_TABLE_NAME, null);
//
//            ContentValues cv = new ContentValues();
//            sqLiteDatabase.beginTransaction();
//            try {
        //update PRODUCT_TABLE and add TIMESTAMP, create temp_db
//
//                sqLiteDatabase.execSQL("alter table " + MAGAZINE_TABLE_NAME
//                        + " add column " + MAGAZINE_COL_TIMESTAMP + " integer");
//
//                for (int j = 0; j < cursor.getCount(); j++) {
//                    cv.clear();
//                    cv.put(MAGAZINE_COL_TIMESTAMP, System.currentTimeMillis());
//                    sqLiteDatabase.update(MAGAZINE_TABLE_NAME, cv, null, null);
//                }
//
//                sqLiteDatabase.execSQL("create temporary table products_temp ("
//                        + "id integer, data text, name text, timestamp integer)");

//                sqLiteDatabase.execSQL("insert into products_temp select _id, "
//                        + MAGAZINE_COL_DATA + ", "
//                        + MAGAZINE_COL_NAME + ", "
//                        + MAGAZINE_COL_TIMESTAMP + " from "
//                        + MAGAZINE_TABLE_NAME + ";");
//                sqLiteDatabase.execSQL("drop table " + MAGAZINE_TABLE_NAME);
//
//                String queryList = "create table " + MAGAZINE_TABLE_NAME
//                        + "(_id integer primary key autoincrement, "
//                        + MAGAZINE_COL_DATA + " DATA, "
//                        + MAGAZINE_COL_NAME + " TEXT, "
//                        + MAGAZINE_COL_TIMESTAMP + " integer);";
//                sqLiteDatabase.execSQL(queryList);
//
//                sqLiteDatabase.execSQL("insert into " + MAGAZINE_TABLE_NAME
//                        + " select id, data, name, timestamp from products_temp;");
//                sqLiteDatabase.execSQL("drop table products_temp");

//                //update NOTE_TABLE and add TIMESTAMP, create temp_db
//                sqLiteDatabase.execSQL("alter table " + NOTE_TABLE_NAME
//                        + " add column " + NOTE_COL_TIMESTAMP + " integer");
//
//                for (int k = 0; k < cursorNote.getCount(); k++) {
//                    cv.clear();
//                    cv.put(NOTE_COL_TIMESTAMP, System.currentTimeMillis());
//                    sqLiteDatabase.update(NOTE_TABLE_NAME, cv, null, null);
//                }
//                sqLiteDatabase.execSQL("create temporary table note_temp " +
//                        "(id integer, description text, data data, name text, timestamp integer)");
//
//                sqLiteDatabase.execSQL("insert into note_temp select _id, "
//                        + NOTE_COL_DESCRIPTION + ", "
//                        + NOTE_COL_DATA + ", "
//                        + NOTE_COL_NAME + ", "
//                        + NOTE_COL_TIMESTAMP + " from Note;");
//                sqLiteDatabase.execSQL("drop table " + NOTE_TABLE_NAME);
//
//                String queryNote = "create table " + NOTE_TABLE_NAME
//                        + "(_id integer primary key autoincrement, "
//                        + NOTE_COL_DESCRIPTION + " TEXT, "
//                        + NOTE_COL_DATA + " DATA, "
//                        + NOTE_COL_NAME + " TEXT, "
//                        + NOTE_COL_TIMESTAMP + " integer);";
//                sqLiteDatabase.execSQL(queryNote);
//
//       //         sqLiteDatabase.execSQL("insert into " + MAGAZINE_TABLE_NAME
////                        + " select id, data, name, timestamp from products_temp;");
////                sqLiteDatabase.execSQL("drop table products_temp");
//
//
//
//
//                sqLiteDatabase.execSQL("insert into " + NOTE_TABLE_NAME
//                        + " select id, description, data, name, timestamp");
//                sqLiteDatabase.execSQL("drop table note_temp");
//
//                sqLiteDatabase.setTransactionSuccessful();
//            } finally {
//                sqLiteDatabase.endTransaction();
//            }
//            cursorNote.close();
//            //cursor.close();
//        }

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public static int getDbVersion() {
        return DB_VERSION;
    }

    private String getCurrentData() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        if (month <= 9 && day > 9) {
            return day + "." + "0" + month + "." + year;
        } else if (day <= 9 && month > 9) {
            return "0" + day + "." + month + "." + year;
        } else if (day <= 9 && month <= 9) {
            return "0" + day + "." + "0" + month + "." + year;
        }
        return day + "." + month + "." + year;
    }

    public void insertNote(String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_COL_NAME, name);
        cv.put(NOTE_COL_DESCRIPTION, description);
        cv.put(NOTE_COL_DATA, getCurrentData());
        cv.put(NOTE_COL_TIMESTAMP, System.currentTimeMillis());
        db.insert(NOTE_TABLE_NAME, null, cv);
    }

    public void insertProduct(String name, String magazine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCTS_COL_NAME, name);
        contentValues.put(PRODUCTS_COL_MAGAZINE, magazine);
        contentValues.put(PRODUCTS_COL_BOUGHT, "NO");
        db.insert(PRODUCTS_TABLE_NAME, null, contentValues);
    }


    public void insertShoppingList(String magazine) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MAGAZINE_COL_NAME, magazine);
        cv.put(MAGAZINE_COL_DATA, getCurrentData());
        cv.put(MAGAZINE_COL_TIMESTAMP, System.currentTimeMillis());
        sqLiteDatabase.insert(MAGAZINE_TABLE_NAME, null, cv);
    }

    public void updateStatus(long rowID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCTS_COL_BOUGHT, "YES");
        sqLiteDatabase.update(PRODUCTS_TABLE_NAME, contentValues, "_id = ?",
                new String[]{String.valueOf(rowID)});
    }

    public void updateNote(long id, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_COL_NAME, title);
        cv.put(NOTE_COL_DESCRIPTION, description);
        db.update(NOTE_TABLE_NAME, cv, "_id = " + id, null);
    }

    public void updateList(long rowId, String newName, String oldName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MAGAZINE_COL_NAME, newName);
        cv.put(MAGAZINE_COL_DATA, getCurrentData());
        sqLiteDatabase.update(MAGAZINE_TABLE_NAME, cv, "_id = " + rowId, null);
        updateProduct(newName, oldName);
    }

    private void updateProduct(String newName, String oldName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCTS_COL_MAGAZINE, newName);
        sqLiteDatabase.update(
                PRODUCTS_TABLE_NAME, cv, PRODUCTS_COL_MAGAZINE + " = ?", new String[]{oldName});
    }

    public void updateTimeStampAndDate(long id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MAGAZINE_COL_DATA, getCurrentData());
        cv.put(MAGAZINE_COL_TIMESTAMP, System.currentTimeMillis());
        sqLiteDatabase.update(MAGAZINE_TABLE_NAME, cv, MAGAZINE_COL_ID + " = " + id, null);
    }

    public void updateProductList(String name, long id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCTS_COL_NAME, name);
        sqLiteDatabase.update(PRODUCTS_TABLE_NAME, cv, "_id = " + id, null);
    }


    public void updateBoughtProduct(long rowId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCTS_COL_BOUGHT, "NO");
        sqLiteDatabase.update(PRODUCTS_TABLE_NAME, cv, "_id = ?",
                new String[]{String.valueOf(rowId)});
    }

    public void dropProductTable(String magazine) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selections = PRODUCTS_COL_MAGAZINE + " = ? and " + PRODUCTS_COL_BOUGHT + " = ? ";
        String[] selectionArgs = new String[]{magazine, "YES"};
        sqLiteDatabase.delete(PRODUCTS_TABLE_NAME, selections, selectionArgs);
    }

    public void dropListTable() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(MAGAZINE_TABLE_NAME, null, null);
    }

    public void deleteList(long rowId, String magazine) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(MAGAZINE_TABLE_NAME, "_id = " + String.valueOf(rowId), null);
        sqLiteDatabase.delete(PRODUCTS_TABLE_NAME, PRODUCTS_COL_MAGAZINE + " = ?", new String[]{magazine});
    }

    public void deleteFromDB(long id, String dbTableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(dbTableName, "_id = " + id, null);
    }
}
