package lk.zeamac.app.ceylonhandicraft.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.zeamac.app.ceylonhandicraft.Entity.DeliveryInfoEntity;
import lk.zeamac.app.ceylonhandicraft.R;

public class AddDeliveryAddressActivity extends AppCompatActivity {

    private FirebaseFirestore fireStore;

    private FirebaseAuth firebaseAuth;
    private String selectedCountry;
    private String selectedCity;
    EditText editTextId, Suburb, StreetName, HouseNumber, homeOrOffice, deliveryDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_address);




        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        Suburb = findViewById(R.id.editTextTextSuburb);
        StreetName = findViewById(R.id.editTextStreet);
        HouseNumber = findViewById(R.id.editTextTextHouseNumber);
        homeOrOffice = findViewById(R.id.editTextTextOfficeOrHome);
        deliveryDetails = findViewById(R.id.textViewDeliveryDetails);


        Spinner countrySpinner = findViewById(R.id.countrySpinner);
        String[] itemsCountry = {"Select","Sri Lanka"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemsCountry);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        countrySpinner.setAdapter(adapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner citySpinner = findViewById(R.id.citySpinner);
        String[] itemsCity = {"Select","Ampara","Anuradhapura","Badulla","Batticaloa","Colombo","Galle","Gampaha","Hambantota","Jaffna","Kalutara","Kandy","Kegalle","Kilinochchi","Kurunegala","Mannar","Matale","Matara","Monaragala","Mullaitivu","Nuwara Eliya","Polonnaruwa","Puttalam","Ratnapura","Trincomalee","Vavuniya"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemsCity);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter1);

        citySpinner.setAdapter(adapter1);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = adapter1.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //AddProduct

        findViewById(R.id.textViewDeliveryDetailsSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String suburb = Suburb.getText().toString();
                String streetName = StreetName.getText().toString();
                String houseNumber = HouseNumber.getText().toString();
                String textLabel = homeOrOffice.getText().toString();
                String deliveryInstructions = deliveryDetails.getText().toString();

                if (selectedCountry.equals("Select")) {
                    Toast.makeText(AddDeliveryAddressActivity.this, "Please Select Your Country", Toast.LENGTH_LONG).show();
                }else if (selectedCity.equals("Select")) {
                    Toast.makeText(AddDeliveryAddressActivity.this, "Please Select  Your City", Toast.LENGTH_LONG).show();
                }  else if (suburb.isEmpty()) {
                    Toast.makeText(AddDeliveryAddressActivity.this, "Please Enter  Your Suburb", Toast.LENGTH_LONG).show();
                }  else if (streetName.isEmpty()) {
                    Toast.makeText(AddDeliveryAddressActivity.this, "Please Enter  Your Home Street ", Toast.LENGTH_LONG).show();
                } else if (houseNumber.isEmpty()) {
                    Toast.makeText(AddDeliveryAddressActivity.this, "Please Enter  Your House Number", Toast.LENGTH_LONG).show();
                } else if (textLabel.isEmpty()) {
                    Toast.makeText(AddDeliveryAddressActivity.this, "Please Enter  Home Or Office", Toast.LENGTH_LONG).show();
                }else if (deliveryInstructions.isEmpty()) {
                    Toast.makeText(AddDeliveryAddressActivity.this, "Please Enter Delivery Details", Toast.LENGTH_LONG).show();
                } else {

                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    String Id = user.getUid();

                    DocumentReference reference =    fireStore.collection("UsersAddress").document(Id);
                    DeliveryInfoEntity address = new DeliveryInfoEntity (Id,selectedCountry,selectedCity,suburb,streetName,houseNumber,textLabel,deliveryInstructions,null,null);
                    reference.set(address).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddDeliveryAddressActivity.this,"Your Delivery Data Added Successful",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddDeliveryAddressActivity.this, DeliveryLocationActivity.class);
                            startActivity(intent);
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AddDeliveryAddressActivity.this,"Failed",Toast.LENGTH_LONG).show();

                        }
                    });




                }
            }


        });

        // Current Address
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String Id = user.getUid();
        CheckBox showAddress = findViewById(R.id.setDefault);

        showAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                if (checked) {

                    fireStore.collection("UsersAddress").document(Id).get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();

                            if(snapshot.exists()){
                                DeliveryInfoEntity address = snapshot.toObject( DeliveryInfoEntity.class);
                                if(address != null){
                                    countrySpinner.setSelection(getIndex(countrySpinner,address.getCountry()));
                                    citySpinner.setSelection(getIndex(citySpinner,address.getCity()));
                                    Suburb.setText(address.getSuburb());
                                    StreetName.setText(address.getStreetName());
                                    HouseNumber.setText(address.getHouseNumber());
                                    homeOrOffice.setText(address.getLabel());
                                    deliveryDetails.setText(address.getDeliveryInstructions());
                                    Toast.makeText(AddDeliveryAddressActivity.this, "Get Your Old Address", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                } else {
                    countrySpinner.setSelection(0);
                    citySpinner.setSelection(0);
                    Suburb.setText("");
                    StreetName.setText("");
                    HouseNumber.setText("");
                    homeOrOffice.setText("");
                    deliveryDetails.setText("");
                    Toast.makeText(AddDeliveryAddressActivity.this, "Your Data Not Added Address", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Current Address

        //AddProduct


        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDeliveryAddressActivity.this, OrderInfoActivity.class);
                startActivity(intent);
            }
        });

    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }
}
