package ua.primedeym.shoppinglist.Fragments;


import android.app.Dialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ua.primedeym.shoppinglist.R;
import ua.primedeym.shoppinglist.SLDatabaseHelper;

public class BuyListFragment extends Fragment {

    protected View view;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView listView;
    private String textTitle, productName;
    private SLDatabaseHelper helper;
    CursorAdapter adapter;
    private EditText inputText;

    public BuyListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy, container, false);

        helper = new SLDatabaseHelper(getContext());
        textTitle = getActivity().getTitle().toString();
        initFab();

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
        return view;
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createProduct();
                }
            });
        }
    }

    private void createProduct() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.cd_title_text);
        dialogTitle.setText("Название товара");
        inputText = (EditText) dialog.findViewById(R.id.cd_edit_text);
        Button addButton = (Button) dialog.findViewById(R.id.cd_button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputText.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Название товара не может быть пустым", Toast.LENGTH_SHORT).show();
                } else {
                    productName = inputText.getText().toString();
                    helper.insertProduct(productName, textTitle);
                    Toast.makeText(getContext(), "Вы добавили товар "
                            + inputText.getText().toString(), Toast.LENGTH_SHORT).show();
                    updateCursor();
                    inputText.setText(" ");
                }
            }
        });
        Button cancelButton = (Button) dialog.findViewById(R.id.cd_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showProduct() {
        db = helper.getWritableDatabase();
        try {
            cursor = db.query(SLDatabaseHelper.PRODUCTS_TABLE_NAME,
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
        } catch (SQLException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCursor() {
        helper = new SLDatabaseHelper(getContext());
        db = helper.getWritableDatabase();
        try {
            Cursor cursorNew = db.query(SLDatabaseHelper.PRODUCTS_TABLE_NAME,
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
