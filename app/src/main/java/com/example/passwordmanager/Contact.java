package com.example.passwordmanager;

public class Contact {
    String documentID;
    String email;
    String password;
    String uid;

    public Contact() {
    }

    public Contact(String documentID, String email, String password, String uid) {
        this.documentID = documentID;
        this.email = email;
        this.password = password;
        this.uid = uid;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "documentID='" + documentID + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
