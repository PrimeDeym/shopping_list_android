package ua.primedeym.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ua.primedeym.shoppinglist.list.ListsActivity;
import ua.primedeym.shoppinglist.note.NoteActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        versionApp();
    }

    private void versionApp() {
        TextView versionApp = (TextView) findViewById(R.id.text_version_app);
        //TODO Delete this after release
        versionApp.setText("Beta in progress");
    }

    public void startListActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ListsActivity.class);
        startActivity(intent);
    }

    public void startToDoActivity(View view) {
        startActivity(new Intent(this, NoteActivity.class));
    }
}
