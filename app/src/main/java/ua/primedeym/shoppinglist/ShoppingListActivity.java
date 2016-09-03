package ua.primedeym.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ua.primedeym.shoppinglist.Fragments.BoughtListFragment;
import ua.primedeym.shoppinglist.Fragments.BuyListFragment;

public class ShoppingListActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;
    List<Fragment> listFragment;
    String intentExtra, title, listName;
    SLDatabaseHelper helper;
    EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        initFab();
        listFragment = new ArrayList<>();
        helper = new SLDatabaseHelper(this);

        Intent intent = getIntent();
        intentExtra = intent.getStringExtra("magazine");
        setTitle(intentExtra);

        title = getTitle().toString();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment frag = listFragment.get(tab.getPosition());
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

    public void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createList();
                }
            });
        }
    }

    private void createList() {
        inputText = new EditText(this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Добавить товар");
        alertDialog.setView(inputText);
        alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listName = String.valueOf(inputText.getText());
                helper.insertProduct(listName, title);
                onResume();
                Toast.makeText(getApplicationContext(), "Вы создали товар " + listName, Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BuyListFragment(), "Купить");
        adapter.addFragment(new BoughtListFragment(), "Купленные");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        List<String> titleFragment = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
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

        public void addFragment(Fragment fragment, String title) {
            listFragment.add(fragment);
            titleFragment.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleFragment.get(position);
        }
    }
}
