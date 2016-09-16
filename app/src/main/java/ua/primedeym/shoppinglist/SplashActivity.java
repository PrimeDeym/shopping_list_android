package ua.primedeym.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
        finish();
        super.onCreate(savedInstanceState);
    }
}
