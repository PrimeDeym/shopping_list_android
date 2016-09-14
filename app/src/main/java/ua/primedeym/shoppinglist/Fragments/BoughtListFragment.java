package ua.primedeym.shoppinglist.Fragments;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import ua.primedeym.shoppinglist.R;
import ua.primedeym.shoppinglist.SLDatabaseHelper;

public class BoughtListFragment extends Fragment {

    protected View view;
    SQLiteDatabase db;
    SLDatabaseHelper helper;
    Cursor cursor, cursorNew;
    ListView listView;
    String textTitle;

    public BoughtListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bought_list, container, false);
        helper = new SLDatabaseHelper(getContext());
        textTitle = getActivity().getTitle().toString();

        db = helper.getWritableDatabase();
        listView = (ListView) view.findViewById(R.id.bought_product_listView);
        showProduct();
        return view;
    }

    public void showProduct() {
        cursor = db.query(SLDatabaseHelper.TABLE_NAME,
                new String[]{"_id", SLDatabaseHelper.COL_MAGAZINE, SLDatabaseHelper.COL_NAME},
                SLDatabaseHelper.COL_BOUGHT + " = ? and " + SLDatabaseHelper.COL_MAGAZINE + " = ?",
                new String[]{"YES", textTitle},
                null, null, null);
        CursorAdapter adapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{SLDatabaseHelper.COL_NAME},
                new int[]{android.R.id.text1}, 0);
        listView.setAdapter(adapter);
    }

    private void updateCursor() {
        try {
            helper = new SLDatabaseHelper(getContext());
            db = helper.getReadableDatabase();
            cursorNew = db.query(SLDatabaseHelper.TABLE_NAME,
                    new String[]{"_id", SLDatabaseHelper.COL_MAGAZINE, SLDatabaseHelper.COL_NAME},
                    SLDatabaseHelper.COL_BOUGHT + " = ? and " + SLDatabaseHelper.COL_MAGAZINE + " = ?",
                    new String[]{"YES", textTitle},
                    null, null, null);
            CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
            adapter.changeCursor(cursorNew);
            cursor = cursorNew;

        } catch (SQLException e) {
            Toast.makeText(getContext(), "База не доступна", Toast.LENGTH_SHORT).show();
        }
    }

    public void onResume() {
        super.onResume();
        showProduct();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }


}
