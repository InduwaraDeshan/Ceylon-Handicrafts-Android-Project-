package lk.zeamac.app.ceylonhandicraft.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import lk.zeamac.app.ceylonhandicraft.Adapter.CartViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Adapter.WishListViewAdapter;
import lk.zeamac.app.ceylonhandicraft.Entity.CartEntity;
import lk.zeamac.app.ceylonhandicraft.Entity.WishListEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class WishListFragment extends Fragment {

    private RecyclerView.Adapter wishListAdapter;
    private RecyclerView recyclerViewWishList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        //     LoadProduct
        ArrayList<WishListEntity> items = new ArrayList<>();
        items.add(new WishListEntity("wooden","Wooden","1000"));
        items.add(new WishListEntity("pottery","Pottery","1000"));
        items.add(new WishListEntity("elephant","Traditional","1000"));
        items.add(new WishListEntity("wooden","Wooden","1000"));
        items.add(new WishListEntity("pottery","Pottery","1000"));
        items.add(new WishListEntity("elephant","Traditional","1000"));
        items.add(new WishListEntity("wooden","Wooden","1000"));
        items.add(new WishListEntity("pottery","Pottery","1000"));
        items.add(new WishListEntity("elephant","Traditional","1000"));

        recyclerViewWishList = fragment.findViewById(R.id.favoriteView);
        recyclerViewWishList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        wishListAdapter = new WishListViewAdapter(items);
        recyclerViewWishList.setAdapter(wishListAdapter);
        //     LoadProduct



    }
}