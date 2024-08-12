package lk.zeamac.app.ceylonhandicraft.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lk.zeamac.app.ceylonhandicraft.Activity.SingleProductViewActivity;
import lk.zeamac.app.ceylonhandicraft.Adapter.CategoryViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Adapter.ProductViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Adapter.ProductViewAdapter2;
import lk.zeamac.app.ceylonhandicraft.Entity.CategoryEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.ProductEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class ProductListFragment extends Fragment implements CategoryViewAdapter.OnCategoryClickListener ,ProductViewAdapter2.OnProductClickListener {
    private ProductViewAdapter productAdapter;
    private ProductViewAdapter2 productAdapter2;
    private RecyclerView recyclerViewProductView;
    private SearchView searchView;
    private ArrayList<ProductEntity> productItems;
    private boolean isGridLayout = false;
    private ArrayList<CategoryEntity> categoryItems;
    private FirebaseFirestore fireStore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        fireStore = FirebaseFirestore.getInstance();

        searchView = fragment.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (isGridLayout) {
                    filterGrid(newText, productAdapter2);
                } else {
                    filterList(newText, productAdapter);

                }
                return false;
            }
        });






        //Load Category
        categoryItems = new ArrayList<>();
        RecyclerView itemView = fragment.findViewById(R.id.categoryView);
        CategoryViewAdapter categoryAdapter = new CategoryViewAdapter(categoryItems, getActivity(), this::OnCategoryClick);
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

        //Load Category


        //     View Product

        productItems = new ArrayList<>();
        recyclerViewProductView = fragment.findViewById(R.id.allProductView);
        recyclerViewProductView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        productAdapter = new ProductViewAdapter(productItems, getActivity(),this::OnProductClick);
        recyclerViewProductView.setAdapter(productAdapter);

        fireStore.collection("Products")
                //                .limit(5)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange change : value.getDocumentChanges()) {
                            ProductEntity product = change.getDocument().toObject(ProductEntity.class);
                            switch (change.getType()) {
                                case ADDED:
                                    productItems.add(product);
                                case MODIFIED:
                                    ProductEntity old = productItems.stream().filter(i -> i.getId()
                                                    .equals(product.getId()))
                                            .findFirst()
                                            .orElse(null);


                                    if (old != null) {
                                        old.setName(product.getName());
                                        old.setDescription(product.getDescription());
                                        old.setCategory(product.getCategory());
                                        old.setQty(product.getQty());
                                        old.setPrice(product.getPrice());
                                        old.setImage(product.getImage());
                                    }
                                    break;
                                case REMOVED:
                                    productItems.remove(product);
                            }
                        }

                        productAdapter.notifyDataSetChanged();
                        Bundle bundle = getArguments();
                        if (bundle != null) {
                            OnCategorySearch(bundle.getString("categoryType"));
                        }

                    }
                });


        //     View Product




        fragment.findViewById(R.id.imageViewListProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGridLayout = false;
                switchToLayout(1);
            }
        });

        fragment.findViewById(R.id.imageViewGridViewProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGridLayout = true;
                switchToLayout(2);
            }
        });


    }



    private void switchToLayout(int spanCount) {
        recyclerViewProductView = requireView().findViewById(R.id.allProductView);
        recyclerViewProductView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount, GridLayoutManager.VERTICAL, false));

        if (isGridLayout) {
            productAdapter2 = new ProductViewAdapter2(productItems, getActivity(),this::OnProductClick);
            recyclerViewProductView.setAdapter(productAdapter2);
        } else {
            productAdapter = new ProductViewAdapter(productItems, getActivity(),this::OnProductClick);
            recyclerViewProductView.setAdapter(productAdapter);
        }
    }


    private void filterList(String text, ProductViewAdapter adapter) {
        if (productItems == null || adapter == null) {
            return;
        }
        ArrayList<ProductEntity> filteredItems = new ArrayList<>();
        for (ProductEntity product : productItems) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredItems.add(product);
            }
        }
        getActivity().runOnUiThread(() -> {
            if (filteredItems.isEmpty()) {
                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilterList(filteredItems);
            }
        });
    }

    private void filterGrid(String text, ProductViewAdapter2 adapter) {
        if (productItems == null || adapter == null) {
            return;
        }
        ArrayList<ProductEntity> filteredItems = new ArrayList<>();
        for (ProductEntity product : productItems) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredItems.add(product);
            }
        }
        getActivity().runOnUiThread(() -> {
            if (filteredItems.isEmpty()) {
                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilterList(filteredItems);
            }
        });
    }


    @Override
    public void OnCategoryClick(String categoryType) {

        ArrayList<ProductEntity> filteredList = new ArrayList<>();
        for (ProductEntity product : productItems) {
            if (product.getCategory().equals(categoryType)) {
                filteredList.add(product);
                System.out.println(product.getCategory());
            }
        }
        if (isGridLayout) {
            productAdapter2.setFilterList(filteredList);
        } else {
            productAdapter.setFilterList(filteredList);
        }

    }

    private void OnCategorySearch(String categoryType) {

        ArrayList<ProductEntity> filteredList = new ArrayList<>();
        for (ProductEntity product : productItems) {

            if (product.getCategory().equals(categoryType)) {
                filteredList.add(product);
            }
            productAdapter.setFilterList(filteredList);
        }

    }


    @Override
    public void OnProductClick(String product) {

        Intent intent = new Intent(getActivity(),SingleProductViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("productId", product);
        intent.putExtras(bundle);
        startActivity(intent);


    }
}