package ua.primedeym.shoppinglist.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ua.primedeym.shoppinglist.DBHelper;
import ua.primedeym.shoppinglist.R;

public class NoteCreateActivity extends AppCompatActivity {
    private EditText title, description;
    private DBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);
        // TODO Добавить в ресурсы для англоязычной версии
        setTitle("Добавить заметку");
        helper = new DBHelper(this);
        title = (EditText) findViewById(R.id.et_note_title);
        description = (EditText) findViewById(R.id.et_note_description);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                description.setText(sharedText);
            }
        }
    }

    public void addNote(View view) {
        String textDescription = description.getText().toString();
        String textTitle = title.getText().toString();
        if (textTitle.equals("") && textDescription.equals("")) {
            Toast.makeText(this, "Название и Описание не может быть пустым", Toast.LENGTH_SHORT).show();
        } else {
            if (textTitle.equals("")) {
                textTitle = textDescription;
                if (textTitle.length() > 20) {
                    textTitle = textDescription.substring(0, 20);
                    String text = textTitle + "...";
                    helper.insertNote(text, textDescription);
                    finish();
                } else {
                    helper.insertNote(textTitle, textDescription);
                    finish();
                }
            } else {
                helper.insertNote(textTitle, textDescription);
                finish();
            }
            Toast.makeText(this, "Заметка создана", Toast.LENGTH_SHORT).show();
        }
    }
}
