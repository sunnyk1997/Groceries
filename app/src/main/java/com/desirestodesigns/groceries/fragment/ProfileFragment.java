package com.desirestodesigns.groceries.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.desirestodesigns.groceries.datamodel.Address;
import com.desirestodesigns.groceries.datamodel.CustomerRegistrationForm;
import com.desirestodesigns.groceries.R;
import com.desirestodesigns.groceries.activity.EditAddressActivity;
import com.desirestodesigns.groceries.activity.EditCustomerDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private static final String TAG = "PROFILE FRAGMENT";
    TextView mName,mNumber,mAddress;
    Button mEdit,mAddOrModify;
    String documentId,userId;
    ArrayList<CustomerRegistrationForm> customerArrayList = new ArrayList<>();
    ArrayList<Address> addressArrayList = new ArrayList<>();
    View view;
    private FirebaseUser mCurrentUser;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile,container,false);
        initializeUi();
        readFromDb();
        return view;
    }

    private void initializeUi() {
        mName = view.findViewById(R.id.customer_name);
        mNumber = view.findViewById(R.id.customer_number);
        mAddress = view.findViewById(R.id.customer_address);
        mEdit = view.findViewById(R.id.edit);
        mAddOrModify = view.findViewById(R.id.add_or_modify);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditCustomerDetailsActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });
        mAddOrModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditAddressActivity.class);
                intent.putExtra("DOCUMENT_ID", documentId);
                startActivity(intent);
            }
        });
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = mCurrentUser.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Customers");
    }
    private void readFromDb() {
        Log.i(TAG, "readFromDb Method Invoked");
       collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            customerArrayList.clear();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                CustomerRegistrationForm customer = doc.toObject(CustomerRegistrationForm.class);
                                String customerName = customer.getFirstName() + " " +customer.getLastName();
                                String customerNumber = customer.getMobileNumber();
//                                String address = customer.getAddress();
                                 userId = customer.getUserId();
                                 mName.setText(customerName);
                                 mNumber.setText(customerNumber);
//                                mAddress.setText(address);



                            }
                        }

                    }
                });
       collectionReference.document(userId).collection("Addresses").
               addSnapshotListener(new EventListener<QuerySnapshot>() {
                   @Override
                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                       if (!queryDocumentSnapshots.isEmpty()) {
                           addressArrayList.clear();

                           for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                               Address address = doc.toObject(Address.class);
                               String status = address.getStatus();
                               if(status.equals("Active")){
                                   String customerAddress = address.getAddress();
                                   mAddress.setText(customerAddress);
                                   documentId = address.getDocumentId();
                               }

                           }
                       }
                   }
               });
    }

}
