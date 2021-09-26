package com.example.swaralipi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogFileName extends AppCompatDialogFragment {

    private DialogListener listener;
    String fill;

    public DialogFileName(String fill) {
        this.fill = fill;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.file_name_dialog,null);
        EditText etfileName = view.findViewById(R.id.etgetFileName);
        etfileName.setText(fill);
        etfileName.selectAll();
        etfileName.requestFocus();

        builder.setView(view).setTitle("Enter File Name")
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = etfileName.getText().toString();
                        listener.applyTexts(fileName);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (DialogListener) context;
    }

    public interface DialogListener{
        void applyTexts(String fileName);
    }
}
