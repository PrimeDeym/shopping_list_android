package ua.primedeym.shoppinglist.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ua.primedeym.shoppinglist.DBHelper;
import ua.primedeym.shoppinglist.Fragments.BoughtListFragment;
import ua.primedeym.shoppinglist.Fragments.BuyListFragment;
import ua.primedeym.shoppinglist.R;

public class ShoppingListActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;
    List<Fragment> listFragment;
    List<String> titleFragment;
    String intentExtra;
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
        intentExtra = intent.getStringExtra("magazine");
        setTitle(intentExtra);

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

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BuyListFragment(), "Купить");
        adapter.addFragment(new BoughtListFragment(), "Купленные");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

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
