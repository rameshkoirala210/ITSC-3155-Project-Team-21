package com.example.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EditContactFragment extends Fragment {

    Contact contact;
    FirebaseFirestore db;
    EditText editTextEmail, editTextPassword;
    TextView textViewWebsite;

    public EditContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_contact, container, false);
        editTextEmail = view.findViewById(R.id.editTexteditEmail);
        editTextPassword = view.findViewById(R.id.editTexteditPassword);
        db = FirebaseFirestore.getInstance();
        textViewWebsite = view.findViewById(R.id.textVieweditWebsite);

        view.findViewById(R.id.buttoneditCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        //TODO display whatever is in the contact
        editTextPassword.setText(contact.getPassword());
        editTextEmail.setText(contact.getEmail());
        textViewWebsite.setText(contact.getWebsite());

        view.findViewById(R.id.buttoneditSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if(editTextEmail.getText().toString().isEmpty()) {
                    builder.setTitle("Missing Fields").
                            setMessage("Title is Empty")
                            .setPositiveButton("OK", null)
                            .show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches()){
                    builder.setTitle("Invalid Email").
                            setMessage("Email is Incorrect")
                            .setPositiveButton("OK", null)
                            .show();
                } else if(editTextPassword.getText().toString().isEmpty()) {
                    builder.setTitle("Missing Fields").
                            setMessage("Password is Empty")
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    updateData(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                }
            }
        });

        return view;
    }

//    EditContactFragment.EditContactListener mListener;
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        mListener = (EditContactFragment.EditContactListener) (context);
//    }
//
//    interface EditContactListener{
//        void updateFinished();
//    }

    private void updateData(String email, String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        db.collection("Contact").document(contact.getDocumentID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "onSuccess: Successfully Updated");
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}