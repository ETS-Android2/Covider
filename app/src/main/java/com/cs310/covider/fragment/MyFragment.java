package com.cs310.covider.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cs310.covider.MainActivity;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MyFragment extends Fragment {
    protected View rootView = null;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }

    public void openDialog(@NotNull @NonNull Task task) {
        openDialog(Objects.requireNonNull(task.getException()).getMessage());
    }

    public void openDialog(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireActivity());
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void redirectToHome() {
        assert getActivity() != null;
        ((MainActivity) getActivity()).changeToAuthedMenu();
    }

    protected void showYesNoDialog(DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireActivity());
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setPositiveButton("Yes", yesListener);
        builder1.setNegativeButton("No", noListener);
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
