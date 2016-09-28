package ua.primedeym.shoppinglist.Note;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ua.primedeym.shoppinglist.DBHelper;
import ua.primedeym.shoppinglist.R;

public class CreateNoteActivity extends AppCompatActivity {
    private EditText title, description;
    private DBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);

        helper = new DBHelper(this);
        title = (EditText) findViewById(R.id.et_note_title);
        description = (EditText) findViewById(R.id.et_note_description);
    }

    public void addNote(View view) {
        if (title.getText().toString().equals("")) {
            Toast.makeText(this, "Назнавние заметки не может быть пустым", Toast.LENGTH_SHORT).show();
        } else {
            helper.insertNote(title.getText().toString(), description.getText().toString());
            Toast.makeText(this, "Заметка создана", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
