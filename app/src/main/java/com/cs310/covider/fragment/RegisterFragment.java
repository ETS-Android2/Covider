package com.cs310.covider.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cs310.covider.MainActivity;
import com.cs310.covider.R;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends MyFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = rootView.findViewById(R.id.register_usertype);
        String[] items = new String[]{"I'm a instructor", "I'm a student"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        Button button = rootView.findViewById(R.id.register_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailView = rootView.findViewById(R.id.register_email);
                EditText emailConfirmView = rootView.findViewById(R.id.register_email_confirm);
                EditText passView = rootView.findViewById(R.id.register_password);
                EditText passConfirmView = rootView.findViewById(R.id.register_password_confirm);
                boolean ok = true;
                String email = String.valueOf(emailView.getText());
                String emailConfirm = String.valueOf(emailConfirmView.getText());
                String pass = String.valueOf(passView.getText());
                String passConfirm = String.valueOf(passConfirmView.getText());
                String error;
                if ((error = Util.emailError(email)) != null) {
                    ok = false;
                    emailView.setError(error);
                }
                if ((error = Util.passwordError(pass)) != null) {
                    ok = false;
                    passView.setError(error);
                }
                if (!pass.equals(passConfirm)) {
                    ok = false;
                    passConfirmView.setError("Passwords don't match!");
                }
                if (!email.equals(emailConfirm)) {
                    ok = false;
                    emailConfirmView.setError("Emails don't match!");
                }
                if (ok) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        CollectionReference dbUsers = FirebaseFirestore.getInstance().collection("Users");
                                        User.UserType userType = User.UserType.INSTRUCTOR;
                                        if (spinner.getSelectedItem().toString().equals(items[1])) {
                                            userType = User.UserType.STUDENT;
                                        }
                                        User user = new User(userType, new ArrayList<>(), email, null, null, null, new HashMap<>(), null);
                                        dbUsers.document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    MainActivity mainActivity = (MainActivity) getActivity();
                                                    assert mainActivity != null;
                                                    mainActivity.changeToAuthedMenu();
                                                } else {
                                                    FirebaseAuth.getInstance().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            openDialog(task);
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } else {
                                        openDialog(task);
                                    }
                                }
                            }
                    );
                }
            }
        });
    }
}