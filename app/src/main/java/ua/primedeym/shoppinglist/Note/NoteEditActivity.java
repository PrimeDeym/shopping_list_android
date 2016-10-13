package ua.primedeym.shoppinglist.Note;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ua.primedeym.shoppinglist.DBHelper;
import ua.primedeym.shoppinglist.R;

public class NoteEditActivity extends AppCompatActivity {
    private DBHelper helper;
    EditText edit_title, edit_description;
    String title, description;
    long id;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);


        bundle = getIntent().getExtras();
        id = bundle.getLong("id");
        title = bundle.getString("title");
        description = bundle.getString("description");
        helper = new DBHelper(this);

        edit_title = (EditText) findViewById(R.id.activity_edit_title);
        edit_description = (EditText) findViewById(R.id.activity_edit_description);
        edit_title.setText(title);
        edit_title.setSelection(edit_title.length());
        edit_description.setText(description);
        edit_description.setSelection(edit_description.length());

    }

    public void editNote(View view) {
        if (edit_title.getText().toString().equals("")){
            Toast.makeText(this, "Название не должно быть пустым", Toast.LENGTH_SHORT).show();
        } else {
            helper.updateNote(id, edit_title.getText().toString(), edit_description.getText().toString());
            Toast.makeText(this, "Заметка обновлена", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
