package lk.zeamac.app.ceylonhandicraft.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import lk.zeamac.app.ceylonhandicraft.Activity.OrderInfoActivity;
import lk.zeamac.app.ceylonhandicraft.Adapter.AllCategoryViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Adapter.BestDealViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Adapter.CategoryViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Entity.AllCategoryEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.BestDealEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.CategoryEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.DeliveryInfoEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.UserEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class DashboardFragment extends Fragment implements CategoryViewAdapter.OnCategoryClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private RecyclerView.Adapter  bestDealAdapter, allCategoryAdapter;
    private RecyclerView  recyclerViewBestDeal, recyclerViewAllCategory;
    private SearchView searchView;
    ArrayList<CategoryEntity> categoryItems;

    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;

    private TextView textInputLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        view.findViewById(R.id.textInputSearch).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new ProductListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        textInputLocation= fragment.findViewById(R.id.textInputLocation);

        fragment.findViewById(R.id.textInputLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderInfoActivity.class));
            }
        });

        fragment.findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,new CartFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



//        View Current Location
        FirebaseUser user1 = firebaseAuth.getCurrentUser();
        String Id = user1.getUid();
        fireStore.collection("UsersAddress").document(Id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot snapshot = task.getResult();

                if(snapshot.exists()){
                    DeliveryInfoEntity address = snapshot.toObject( DeliveryInfoEntity.class);
                    if(address != null){
                        textInputLocation.setText(address.getCity()+" , "+address.getSuburb()+" , "+address.getStreetName());
                    }else{
                        textInputLocation.setHint("Change");
                    }
                }
            }
        });
//        View Current Location




        //Cart Items
//        TextView quantityTextView = fragment.findViewById(R.id.quantityTextView);
       TextView cartItems = fragment.findViewById(R.id.cartItems);

        FirebaseUser currentUser1 = firebaseAuth.getCurrentUser();
        if (currentUser1 != null) {
            CollectionReference cartRef = fireStore.collection("Cart");
            Query query = cartRef.whereEqualTo("userId", currentUser1.getUid());
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String cartSize = String.valueOf(queryDocumentSnapshots.size());
                        cartItems.setText(cartSize);
                    }
                }
            });
        }
        //Cart Items








        // Image Slider

        ImageSlider imageSlider = fragment.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.slideimg1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slideimg3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slideimg2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slideimg4, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        // Image Slider


//       Side Navigation

        drawerLayout = getActivity().findViewById(R.id.drawerLayout);
        navigationView = getActivity().findViewById(R.id.navigationView);
        toolbar = fragment.findViewById(R.id.toolBar);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.userNameH);
        TextView userEmail = headerView.findViewById(R.id.userEmailH);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            fireStore.collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserEntity userEntity = document.toObject(UserEntity.class);
                        if (userEntity != null) {
                            userName.setText(userEntity.getName());
                            userEmail.setText(userEntity.getEmail());
                        }
                    } else {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            userName.setText(user.getDisplayName());
                            userEmail.setText(user.getEmail());
                        }
                    }
                }
            });
        }





        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getActivity());

//      Side  Navigation




        //     View Category
        categoryItems = new ArrayList<>();
        RecyclerView itemView = fragment.findViewById(R.id.categoryView);
        CategoryViewAdapter categoryAdapter = new CategoryViewAdapter(categoryItems, getActivity(),this::OnCategoryClick);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        itemView.setLayoutManager(linearLayoutManager);
        itemView.setAdapter(categoryAdapter);

        fireStore.collection("Categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange change : value.getDocumentChanges()) {
                    CategoryEntity category = change.getDocument().toObject(CategoryEntity.class);
                    switch (change.getType()) {
                        case ADDED:
                            categoryItems.add(category);
                        case MODIFIED:
                            CategoryEntity old = categoryItems.stream().filter(i -> i.getId()
                                            .equals(category.getId()))
                                    .findFirst()
                                    .orElse(null);


                            if (old != null) {
                                old.setName(category.getName());
                                old.setImagePath(category.getImagePath());
                            }
                            break;
                        case REMOVED:
                            categoryItems.remove(category);
                    }
                }

                categoryAdapter.notifyDataSetChanged();
            }
        });


        //     View Category

        // shop Now Button
        fragment.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,new ProductListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        // shop Now Button



        //     LoadBestDeal
        ArrayList<BestDealEntity> bestItems = new ArrayList<>();
        bestItems.add(new BestDealEntity("wooden", "Wooden", "Good Product", "1000", 1));
        bestItems.add(new BestDealEntity("pottery", "Pottery", "Good Product", "1500", 2));
        bestItems.add(new BestDealEntity("elephant", "Traditional", "Good Product", "2000", 3));
        bestItems.add(new BestDealEntity("wooden", "Wooden", "Good Product", "1000", 1));
        bestItems.add(new BestDealEntity("pottery", "Pottery", "Good Product", "1500", 2));
        bestItems.add(new BestDealEntity("elephant", "Traditional", "Good Product", "2000", 3));
        bestItems.add(new BestDealEntity("wooden", "Wooden", "Good Product", "1000", 1));
        bestItems.add(new BestDealEntity("pottery", "Pottery", "Good Product", "1500", 2));
        bestItems.add(new BestDealEntity("elephant", "Traditional", "Good Product", "2000", 3));

        recyclerViewBestDeal = fragment.findViewById(R.id.bestDealView);
        recyclerViewBestDeal.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        bestDealAdapter = new BestDealViewAdapter(bestItems);
        recyclerViewBestDeal.setAdapter(bestDealAdapter);
        //     LoadBestDeal



        //     LoadAllCategoryWithShopNow
        ArrayList<AllCategoryEntity> AllCategoryItems = new ArrayList<>();
        AllCategoryItems.add(new AllCategoryEntity("wooden", "Wooden", "BestProduct ", "1000"));
        AllCategoryItems.add(new AllCategoryEntity("pottery", "Pottery", "BestProduct ", "1500"));
        AllCategoryItems.add(new AllCategoryEntity("wooden", "Wooden", "BestProduct ", "1000"));
        AllCategoryItems.add(new AllCategoryEntity("pottery", "Pottery", "BestProduct ", "1500"));
        AllCategoryItems.add(new AllCategoryEntity("elephant", "Traditional", "BestProduct ", "2000"));
        AllCategoryItems.add(new AllCategoryEntity("elephant", "Traditional", "BestProduct ", "2000"));

        recyclerViewAllCategory = fragment.findViewById(R.id.allCategoryView);
        recyclerViewAllCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        allCategoryAdapter = new AllCategoryViewAdapter(AllCategoryItems);
        recyclerViewAllCategory.setAdapter(allCategoryAdapter);
        //     LoadAllCategoryWithShopNow


    }


    @Override
    public void OnCategoryClick(String categoryType) {
        ProductListFragment productListFragment = new ProductListFragment();

        Bundle bundle = new Bundle();
        bundle.putString("categoryType", categoryType);
        productListFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, productListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


}
