package ua.primedeym.shoppinglist.Fragments;


import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ua.primedeym.shoppinglist.R;
import ua.primedeym.shoppinglist.SLDatabaseHelper;
import ua.primedeym.shoppinglist.ShoppingListActivity;

public class BuyListFragment extends Fragment {

    protected View view;
    SQLiteDatabase db;
    SLDatabaseHelper helper;
    Cursor cursor, cursorNew;
    ListView listView;
    ShoppingListActivity activity;

    public BuyListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy, container, false);
        listView = (ListView) view.findViewById(R.id.buy_product_listView);
        helper = new SLDatabaseHelper(getContext());
        db = helper.getWritableDatabase();

        activity = (ShoppingListActivity) getActivity();

        showProduct();
        return view;
    }

    public void showProduct() {
        cursor = db.query(SLDatabaseHelper.TABLE_NAME, new String[]{"_id", SLDatabaseHelper.COL_NAME},
                SLDatabaseHelper.COL_BOUGHT + " = ?", new String[]{"NO"}, null, null, null);
        CursorAdapter adapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{SLDatabaseHelper.COL_NAME},
                new int[]{android.R.id.text1}, 0);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = (TextView) view;
                String text1 = text.getText().toString();
                Toast.makeText(getContext(), "Вы купили " + text1, Toast.LENGTH_SHORT).show();
                helper.updateStatus(l);
                onResume();

            }
        });
    }

    public void updateCursor(){
        try {
            SLDatabaseHelper helperNew = new SLDatabaseHelper(getContext());
            db = helperNew.getReadableDatabase();
            Cursor cursorNew = db.query(SLDatabaseHelper.TABLE_NAME,
                    new String[]{"_id", SLDatabaseHelper.COL_NAME},
                    SLDatabaseHelper.COL_BOUGHT + " = ?", new String[]{"NO"},
                    null, null, null);
            CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
            adapter.changeCursor(cursorNew);
            cursor = cursorNew;
        } catch (SQLException e) {
            Toast.makeText(getContext(), "База не доступна", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCursor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        cursorNew.close();
        db.close();
    }
}
