package ua.primedeym.shoppinglist.Note;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import ua.primedeym.shoppinglist.DBHelper;
import ua.primedeym.shoppinglist.R;

public class NoteResultActivity extends AppCompatActivity {

    DBHelper helper;
    Cursor cursor;
    SQLiteDatabase db;
    TextView title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_result);

        Bundle bundle = getIntent().getExtras();
        setTitle(bundle.getString("Title"));
        helper = new DBHelper(this);

        title = (TextView) findViewById(R.id.note_title);
        description = (TextView) findViewById(R.id.note_description);

        noteDetail(bundle.getLong("id"));

    }

    private void noteDetail(long id) {
        try {
            db = helper.getWritableDatabase();
            cursor = db.query(DBHelper.NOTE_TABLE_NAME,
                    new String[]{"_id", DBHelper.NOTE_COL_NAME, DBHelper.NOTE_COL_DESCRIPTION, DBHelper.NOTE_COL_DATA},
                    "_id = " + id, null, null, null, null);
            if (cursor.moveToFirst()) {
                String name = cursor.getString(1);
                title.setText(name);
                String descriptionCursor = cursor.getString(2);
                description.setText(descriptionCursor);
            }
            db.close();
            cursor.close();

        } catch (SQLException e) {
            Toast.makeText(this, "База данных не доступна", Toast.LENGTH_SHORT).show();
        }
    }
}
