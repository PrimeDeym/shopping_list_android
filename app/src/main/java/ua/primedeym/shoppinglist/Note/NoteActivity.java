package ua.primedeym.shoppinglist.Note;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ua.primedeym.shoppinglist.DBHelper;
import ua.primedeym.shoppinglist.R;

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
                    null, null, null, null, DBHelper.NOTE_COL_DATA + " DESC");
            adapter = new SimpleCursorAdapter(this,
                    R.layout.custom_listview_note_activity,
                    cursor,
                    new String[]{DBHelper.NOTE_COL_NAME, DBHelper.NOTE_COL_DATA},
                    new int[]{R.id.textview_title, R.id.textview_data}, 0);
            listView.setAdapter(adapter);
        } catch (SQLException e) {
            Toast.makeText(this, "База данных не доступна", Toast.LENGTH_SHORT).show();
        }
    }

    private void initListView() {
        listView.setClickable(true);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NoteResultActivity.class);
                TextView text = (TextView) view.findViewById(R.id.textview_title);
                String title = text.getText().toString();
                intent.putExtra("title", title);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.drop_table:
//                helper.deleteNoteAllListForTest();
//                onResume();
//                Toast.makeText(this, "Заметки удалены",
//                        Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


}
