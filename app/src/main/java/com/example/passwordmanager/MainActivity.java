package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, CreateAccountFragment.NewAccountListener, ContactFragment.ContactListener, NewContactFragment.NewContactFragmentListener {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.layout, new LoginFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.layout, new ContactFragment()).commit();
        }
    }


    @Override
    public void gotoContactFragmentfromRegister() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout, new ContactFragment()).commit();
    }

    @Override
    public void gotoLoginFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout, new LoginFragment()).commit();
    }

    @Override
    public void gotoContactFragmentfromLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout, new ContactFragment()).commit();
    }

    @Override
    public void gotoNewAccountFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout, new CreateAccountFragment()).addToBackStack(null).commit();
    }

    @Override
    public void gotoNewContactFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout, new NewContactFragment()).addToBackStack(null).commit();
    }

    @Override
    public void gotoLoginFragmentafterLogout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout, new LoginFragment()).commit();
    }

    @Override
    public void gotoContactFragmentAfterCancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void doneCreateContact() {
        getSupportFragmentManager().popBackStack();
    }
}