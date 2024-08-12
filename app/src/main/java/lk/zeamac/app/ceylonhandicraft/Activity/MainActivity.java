package lk.zeamac.app.ceylonhandicraft.Activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import lk.zeamac.app.ceylonhandicraft.Fragment.CartFragment;
import lk.zeamac.app.ceylonhandicraft.Fragment.DashboardFragment;
import lk.zeamac.app.ceylonhandicraft.Fragment.ProductListFragment;
import lk.zeamac.app.ceylonhandicraft.Fragment.ProfileFragment;
import lk.zeamac.app.ceylonhandicraft.Fragment.WishListFragment;
import lk.zeamac.app.ceylonhandicraft.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;

    SharedPreferences sharedPreferences;
    private NavigationView navigationView;

    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;
    String productId, productQty;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        drawerLayout = findViewById(R.id.drawerLayout);
        bottomNavigationView.setOnItemSelectedListener(this);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);


        sharedPreferences = getSharedPreferences(SignInActivity.PREFS_NAME, 0);

        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getString("productId");
        }


    }

    private boolean isBottomNavigationItemSelected = false;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (!isBottomNavigationItemSelected) {
            isBottomNavigationItemSelected = true;

            if (itemId == R.id.bottomNavHome || itemId == R.id.sideNavHome) {
                loadFragment(new DashboardFragment());
                bottomNavigationView.setSelectedItemId(R.id.bottomNavHome);


            } else if (itemId == R.id.bottomNavProfile || itemId == R.id.sideNavProfile) {
                loadFragment(new ProfileFragment());
                bottomNavigationView.setSelectedItemId(R.id.bottomNavProfile);



            } else if (itemId == R.id.sideNavAboutUs) {
                Intent intent = new Intent(MainActivity.this, AboutusActivity.class);
                startActivity(intent);


            } else if (itemId == R.id.bottomNavCart || itemId == R.id.sideNavCart) {
                loadFragment(new CartFragment());
                bottomNavigationView.setSelectedItemId(R.id.bottomNavCart);


            } else if (itemId == R.id.bottomNavFavorite || itemId == R.id.sideNavWishlist) {
                loadFragment(new WishListFragment());
                bottomNavigationView.setSelectedItemId(R.id.bottomNavFavorite);


            } else if (itemId == R.id.bottomNavCategory || itemId == R.id.sideNavCategory) {
                loadFragment(new ProductListFragment());
                bottomNavigationView.setSelectedItemId(R.id.bottomNavCategory);


            } else if (itemId == R.id.sideNavOrders) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.sideNavLogout) {

                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();

                        SharedPreferences sharedPreferences = getSharedPreferences(SignInActivity.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("hasLoggedIn", false);
                        editor.commit();

                        finish();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    }
                });
            } else {

            }

            drawerLayout.closeDrawer(GravityCompat.START);
            isBottomNavigationItemSelected = false;
        }
        return true;

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}