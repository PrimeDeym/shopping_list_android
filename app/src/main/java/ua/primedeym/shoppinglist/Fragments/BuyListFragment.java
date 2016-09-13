package ua.primedeym.shoppinglist.Fragments;


import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ua.primedeym.shoppinglist.R;
import ua.primedeym.shoppinglist.SLDatabaseHelper;

public class BuyListFragment extends Fragment {

//    protected View view;
    private SQLiteDatabase db;
    private Cursor cursor;
    ListView listView;
    String textTitle, listName;
    SLDatabaseHelper helper;
    CursorAdapter adapter;
    EditText inputText;

    public BuyListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);
        textTitle = getActivity().getTitle().toString();
        listView = (ListView) view.findViewById(R.id.buy_product_listView);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = (TextView) view;
                String text1 = text.getText().toString();
                Toast.makeText(getContext(), "Вы купили " + text1, Toast.LENGTH_SHORT).show();
                helper.updateStatus(l);
                updateCursor();
            }
        });
        helper = new SLDatabaseHelper(getContext());

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createProduct();
                }
            });
        }
        return view;
    }



    private void createProduct() {
        inputText = new EditText(getContext());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Добавить товар");
        alertDialog.setView(inputText);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listName = String.valueOf(inputText.getText());
                helper.insertProduct(listName, textTitle);
                updateCursor();
            }
        });
        alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    public void showProduct() {
        db = helper.getWritableDatabase();
        try {
            cursor = db.query(SLDatabaseHelper.TABLE_NAME,
                    new String[]{"_id", SLDatabaseHelper.COL_NAME},
                    SLDatabaseHelper.COL_BOUGHT + " = ? and " + SLDatabaseHelper.COL_MAGAZINE + " = ?",
                    new String[]{"NO", textTitle},
                    null, null, null);

            adapter = new SimpleCursorAdapter(getContext(),
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{SLDatabaseHelper.COL_NAME},
                    new int[]{android.R.id.text1}, 0);
            listView.setAdapter(adapter);
        }catch(SQLException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateCursor() {
        helper = new SLDatabaseHelper(getContext());
        db = helper.getWritableDatabase();
        try {
            Cursor cursorNew = db.query(SLDatabaseHelper.TABLE_NAME,
                    new String[]{"_id", SLDatabaseHelper.COL_MAGAZINE, SLDatabaseHelper.COL_NAME},
                    SLDatabaseHelper.COL_BOUGHT + " = ? and " + SLDatabaseHelper.COL_MAGAZINE + " = ?",
                    new String[]{"NO", textTitle},
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
        showProduct();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
