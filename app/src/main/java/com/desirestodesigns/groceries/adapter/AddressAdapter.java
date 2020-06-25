package com.desirestodesigns.groceries.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.desirestodesigns.groceries.datamodel.AddressCard;
import com.desirestodesigns.groceries.interfaces.OnRecyclerItemClickListener;
import com.desirestodesigns.groceries.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private static final String TAG = "ADDRESS ADAPTER";
    private ArrayList<AddressCard> addressCardArrayList;


    private OnRecyclerItemClickListener mListener;

    public AddressAdapter(ArrayList<AddressCard> addressCardArrayList) {
        this.addressCardArrayList = addressCardArrayList;
    }



    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.addresscard, parent, false);
        AddressViewHolder addressViewHolder = new AddressViewHolder(view, mListener);
        return addressViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        final AddressCard addressCard = addressCardArrayList.get(position);
        holder.name.setText(addressCard.getName());
        holder.phoneNumber.setText(addressCard.getPhoneNumber());
        holder.address.setText(addressCard.getAddress());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit(addressCard.getDocumentId(), mListener);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = getItemCount();
                if(count>1){
                delete(addressCard.getDocumentId(), mListener);}
                else{
                    Toast.makeText(view.getContext(), "Can't delete this card ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.i(TAG, "onBindViewHolder method Completed");
    }

    private void delete(String documentId, OnRecyclerItemClickListener mListener) {
        if (mListener != null) {
            if (documentId != null) {
                mListener.onDeleteClick(documentId);
                Log.i(TAG, "test  " + documentId);
                Log.i(TAG, "onDeleteClick METHOD IS CLICKED AND CONTROL SEND TO MAIN ACTIVITY");
            }
        }
    }

    public void edit(String documentId, OnRecyclerItemClickListener mListener) {
        if (mListener != null) {
            if (documentId != null) {
                Log.i(TAG, "test  " + documentId);
                mListener.onEditClick(documentId);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (addressCardArrayList == null) {
            return 0;
        } else {
            return addressCardArrayList.size();
        }
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name, phoneNumber, address;
        public ImageButton edit, delete, status;



//        }
        public AddressViewHolder(@NonNull View itemView, OnRecyclerItemClickListener mListener
        ) {

            super(itemView);
            name = itemView.findViewById(R.id.rv_name);
            phoneNumber = itemView.findViewById(R.id.rv_phone_number);
            address = itemView.findViewById(R.id.rv_address);
            delete = itemView.findViewById(R.id.imgb1);
            edit = itemView.findViewById(R.id.imgb2);


        }






    }

}
