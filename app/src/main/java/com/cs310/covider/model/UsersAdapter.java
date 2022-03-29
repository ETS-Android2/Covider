package com.cs310.covider.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs310.covider.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

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
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        TextView emailTextView = convertView.findViewById(R.id.item_user_email);
        TextView lastCheckDateView = convertView.findViewById(R.id.item_user_check_date);
        TextView lastSymptomsDateView = convertView.findViewById(R.id.item_user_symptoms_date);
        TextView lastInfectedDateView = convertView.findViewById(R.id.item_user_infection_date);
        if (user.getLastCheckDate() != null) {
            calendar.setTime(user.getLastCheckDate());
        }
        String lastCheckDate = user.getLastCheckDate() == null ? "User has no daily check history" : sdf.format(calendar.getTime()) + " PST";
        if (user.getLastSymptomsDate() != null) {
            calendar.setTime(user.getLastSymptomsDate());
        }
        String lastSymptomsDate = user.getLastSymptomsDate() == null ? "User has not shown any symptoms" : sdf.format(calendar.getTime()) + " PST";
        if (user.getLastInfectionDate() != null) {
            calendar.setTime(user.getLastInfectionDate());
        }
        String lastInfectedDate = user.getLastInfectionDate() == null ? "User has not reported infection" : sdf.format(calendar.getTime()) + " PST";
        emailTextView.setText(user.getEmail());
        lastCheckDateView.setText(lastCheckDate);
        lastSymptomsDateView.setText(lastSymptomsDate);
        lastInfectedDateView.setText(lastInfectedDate);
        return convertView;
    }
}
