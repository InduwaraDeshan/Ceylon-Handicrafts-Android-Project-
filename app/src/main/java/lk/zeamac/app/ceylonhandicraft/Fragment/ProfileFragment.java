package lk.zeamac.app.ceylonhandicraft.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import lk.zeamac.app.ceylonhandicraft.Activity.MainActivity;
import lk.zeamac.app.ceylonhandicraft.Activity.OrderActivity;
import lk.zeamac.app.ceylonhandicraft.Activity.ProfileInformationActivity;
import lk.zeamac.app.ceylonhandicraft.Activity.SignInActivity;
import lk.zeamac.app.ceylonhandicraft.R;

public class ProfileFragment extends Fragment {

    SharedPreferences sharedPreferences;

    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(SignInActivity.PREFS_NAME, 0);

        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(getActivity(), gOptions);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);





    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        fragment.findViewById(R.id.imageViewProfileInformation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileInformationActivity.class));

            }
        });

        fragment.findViewById(R.id.imageViewOrderListView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderActivity.class));

            }
        });

        fragment.findViewById(R.id.imageViewLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SignInActivity.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("hasLoggedIn", false);
                        editor.commit();



                        startActivity(new Intent(getActivity(), SignInActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                    }
                });
            }
        });


    }




}