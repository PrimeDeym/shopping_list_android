package ua.primedeym.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {
    SQLiteDatabase db;
    DBHelper helper;
    Cursor cursor;
    CursorAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle("Заметки");
        helper = new DBHelper(this);
        listView = (ListView) findViewById(R.id.listview_note);

        initFab();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showNoteList();
    }

    private void showNoteList() {
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(DBHelper.NOTE_TABLE_NAME,
                    new String[]{"_id", DBHelper.NOTE_COL_NAME, DBHelper.NOTE_COL_DATA},
                    null, null, null, null, null);
            adapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{DBHelper.NOTE_COL_NAME},
                    new int[]{android.R.id.text1}, 0);
            listView.setAdapter(adapter);
        } catch (SQLException e) {
            Toast.makeText(this, "База данных не доступна", Toast.LENGTH_SHORT).show();
        }
    }

    private void initListView() {
        listView.setClickable(true);
        registerForContextMenu(listView);
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNote();
                }
            });
        }
    }

    private void createNote() {
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        startActivity(intent);

    }


}
