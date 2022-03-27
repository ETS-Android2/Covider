package com.cs310.covider.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.cs310.covider.R;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<User> {
    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        TextView emailTextView = convertView.findViewById(R.id.item_user_email);
        TextView lastCheckDateView = convertView.findViewById(R.id.item_user_check_date);
        TextView lastInfectionDateView = convertView.findViewById(R.id.item_user_infection_date);
        String lastCheckDate = user.getLastCheckDate() == null ? "User has no daily check history" : String.format("%tc", user.getLastCheckDate());
        String lastInfectionDate = user.getLastInfectionDate() == null ? "User has not shown any symptoms" : String.format("%tc", user.getLastInfectionDate());
        emailTextView.setText(user.getEmail());
        lastCheckDateView.setText(lastCheckDate);
        lastInfectionDateView.setText(lastInfectionDate);
        return convertView;
    }
}
