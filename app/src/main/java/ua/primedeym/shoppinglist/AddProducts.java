package ua.primedeym.shoppinglist;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddProducts extends AppCompatActivity {

    SQLiteDatabase db;
    EditText editText;
    SLDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        editText = (EditText) findViewById(R.id.editText);
        databaseHelper = new SLDatabaseHelper(this);

    }

    public void addProductsToList(View view) {
        String text = editText.getText().toString();
        try {
            databaseHelper.insertProduct(text, "magazine");
            Toast.makeText(AddProducts.this, "Вы успешно добавили товар в список покупок", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(AddProducts.this, "База данных не доступна", Toast.LENGTH_SHORT).show();
        }
        editText.setText(" ");
    }

    public void addProductToFavorite(View view) {
        String text = editText.getText().toString();
        try {
            Toast.makeText(AddProducts.this, "Товар добавлен в избранное", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(AddProducts.this, "База данных не доступна", Toast.LENGTH_SHORT).show();
        }
    }
}
