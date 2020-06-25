package com.desirestodesigns.groceries.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.desirestodesigns.groceries.datamodel.Address;
import com.desirestodesigns.groceries.adapter.AddressAdapter;
import com.desirestodesigns.groceries.datamodel.AddressCard;
import com.desirestodesigns.groceries.interfaces.OnRecyclerItemClickListener;
import com.desirestodesigns.groceries.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditAddressActivity extends AppCompatActivity {

    private static final String TAG = "Edit Address Activity";
    EditText mName, mMobileNumber, mAddressLane1, mAddressLane2, mPinCode;
    Button mSave;
    String name, mobileNumber, addressLane1, addressLane2, pinCode, createdDate, userId, documentId;
    Address address;
    String deleteId;
    FloatingActionButton mAddAddress;
    RecyclerView recyclerView;
    AddressAdapter addressAdapter;
    ArrayList<AddressCard> addressCardArrayList = new ArrayList<>();
    private FirebaseUser mCurrentUser;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        initializeUi();
        onClickModify();
        documentId = getIntent().getStringExtra("DOCUMENT_ID");
        Log.i(TAG, "return value test" + documentId);
        if (!TextUtils.isEmpty(documentId)) {
            Toast.makeText(this, documentId, Toast.LENGTH_SHORT).show();
//            editItem(userId);
        } else {
            DocumentReference ref = firebaseFirestore.collection("Customers").
                    document().collection("Addresses").document();
            documentId = ref.getId();
        }
        recyclerMethod(addressCardArrayList);
        readFromDb();
    }

    private void onClickModify() {
        documentId = getIntent().getStringExtra("DOCUMENT_ID");
        collectionReference.document(userId).collection("Addresses").
                document(documentId).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot != null) {
                            address = new Address();
                            address = documentSnapshot.toObject(Address.class);
                            try {
                                mName.setText(address.getName());
                                mMobileNumber.setText(address.getPhoneNumber());
                                mAddressLane1.setText(address.getAddressLane1());
                                mAddressLane2.setText(address.getAddressLane2());
                                mPinCode.setText(address.getPinCode());
                            }catch (NullPointerException e1){
                                Log.i(TAG, String.valueOf(e1));
                            }


                        }

                    }
                });
    }

    private void readFromDb() {

        Log.i(TAG, "readFromDb Method Invoked");
        firebaseFirestore.collection("Customers").
                document(userId).collection("Addresses")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            addressCardArrayList.clear();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                AddressCard addressCard = doc.toObject(AddressCard.class);
                                String customerName = addressCard.getName();
                                String phoneNumber = addressCard.getPhoneNumber();
                                String address = addressCard.getAddress();
                                addressCard.setName(customerName);
                                addressCard.setPhoneNumber(phoneNumber);
                                addressCard.setAddress(address);
                                addressCardArrayList.add(addressCard);
                                //countArrayList.add(employee);
                            }
                            //notify Adapter for Updated Data
                            addressAdapter.notifyDataSetChanged();

                        } else {
                            addressCardArrayList.clear();
                            addressAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }


    private void recyclerMethod(ArrayList<AddressCard> addressCardArrayList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        addressAdapter = new AddressAdapter(addressCardArrayList);
        recyclerView.setAdapter(addressAdapter);
        //Delete using image button in recycler view
        addressAdapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onDeleteClick(String removeDocId) {
                deleteId = removeDocId;
                removeItem(removeDocId);
            }

            @Override
            public void onEditClick(String editDocId) {
                Intent intent = new Intent(getBaseContext(), EditAddressActivity.class);
                intent.putExtra("DOCUMENT_ID", editDocId);
                startActivity(intent);
            }



        });
        Log.i(TAG, "recyclerMethod is Completed ");
    }

    private void removeItem(final String removeDocId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditAddressActivity.this);

        builder.setTitle("Attention!");
        builder.setMessage("Are you sure to delete this Customer details?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Delete Category from the database
                Log.i("TAG", removeDocId);
                collectionReference.document(userId).collection("Addresses").
                        document(removeDocId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "DocumentSnapshot successfully deleted!");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "Error deleting document", e);
                            }
                        });
                Toast.makeText(EditAddressActivity.this, "Card Deleted", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog deleteAlertDialog = builder.create();
        deleteAlertDialog.show();
    }

    private void initializeUi() {
        mName = findViewById(R.id.name1);
        mMobileNumber = findViewById(R.id.number1);
        mAddressLane1 = findViewById(R.id.address_lane_1);
        mAddressLane2 = findViewById(R.id.address_lane_2);
        mPinCode = findViewById(R.id.pin_code);
        mSave = findViewById(R.id.save_btn_3);
        mAddAddress = findViewById(R.id.fab);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Customers");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mCurrentUser != null;
        userId = mCurrentUser.getUid();
        recyclerView = findViewById(R.id.recycler_view);
//        addressAdapter = new AddressAdapter(addressCardArrayList,this);
//        recyclerView.setAdapter(addressAdapter);
    }

    public void onClickSave1(View view) {
        name = mName.getText().toString();
        mobileNumber = mMobileNumber.getText().toString();
        addressLane1 = mAddressLane1.getText().toString();
        addressLane2 = mAddressLane2.getText().toString();
        pinCode = mPinCode.getText().toString();
        if ((!TextUtils.isEmpty(name)) && (!TextUtils.isEmpty(mobileNumber))
                && (!TextUtils.isEmpty(addressLane1)) && (!TextUtils.isEmpty(pinCode))) {
            Log.i(TAG, "Entered in to IF loop");
            addressObject();
        } else {
            Log.i(TAG, "Entered into ELSE loop");
            Toast.makeText(this, "Please enter appropriate data", Toast.LENGTH_LONG).show();
        }
    }

    private void addressObject() {

//        DocumentReference ref = firebaseFirestore.collection("Customers").document();
//        documentId = ref.getId();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss");
        createdDate = simpleDateFormat.format(new Date());
        Log.i(TAG, "Current Timestamp: " + createdDate);
        address = new Address();
        address.setName(name);
        address.setPhoneNumber(mobileNumber);
        address.setAddress(addressLane1+","+
                addressLane2+","+pinCode);
        address.setAddressLane1(addressLane1);
        address.setAddressLane2(addressLane2);
        address.setPinCode(pinCode);
        address.setCreatedDate(createdDate);
        address.setDocumentId(documentId);
        address.setStatus("Inactive");
        sendToDB(userId, documentId);
    }

    private void sendToDB(String userId, String documentId) {
        collectionReference.document(userId).collection("Addresses")
                .document(documentId)
                .set(address).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                clearUi();
                Log.i(TAG, "clearUi method is executed");
                finish();
                Toast.makeText(EditAddressActivity.this, "Data successfully added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditAddressActivity.this, "Issue while adding data", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void clearUi() {
        mName.setText("");
        mMobileNumber.setText("");
        mAddressLane1.setText("");
        mAddressLane2.setText("");
        mPinCode.setText("");
    }

    public void onClickSwitchActivity(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditAddressActivity.this);

        builder.setTitle("Attention!");
        builder.setMessage("Are you sure to add new Address?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                DocumentReference ref = firebaseFirestore.collection("Customers").
                        document().collection("Addresses").document();
                documentId = ref.getId();
                mName.setText("");
                mMobileNumber.setText("");
                mAddressLane1.setText("");
                mAddressLane2.setText("");
                mPinCode.setText("");
//                Toast.makeText(EditAddressActivity.this, "Card Deleted", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog deleteAlertDialog = builder.create();
        deleteAlertDialog.show();
    }



}
