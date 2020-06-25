package com.desirestodesigns.groceries.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.desirestodesigns.groceries.datamodel.Address;
import com.desirestodesigns.groceries.datamodel.CustomerRegistrationForm;
import com.desirestodesigns.groceries.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "REGISTRATION ACTIVITY";
    EditText mFirstName, mLastName, mMobileNumber, mAddressLane1,mAddressLane2,mPinCode;
    Button mSave;
    String firstName, lastName, mobileNumber, addressLane1,addressLane2,pinCode,
            createdDate,userId,documentId;
    CustomerRegistrationForm registrationForm;
    //    ArrayList<Address> addressArrayList = new ArrayList<>();
    Address address;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    private FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);
        Log.i(TAG,"Registration");
        initializeUi();
    }

    private void initializeUi() {
        mFirstName = findViewById(R.id.first_name);
        mLastName = findViewById(R.id.last_name);
        mMobileNumber = findViewById(R.id.mobile_number);
        mAddressLane1 = findViewById(R.id.address_lane1);
        mAddressLane2 = findViewById(R.id.address_lane2);
        mPinCode = findViewById(R.id.pincode);
        mSave = findViewById(R.id.save_btn_2);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Customers");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void onClickSave(View view) {
        firstName = mFirstName.getText().toString();
        lastName = mLastName.getText().toString();
        mobileNumber = mMobileNumber.getText().toString();
        addressLane1 = mAddressLane1.getText().toString();
        addressLane2 = mAddressLane2.getText().toString();
        pinCode = mPinCode.getText().toString();

        if ((!TextUtils.isEmpty(firstName)) && (!TextUtils.isEmpty(lastName))
                && (!TextUtils.isEmpty(mobileNumber)) && (!TextUtils.isEmpty(addressLane1))
                && (!TextUtils.isEmpty(pinCode))) {
            Log.i(TAG, "Entered in to IF loop");
            registrationObject();
        } else {
            Log.i(TAG, "Entered into ELSE loop");
            Toast.makeText(this, "Please enter appropriate data",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void registrationObject() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "dd/MM/yyyy  hh:mm:ss");
        createdDate = simpleDateFormat.format(new Date());
        Log.i(TAG, "Current Timestamp: " + createdDate);
        DocumentReference ref = firebaseFirestore.collection("Customers")
                .document()
                .collection("Addresses").document();
        documentId = ref.getId();
        userId = mCurrentUser.getUid();
        Log.i(TAG,userId);
        registrationForm = new CustomerRegistrationForm();
        registrationForm.setFirstName(firstName);
        registrationForm.setLastName(lastName);
        registrationForm.setMobileNumber(mobileNumber);
        registrationForm.setCreatedDate(createdDate);
        registrationForm.setUserId(userId);
//        registrationForm.setAddress(address);
        address = new Address();
        address.setName(firstName+" "+lastName);
        address.setPhoneNumber(mobileNumber);
        address.setAddress(addressLane1+","+
                addressLane2+","+pinCode);
        address.setAddressLane1(addressLane1);
        address.setAddressLane2(addressLane2);
        address.setPinCode(pinCode);
        address.setStatus("Active");
        address.setCreatedDate(createdDate);
        address.setDocumentId(documentId);
        sendToDb(userId,documentId);
    }
    private void sendToDb(final String userId, String documentId){
        Log.i(TAG, "sendToDb method is invoked");
        collectionReference.document(userId)
                .set(registrationForm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerRegistrationActivity.this,
                        "Issue while adding data", Toast.LENGTH_SHORT).show();
            }
        });
        collectionReference.document(userId).collection("Addresses")
                .document(documentId)
                .set(address).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                clearUi();
                Log.i(TAG, "clearUi method is executed");
                finish();
                Toast.makeText(CustomerRegistrationActivity.this,
                        "Data successfully added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerRegistrationActivity.this,
                        "Issue while adding data", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void clearUi() {
        mFirstName.setText("");
        mLastName.setText("");
        mMobileNumber.setText("");
        mAddressLane1.setText("");
        mAddressLane2.setText("");
        mPinCode.setText("");
        Intent homeIntent = new Intent(CustomerRegistrationActivity.this,
                MainActivity.class);
        startActivity(homeIntent);
    }
}
