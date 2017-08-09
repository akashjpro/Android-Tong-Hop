package com.adida.aka.androidgeneral.fragment;


import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adida.aka.androidgeneral.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragment extends Fragment implements View.OnClickListener {

    EditText edtUsername, edtPass, edtEmail;
    Button btnDangKy, btnHuyDangKy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        view.findViewById(R.id.btn_waiting).setOnClickListener(this);
        view.findViewById(R.id.btn_confirm).setOnClickListener(this);
        view.findViewById(R.id.btn_info).setOnClickListener(this);
        view.findViewById(R.id.btn_input).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_waiting:
                waitingDialog();
                break;
            case R.id.btn_info:
                infoDialog();
                break;
            case R.id.btn_confirm:
                confirmDialog();
                break;
            case R.id.btn_input:
                inputDialog();
                break;
        }
    }

    /**
     * Example input dialog
     */
    private void inputDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setCanceledOnTouchOutside(false);

        edtUsername =  dialog.findViewById(R.id.editTextUsername);
        edtEmail    =  dialog.findViewById(R.id.editTextEmail);
        edtPass     =  dialog.findViewById(R.id.editTextPass);

        btnDangKy      = dialog.findViewById(R.id.buttonDangKy);
        btnHuyDangKy   =  dialog.findViewById(R.id.buttonHuy);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),
                        "Thông tin đăng ký:" +
                                "\nHọ Ten: " + edtUsername.getText().toString()+
                                "\nEmail:"   + edtEmail.getText().toString()   +
                                "\nMat Khau:" + edtPass.getText().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnHuyDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Example confirm dialog
     */
    private void confirmDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle("Are you sure delete!");
        dialog.setIcon(android.R.drawable.ic_delete);

        dialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "Yes", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Example info dialog
     */
    private void infoDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCanceledOnTouchOutside(false);

        Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Example waiting dialog
     */
    private void waitingDialog() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
    }
}
