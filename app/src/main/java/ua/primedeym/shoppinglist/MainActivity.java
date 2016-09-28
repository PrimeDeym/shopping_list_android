package ua.primedeym.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import ua.primedeym.shoppinglist.List.ListsActivity;
import ua.primedeym.shoppinglist.Note.NoteActivity;

public class MainActivity extends AppCompatActivity {

    TextView versionApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        versionApp();
    }

    private void versionApp(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int data  = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        versionApp = (TextView) findViewById(R.id.text_version_app);
        versionApp.setText(year + " Beta v. " + data + month);
    }

    public void startListActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ListsActivity.class);
        startActivity(intent);
    }

    public void startToDoActivity(View view) {
        startActivity(new Intent(this, NoteActivity.class));
    }
}
