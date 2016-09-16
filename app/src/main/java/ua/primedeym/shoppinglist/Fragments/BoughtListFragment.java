package ua.primedeym.shoppinglist.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

        listView = (ListView) view.findViewById(R.id.bought_product_listView);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showReturnDialog(l);
            }
        });
        initFab();
        showProduct();

        return view;
    }

    public void showReturnDialog(final long rID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Вернуть продукт в список?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        helper.updateBoughtProduct(rID);
                        updateCursor();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        helper.deleteProduct(rID);
                        updateCursor();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    helper.dropProductTable(textTitle);
                    Toast.makeText(getContext(), "Купленые товары удалены", Toast.LENGTH_SHORT).show();
                    showProduct();
                }
            });
        }
    }

    public void showProduct() {
        db = helper.getWritableDatabase();
        try {
            cursor = db.query(SLDatabaseHelper.PRODUCTS_TABLE_NAME,
                    new String[]{"_id", SLDatabaseHelper.COL_MAGAZINE, SLDatabaseHelper.COL_NAME},
                    SLDatabaseHelper.COL_BOUGHT + " = ? and " + SLDatabaseHelper.COL_MAGAZINE + " = ?",
                    new String[]{"YES", textTitle},
                    null, null, null);
            CursorAdapter adapter = new SimpleCursorAdapter(getContext(),
                    R.layout.custom_listview_bought_fragment,
                    cursor,
                    new String[]{SLDatabaseHelper.COL_NAME},
                    new int[]{R.id.ctv_title}, 0);
            listView.setAdapter(adapter);
        } catch (SQLException e) {
            Toast.makeText(getContext(), "База данных не доступна", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCursor() {
        try {
            helper = new SLDatabaseHelper(getContext());
            db = helper.getReadableDatabase();
            cursorNew = db.query(SLDatabaseHelper.PRODUCTS_TABLE_NAME,
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
