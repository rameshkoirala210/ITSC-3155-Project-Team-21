package com.example.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CreateAccountFragment extends Fragment {
    EditText registerName, registerEmail,registerpassword;
    private FirebaseAuth mAuth;
    private final String TAG = "NewAccount" ;

    public CreateAccountFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Register");
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        registerName = view.findViewById(R.id.RegisterName);
        registerEmail = view.findViewById(R.id.RegisterEmail);
        registerpassword = view.findViewById(R.id.registerpassword);
        mAuth = FirebaseAuth.getInstance();

        view.findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerpassword.getText().toString();


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if(name.isEmpty()) {
                    builder.setTitle("Missing Fields").
                            setMessage("Name is Empty")
                            .setPositiveButton("OK", null)
                            .show();
                }else if(email.isEmpty()){
                    builder.setTitle("Missing Fields").
                            setMessage("Email is Empty")
                            .setPositiveButton("OK", null)
                            .show();
                }else if(password.isEmpty()){
                    builder.setTitle("Missing Fields").
                            setMessage("Password is Empty")
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "onComplete: Successful");

                                        Log.d(TAG, "onComplete: " + mAuth.getCurrentUser().getUid());
                                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                        mAuth.getCurrentUser().updateProfile(profile);
                                        mListener.gotoContactFragmentfromRegister();
                                    }else{
                                        builder.setTitle("Not Successful")
                                                .setMessage(task.getException().getMessage())
                                                .setPositiveButton("OK", null)
                                                .show();
                                    }

                                }
                            });
                }

            }
        });
        view.findViewById(R.id.buttonCancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoLoginFragment();
            }
        });

        return view;
    }
    CreateAccountFragment.NewAccountListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (NewAccountListener) (context);
    }

    interface NewAccountListener{
        void gotoContactFragmentfromRegister();
        void gotoLoginFragment();
    }
}