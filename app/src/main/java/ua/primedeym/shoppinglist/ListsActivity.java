package ua.primedeym.shoppinglist;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import static android.widget.AdapterView.AdapterContextMenuInfo;
import static android.widget.AdapterView.OnItemClickListener;


public class ListsActivity extends AppCompatActivity {
    SLDatabaseHelper helper;
    SQLiteDatabase db;
    Cursor cursor;
    ListView listView;
    CursorAdapter adapter;
    String listName;
    EditText inputText;
    public static final int UPDATE_MENU = 1;
    public static final int DELETE_MENU = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        setTitle("Списки покупок");
        helper = new SLDatabaseHelper(this);

        initListView();
        initFab();
        showShoppingList();

    }

    private void initListView(){
        listView = (ListView) findViewById(R.id.main_list_view);
        listView.setClickable(true);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RelativeLayout ll = (RelativeLayout) view; // get the parent layout view
                TextView tv = (TextView) ll.findViewById(R.id.ctv_title); // get the child text view
                final String text = tv.getText().toString();
                Intent intent = new Intent(getApplicationContext(), ShoppingListActivity.class);
                intent.putExtra("magazine", text);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, UPDATE_MENU, 0, "Изменить имя списка");
        menu.add(0, DELETE_MENU, 0, "Удалить список");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo adapter = (AdapterContextMenuInfo) item.getMenuInfo();
        AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
        TextView tv = (TextView) adapterContextMenuInfo.targetView.findViewById(R.id.ctv_title);
        final String oldName = tv.getText().toString();
        switch (item.getItemId()){
            case UPDATE_MENU:
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);
                TextView dialogTitle = (TextView) dialog.findViewById(R.id.cd_title_text);
                dialogTitle.setText("Новое название списка");
                inputText = (EditText) dialog.findViewById(R.id.cd_edit_text);
                Button addButton = (Button) dialog.findViewById(R.id.cd_button_add);
                addButton.setText("Изменить");
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (inputText.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Название товара не может быть пустым",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            helper.updateList(adapter.id, inputText.getText().toString(), oldName);
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

                return true;
            case DELETE_MENU:
                helper.deleteList(adapterContextMenuInfo.id, tv.getText().toString());
                updateCursor();
                return true;
        }

        // TODO DELETE
//        if (item.getItemId() == DELETE_MENU) {
//            AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
//            TextView tv = (TextView) adapterContextMenuInfo.targetView.findViewById(R.id.ctv_title);
//            helper.deleteList(adapterContextMenuInfo.id, tv.getText().toString());
//            updateCursor();
//            return true;
//        }
        return super.onContextItemSelected(item);
    }

    public void showShoppingList() {
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(SLDatabaseHelper.MAGAZINE_TABLE_NAME,
                    new String[]{"_id", SLDatabaseHelper.MAGAZINE_COL_NAME, SLDatabaseHelper.MAGAZINE_COL_DATA},
                    null, null, null, null, null);
            adapter = new SimpleCursorAdapter(this,
                    R.layout.custom_listview_lists_activity,
                    cursor,
                    new String[]{SLDatabaseHelper.MAGAZINE_COL_NAME, SLDatabaseHelper.MAGAZINE_COL_DATA},
                    new int[]{R.id.ctv_title, R.id.data_ctv}, 0);
            listView.setAdapter(adapter);
        } catch (SQLException e) {
            Toast.makeText(ListsActivity.this, "База данных не доступна", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateCursor() {
        try {
            helper = new SLDatabaseHelper(this);
            db = helper.getReadableDatabase();
            Cursor cursorNew = db.query(SLDatabaseHelper.MAGAZINE_TABLE_NAME,
                    new String[]{"_id", SLDatabaseHelper.MAGAZINE_COL_NAME, SLDatabaseHelper.MAGAZINE_COL_DATA},
                    null, null, null, null, null);
            CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
            adapter.changeCursor(cursorNew);
            cursor = cursorNew;
        } catch (SQLException e) {
            Toast.makeText(this, "База не доступна", Toast.LENGTH_SHORT).show();
        }
    }


    private void createList() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        inputText = (EditText) dialog.findViewById(R.id.cd_edit_text);
        TextView dialogTitle = (TextView) dialog.findViewById(R.id.cd_title_text);
        dialogTitle.setText("Название списка");
        Button addButton = (Button) dialog.findViewById(R.id.cd_button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputText.getText().toString().equals("")) {
                    Toast.makeText(ListsActivity.this, "Нельзя создавать пустой список", Toast.LENGTH_SHORT).show();
                } else {
                    listName = inputText.getText().toString();
                    helper.insertShoppingList(listName);
                    Toast.makeText(ListsActivity.this, "Вы создали список " + listName, Toast.LENGTH_SHORT).show();
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

    public void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createList();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drop_table:
                helper.dropListTable();
                onResume();
                Toast.makeText(ListsActivity.this, "База данных списков покупок обнулена",
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCursor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
