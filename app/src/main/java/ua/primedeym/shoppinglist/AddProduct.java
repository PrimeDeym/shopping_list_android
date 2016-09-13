package ua.primedeym.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProduct extends AppCompatActivity {

    SLDatabaseHelper slDatabaseHelper;
    Button button;
    EditText editText;
    String listName, magazine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        button = (Button) findViewById(R.id.add_product);
        editText = (EditText) findViewById(R.id.editText);
        slDatabaseHelper = new SLDatabaseHelper(this);
        magazine = getIntent().getStringExtra("title");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(AddProduct.this, "" + String.valueOf(item.getItemId()), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addProductToDB(View view) {
        listName = editText.getText().toString();
        slDatabaseHelper.insertProduct(listName, magazine);
        Toast.makeText(getApplicationContext(), "Вы создали товар " + listName, Toast.LENGTH_SHORT).show();
        editText.setText(" ");
    }
}
