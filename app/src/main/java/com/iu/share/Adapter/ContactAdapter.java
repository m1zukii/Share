package com.iu.share.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iu.share.Activity.MyActivity;
import com.iu.share.Bean.Contact;
import com.iu.share.Bean.User;
import com.iu.share.R;
import com.ruffian.library.widget.RTextView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private ArrayList<Contact> contacts;
    private Context context;

    public ContactAdapter(ArrayList<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Contact contact = contacts.get(i);
        viewHolder.username.setText(contact.getUsername());
        viewHolder.phoneNumber.setText(contact.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        RTextView username;
        RTextView phoneNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = (RTextView) itemView.findViewById(R.id.contact_username);
            phoneNumber = (RTextView) itemView.findViewById(R.id.contact_pNumber);
        }
    }
}
