package ua.primedeym.shoppinglist.fragments;


import android.app.Dialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ua.primedeym.shoppinglist.DBHelper;
import ua.primedeym.shoppinglist.R;

import static android.widget.Toast.makeText;
import static ua.primedeym.shoppinglist.CONST.DELETE_MENU;
import static ua.primedeym.shoppinglist.CONST.UPDATE_MENU;

public class BuyListFragment extends Fragment {

    protected View view;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView listView;
    private String textTitle, productName;
    private DBHelper helper;
    CursorAdapter adapter;
    private EditText inputText;

    public BuyListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy, container, false);

        helper = new DBHelper(getContext());
        textTitle = getActivity().getTitle().toString();
        initFab();

        listView = (ListView) view.findViewById(R.id.buy_product_listView);
        initListView();
        return view;
    }

    private void initListView() {
        listView.setClickable(true);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RelativeLayout rl = (RelativeLayout) view; // get the parent layout view
                TextView tv = (TextView) rl.findViewById(R.id.ctv_title); // get the child text view
                makeText(getContext(), "Вы купили " + tv.getText().toString(), Toast.LENGTH_SHORT).show();
                helper.updateStatus(l);
                updateCursor();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, UPDATE_MENU, 0, R.string.edit);
        menu.add(0, DELETE_MENU, 0, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo adapter = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TextView tv = (TextView) adapter.targetView.findViewById(R.id.ctv_title);
        switch (item.getItemId()) {
            case UPDATE_MENU:
                showDialog(adapter.id, tv.getText().toString());
                return true;
            case DELETE_MENU:
                helper.deleteFromDB(adapter.id, DBHelper.PRODUCTS_TABLE_NAME);
                updateCursor();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private Dialog showDialog(final long id, String title) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.cd_title_text);
        dialogTitle.setText("Новое название");
        inputText = (EditText) dialog.findViewById(R.id.cd_edit_text);
        inputText.setText(title);
        inputText.setSelection(inputText.getText().length());
        Button addButton = (Button) dialog.findViewById(R.id.cd_button_add);
        addButton.setText("Изменить");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputText.getText().toString().equals("")) {
                    makeText(getContext(),
                            "Название не может быть пустым",
                            Toast.LENGTH_SHORT).show();
                } else {
                    helper.updateProductList(inputText.getText().toString(), id);
                    updateCursor();
                    dialog.dismiss();
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
        return dialog;
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
                    makeText(getContext(), "Название не может быть пустым", Toast.LENGTH_SHORT).show();
                } else {
                    productName = inputText.getText().toString();
                    helper.insertProduct(productName, textTitle);
                    Toast toast = Toast.makeText(getContext(), "Вы добавили товар "
                            + inputText.getText().toString(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    updateCursor();
                    inputText.setText("");
                    dialog.show();
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
        try {
            db = helper.getWritableDatabase();
            cursor = db.query(DBHelper.PRODUCTS_TABLE_NAME,
                    new String[]{"_id", DBHelper.COL_NAME},
                    DBHelper.COL_BOUGHT + " = ? and " + DBHelper.COL_MAGAZINE + " = ?",
                    new String[]{"NO", textTitle},
                    null, null, null);

            adapter = new SimpleCursorAdapter(getContext(),
                    R.layout.custom_listview_buy_fragment,
                    cursor,
                    new String[]{DBHelper.COL_NAME},
                    new int[]{R.id.ctv_title}, 0);
            listView.setAdapter(adapter);
        } catch (SQLException e) {
            makeText(getContext(), "База данных не доступна", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateCursor() {
        helper = new DBHelper(getContext());
        db = helper.getWritableDatabase();
        try {
            Cursor cursorNew = db.query(DBHelper.PRODUCTS_TABLE_NAME,
                    new String[]{"_id", DBHelper.COL_MAGAZINE, DBHelper.COL_NAME},
                    DBHelper.COL_BOUGHT + " = ? and " + DBHelper.COL_MAGAZINE + " = ?",
                    new String[]{"NO", textTitle},
                    null, null, null);
            CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
            adapter.changeCursor(cursorNew);
            cursor = cursorNew;
        } catch (SQLException e) {
            makeText(getContext(), "База не доступна", Toast.LENGTH_SHORT).show();
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
