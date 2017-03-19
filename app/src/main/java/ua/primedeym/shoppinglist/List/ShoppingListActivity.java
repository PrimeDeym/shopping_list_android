package ua.primedeym.shoppinglist.list;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ua.primedeym.shoppinglist.DBHelper;
import ua.primedeym.shoppinglist.fragments.BoughtListFragment;
import ua.primedeym.shoppinglist.fragments.BuyListFragment;
import ua.primedeym.shoppinglist.R;

public class ShoppingListActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;
    List<Fragment> listFragment;
    List<String> titleFragment;
    String intentMagazine;
    DBHelper helper;
    Fragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        listFragment = new ArrayList<>();
        titleFragment = new ArrayList<>();
        helper = new DBHelper(this);

        Intent intent = getIntent();
        intentMagazine = intent.getStringExtra("magazine");
        setTitle(intentMagazine);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                frag = listFragment.get(tab.getPosition());
                frag.onResume();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shoppinglist_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_icon:
                shareProductList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareProductList() {
        Intent intent = new Intent();
        String shareText = "";
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.query(DBHelper.PRODUCTS_TABLE_NAME,
                    new String[]{"_id", DBHelper.COL_NAME, DBHelper.COL_BOUGHT, DBHelper.COL_MAGAZINE},
                    DBHelper.COL_BOUGHT + " = ? and " + DBHelper.COL_MAGAZINE + " = ? ",
                    new String[]{"NO", intentMagazine}, null, null, null);
            int numRow = cursor.getCount();
            for (int i = 0; i < numRow; i++) {
                cursor.moveToNext();
                String name = cursor.getString(1);
                shareText += i + 1 + ". " + name + ". <br>";
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, "База не доступна", Toast.LENGTH_SHORT).show();
        }

        String plain = Html.fromHtml(shareText).toString();
        intent.setAction(Intent.ACTION_SEND);
        if (plain.equals("")) {
            Toast.makeText(this, "Вы не можете отправить пустой список", Toast.LENGTH_SHORT).show();
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, plain);
            intent.setType("text/plain");
            startActivity(intent);
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BuyListFragment(), "Купить");
        adapter.addFragment(new BoughtListFragment(), "Купленные");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }

        void addFragment(Fragment fragment, String title) {
            listFragment.add(fragment);
            titleFragment.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleFragment.get(position);
        }
    }
}
