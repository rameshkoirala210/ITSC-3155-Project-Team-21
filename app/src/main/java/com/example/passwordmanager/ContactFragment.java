package com.example.passwordmanager;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;


public class ContactFragment extends Fragment {
    private FirebaseAuth mAuth;
    private final String TAG = "TAG_ContactFragment";
    ArrayList<Contact> contactsList = new ArrayList<>();

    FirebaseFirestore db;
    ContactAdapter adapter;
    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;
    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        getActivity().setTitle("Contact");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                mListener.gotoLoginFragmentafterLogout();
            }
        });

        view.findViewById(R.id.buttonNewForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoNewContactFragment();
            }
        });

        recyclerView = view.findViewById(R.id.container);
        adapter = new ContactAdapter();
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        getContacts();

        return view;
    }

    private void getContacts(){
        db.collection("Contact").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    contactsList.clear();
                    for(QueryDocumentSnapshot queryDocumentSnapshot: value) {
                        Contact contact = queryDocumentSnapshot.toObject(Contact.class);
                        contactsList.add(contact);
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }


    ContactFragment.ContactListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ContactFragment.ContactListener) (context);
    }

    interface ContactListener{
        void gotoNewContactFragment();
        void gotoLoginFragmentafterLogout();
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>{
        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_list, parent, false);
            return new ContactHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            holder.setUpContactItem(contactsList.get(position));
        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public class ContactHolder extends RecyclerView.ViewHolder {
            TextView textViewEmail, textViewPassword;
            ImageView imageViewTrash;

            public ContactHolder(@NonNull View itemView) {
                super(itemView);
                textViewEmail = itemView.findViewById(R.id.textViewEmail);
                textViewPassword = itemView.findViewById(R.id.textViewPassword);
                imageViewTrash = itemView.findViewById(R.id.imageViewtrash);
            }

            public void setUpContactItem(Contact contact) {
                textViewEmail.setText(contact.getEmail());
                textViewPassword.setText(contact.getPassword());
                imageViewTrash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO remove contact from firebase
                        //Log.d(TAG, "onClick: TRYING TO REMOVE" + contact.toString());
                        db.collection("Contact").document(contact.getDocumentID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: " + contact + " successfully deleted");
                            }
                        });
                    }
                });
            }
        }
    }
}