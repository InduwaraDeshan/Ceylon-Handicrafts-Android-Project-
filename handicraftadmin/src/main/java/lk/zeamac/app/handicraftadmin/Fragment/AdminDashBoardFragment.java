package lk.zeamac.app.handicraftadmin.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.zeamac.app.handicraftadmin.Activity.MainActivity;
import lk.zeamac.app.handicraftadmin.Activity.SignInActivity;
import lk.zeamac.app.handicraftadmin.R;


public class AdminDashBoardFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_dash_board, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);


        fragment.findViewById(R.id.adminLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), SignInActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        fragment.findViewById(R.id.adminAddCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CategoryFragment());
            }
        });
        fragment.findViewById(R.id.addProductAdmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProductFragment());
            }
        });

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.adminContainerView, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}