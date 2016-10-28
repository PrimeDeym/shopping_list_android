package ua.primedeym.shoppinglist.note;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import ua.primedeym.shoppinglist.DBHelper;
import ua.primedeym.shoppinglist.R;

public class NoteResultActivity extends AppCompatActivity {

    DBHelper helper;
    Cursor cursor;
    SQLiteDatabase db;
    private TextView data, description;
    long id;
    private String titleNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_result);

        Bundle bundle = getIntent().getExtras();
        helper = new DBHelper(this);

        data = (TextView) findViewById(R.id.note_data);
        description = (TextView) findViewById(R.id.note_description);
        id = bundle.getLong("id");
        titleNote = bundle.getString("title");
        setTitle(titleNote);
        noteDetail(id);

    }

    @Override
    protected void onResume() {
        super.onResume();
        noteDetail(id);
    }

    private void noteDetail(long id) {
        try {
            db = helper.getWritableDatabase();
            cursor = db.query(DBHelper.NOTE_TABLE_NAME,
                    new String[]{"_id", DBHelper.NOTE_COL_NAME, DBHelper.NOTE_COL_DESCRIPTION, DBHelper.NOTE_COL_DATA},
                    "_id = " + id, null, null, null, null);
            if (cursor.moveToFirst()) {
//                String name = cursor.getString(1);
//                title.setText(name);
                String dataCreated = cursor.getString(3);
                data.setText(getString(R.string.created) + dataCreated);
                String descriptionCursor = cursor.getString(2);
                description.setText(descriptionCursor);
            }
            db.close();
            cursor.close();
        } catch (SQLException e) {
            Toast.makeText(this, "База данных не доступна", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_note:
                Intent intent = new Intent(getApplicationContext(), NoteEditActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("title", titleNote);
                intent.putExtra("description", description.getText());
                startActivity(intent);
                return true;
            case R.id.share_icon:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, description.getText().toString());
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                return true;
            case R.id.delete_note:
                helper.deleteNote(id);
                Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
