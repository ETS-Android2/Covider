package com.cs310.covider.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.cs310.covider.MainActivity;
import com.cs310.covider.R;
import com.cs310.covider.fragment.CourseDetailFragment;

import java.util.List;

public class CoursesAdapter extends ArrayAdapter<Course> {

    public CoursesAdapter(@NonNull Context context, int resource, @NonNull List<Course> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_course, parent, false);
        }
        TextView nameAndSection = convertView.findViewById(R.id.item_course_name_section);
        TextView courseMode = convertView.findViewById(R.id.item_course_course_mode);
        TextView building = convertView.findViewById(R.id.item_course_building_name);
        String[] fileds = course.getId().split("=");
        String nameSection = fileds[0] + " section " + fileds[1];
        nameAndSection.setText(nameSection);
        String courseModeText = "Course mode: " + course.getCourseMode().toString();
        courseMode.setText(courseModeText);
        String locationText = "Location: " + ((MainActivity) getContext()).buildingAbrevToFullName(course.getBuildingName());
        building.setText(locationText);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = ((MainActivity) (getContext())).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment, CourseDetailFragment.newInstance(course.getId()));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return convertView;
    }
}
