package ua.primedeym.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SLDatabaseHelper helper;
    SQLiteDatabase db;
    Cursor cursor;
    ListView listView;
    CursorAdapter adapter;
    String listName;
    EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFab();
        helper = new SLDatabaseHelper(this);
        listView = (ListView) findViewById(R.id.main_list_view);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ShoppingListActivity.class);
                TextView text = (TextView) view;
                String text1 = text.getText().toString();
                intent.putExtra("magazine", text1);
                startActivity(intent);
            }
        });
        showShoppingList();
    }

    public void showShoppingList() {
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(SLDatabaseHelper.MAGAZINE_TABLE_NAME,
                    new String[]{"_id", SLDatabaseHelper.MAGAZINE_COL_NAME},
                    null, null, null, null, null);
            adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                    cursor, new String[]{SLDatabaseHelper.MAGAZINE_COL_NAME}, new int[]{android.R.id.text1}, 0);
            listView.setAdapter(adapter);
        } catch (SQLException e) {
            Toast.makeText(MainActivity.this, "База данных не доступна", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateCursor() {
        try {
            helper = new SLDatabaseHelper(this);
            db = helper.getReadableDatabase();
            Cursor cursorNew = db.query(SLDatabaseHelper.MAGAZINE_TABLE_NAME,
                    new String[]{"_id", SLDatabaseHelper.MAGAZINE_COL_NAME},
                    null, null, null, null, null);
            CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
            adapter.changeCursor(cursorNew);
            cursor = cursorNew;
        } catch (SQLException e) {
            Toast.makeText(this, "База не доступна", Toast.LENGTH_SHORT).show();
        }
    }


    private void createList() {
        inputText = new EditText(this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Создать список покупок");
        alertDialog.setView(inputText);
        alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listName = String.valueOf(inputText.getText());
                helper.insertShoppingList(listName);
                onResume();
                Toast.makeText(MainActivity.this, "Вы создали список " + listName, Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
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
            case R.id.menu_add_products:
                helper.dropListTable();
                onResume();
                Toast.makeText(MainActivity.this, "База данных списков покупок обнулена", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.drop_table:
                helper.dropProductTable();
                onResume();
                Toast.makeText(MainActivity.this, "База данных обнулена", Toast.LENGTH_SHORT).show();
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
