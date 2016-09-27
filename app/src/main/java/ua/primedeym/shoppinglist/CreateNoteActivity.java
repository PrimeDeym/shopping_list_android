package ua.primedeym.shoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNoteActivity extends AppCompatActivity {
    private EditText title, description;
    private DBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        helper = new DBHelper(this);
        title = (EditText) findViewById(R.id.et_note_title);
        description = (EditText) findViewById(R.id.et_note_description);
    }

    public void addNote(View view) {
        helper.insertNote(title.getText().toString(), description.getText().toString());
        Toast.makeText(this, "Заметка создана", Toast.LENGTH_SHORT).show();
        finish();
    }
}