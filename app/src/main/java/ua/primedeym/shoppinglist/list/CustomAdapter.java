package ua.primedeym.shoppinglist.list;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ua.primedeym.shoppinglist.DBHelper;
import ua.primedeym.shoppinglist.R;


class CustomAdapter extends SimpleCursorAdapter {
    private DBHelper helper;

    CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        helper = new DBHelper(context);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        String title = cursor.getString(cursor.getColumnIndex(DBHelper.MAGAZINE_COL_NAME));
        String dateC = cursor.getString(cursor.getColumnIndex(DBHelper.MAGAZINE_COL_DATA));

//        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

        TextView name = (TextView) view.findViewById(R.id.ctv_title);
        TextView date = (TextView) view.findViewById(R.id.data_ctv);
        TextView sName = (TextView) view.findViewById(R.id.tv_short_name);
        TextView allCounts = (TextView) view.findViewById(R.id.ctv_counts_all);
        TextView boughtCount = (TextView) view.findViewById(R.id.ctv_counts_bought);
        date.setText(dateC);
        name.setText(title);
        sName.setText(shortName(name));
        boughtCount.setText(context.getString(R.string.bought) + " " + getBoughtCount(title));
        allCounts.setText(context.getString(R.string.all_counts) + " " + getCounts(title));
//        randomBackground(view, imageView);

    }

    private int getCounts(String name) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.PRODUCTS_TABLE_NAME,
                new String[]{"_id", DBHelper.COL_NAME, DBHelper.COL_BOUGHT, DBHelper.COL_MAGAZINE},
                DBHelper.COL_MAGAZINE + " = ? ",
                new String[]{name}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    private int getBoughtCount(String name) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.PRODUCTS_TABLE_NAME,
                new String[]{"_id", DBHelper.COL_NAME, DBHelper.COL_BOUGHT, DBHelper.COL_MAGAZINE},
                DBHelper.COL_BOUGHT + " = ? and " + DBHelper.COL_MAGAZINE + " = ? ",
                new String[]{"YES", name}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    private String shortName(TextView name) {
        String shortName;
        String oldName = name.getText().toString();
        if (name.length() == 1){
            shortName = oldName;
        } else {
            shortName = oldName.substring(0, 1);
        }
        return shortName.toUpperCase();
    }

//    private void randomBackground(View view, ImageView imageView){
//        int[] colors = {Color.BLUE, Color.GREEN, Color.GRAY, Color.RED, Color.BLACK};
//
//        Drawable drawable= imageView.getBackground();
//        int index = (int) (1+ Math.random()*(4));
//        drawable.setColorFilter(colors[index], PorterDuff.Mode.ADD);
//    }

}
