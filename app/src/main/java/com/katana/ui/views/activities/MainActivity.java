package com.katana.ui.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.katana.infrastructure.support.GetterFunc;
import com.katana.ui.R;
import com.katana.ui.databinding.ActivityMainBinding;
import com.katana.ui.viewmodels.MainActivityViewModel;
import com.katana.ui.views.OnFragmentInteractionListener;
import com.katana.ui.views.fragments.AddProductFragment;
import com.katana.ui.views.fragments.CustomersFragment;
import com.katana.ui.views.fragments.InventoryFragment;
import com.katana.ui.views.fragments.ProductCategoryFragment;
import com.katana.ui.views.fragments.SaleFragment;

import java.util.HashMap;
import java.util.Map;

import static com.katana.ui.support.Constants.HIDE_MAIN_PROGRESS;
import static com.katana.ui.support.Constants.SHOW_MAIN_PROGRESS;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainActivityViewModel>
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private Map<Integer, GetterFunc<Fragment>> fragments;

    @Override
    protected int getLayoutReSource() {
        return R.layout.activity_main;
    }

    @Override
    protected MainActivityViewModel getViewModel() {
        return new MainActivityViewModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragments = new HashMap<Integer, GetterFunc<Fragment>>() {{
            put(R.id.add_product, AddProductFragment::newInstance);
            put(R.id.product_categories, ProductCategoryFragment::newInstance);
            put(R.id.sale, SaleFragment::newInstance);
            put(R.id.inventory, InventoryFragment::newInstance);
            put(R.id.customers, CustomersFragment::newInstance);
//            put(R.id.payments, PaymentsFragment::newInstance);
        }};

        MenuItem firstItem = navigationView.getMenu().getItem(0);
        firstItem.setChecked(true);
        onNavigationItemSelected(firstItem);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = fragments.get(id).get();

        if (fragment != null) {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contentFrame, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String tag, Object data) {
        switch (tag) {
            case SHOW_MAIN_PROGRESS:
                viewModel.setShowProgress(true);
                break;
            case HIDE_MAIN_PROGRESS:
                viewModel.setShowProgress(false);
                break;
        }
    }
}
