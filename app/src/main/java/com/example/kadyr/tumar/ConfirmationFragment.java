package com.example.kadyr.tumar;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kadyr.tumar.DataRepository.Client;


public class ConfirmationFragment extends DialogFragment implements View.OnClickListener {
    private ConfirmationFragment.onClickAction mListener;

    public interface onClickAction{
         void DoConfirmationAction();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ConfirmationFragment.onClickAction) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс onClickAction");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_confirmation, null);
        Button btnYes = v.findViewById(R.id.btnYes);
        Button btnNo = v.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        return v;
    }

    public void onClick(View v1){
        if(v1.getId()==R.id.btnYes){
            mListener.DoConfirmationAction();
        }

        getActivity().getFragmentManager().beginTransaction().remove(this).commit();

    }


    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }



    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
