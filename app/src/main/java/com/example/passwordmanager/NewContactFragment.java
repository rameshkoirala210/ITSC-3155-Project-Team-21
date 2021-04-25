package com.example.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class NewContactFragment extends Fragment {
    EditText editTextEmail, editTextPassword;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    public NewContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_contact, container, false);
        editTextEmail = view.findViewById(R.id.edittextNewEmail);
        editTextPassword = view.findViewById(R.id.edittextPassword);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        view.findViewById(R.id.buttonSubmitContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO check for validation
                //TODO create firebase thing
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
                }
                else if(editTextPassword.getText().toString().isEmpty()) {
                    builder.setTitle("Missing Fields").
                            setMessage("Password is Empty")
                            .setPositiveButton("OK", null)
                            .show();
                }else{
                    setData(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                }
            }
        });

        view.findViewById(R.id.buttonCancleContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoContactFragmentAfterCancel();
            }
        });

        return view;
    }



    NewContactFragmentListener mListener;

    interface NewContactFragmentListener{
        void gotoContactFragmentAfterCancel();
        void doneCreateContact();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (NewContactFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CreateForumListener");
        }
    }

    private void setData(String email, String password){
        HashMap<String,Object> user = new HashMap<>();
        user.put("uid", mAuth.getCurrentUser().getUid());
        user.put("email", email);
        user.put("password", password);
        db.collection("Contact")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        user.put("documentID", documentReference.getId());
                        documentReference.update(user);
                        mListener.doneCreateContact();
                    }
                });


    }
}