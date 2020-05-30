package com.prescywallet.presdigi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prescywallet.presdigi.Abstract.Converter;
import com.prescywallet.presdigi.Model.CartItem;
import com.prescywallet.presdigi.database.CartDbHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    BottomNavigationView navigation;
    String imgUrl, email;
    private List<CartItem> cartItems;
    private static int cart_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new HomeFragment());

        //DETERMINE WHO STARTED THIS ACTIVITY
        Intent intent = this.getIntent();

        if(intent != null) {
                imgUrl = intent.getStringExtra("imageUrl");
        }

        cartItems = new CartDbHelper(this).getCart();
        cart_count = cartItems.size();
        invalidateOptionsMenu();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cartItems = new CartDbHelper(this).getCart();
        cart_count = cartItems.size();
        invalidateOptionsMenu();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu, menu);

        // Get the notifications MenuItem and LayerDrawable (layer-list)
        MenuItem notification = menu.findItem(R.id.action_notifications);
        notification.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                return true;
            }
        });

        MenuItem cart = menu.findItem(R.id.action_cart);
        cart.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.mipmap.ic_cart_white_icon));
        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                return true;
            }
        });

        return true;
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    return loadFragment(fragment);
                case R.id.navigation_dashboard:
                    fragment = new CapturePrescriptionFragment();
                    return loadFragment(fragment);
                case R.id.navigation_prescription:
                    fragment = new PrescriptionStatusFragment();
                    return loadFragment(fragment);
                case R.id.navigation_account:
                    fragment = new AccountSettingsFragment();
                    Bundle args = new Bundle();
                    args.putString("profileImageUrl", imgUrl);
                    fragment.setArguments(args);
                    return loadFragment(fragment);
            }
            return false;
        }
    };

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
