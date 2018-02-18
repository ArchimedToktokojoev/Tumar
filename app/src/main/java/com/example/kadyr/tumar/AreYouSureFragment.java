package com.example.kadyr.tumar;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kadyr.tumar.DataRepository.Client;
import com.example.kadyr.tumar.DataRepository.Room;

/**
 * Created by Kadyr on 2/18/2018.
 */

public class AreYouSureFragment extends DialogFragment implements View.OnClickListener {
    Client client;
    private onClickYES mListener;

    public interface onClickYES{
        public void setClientsList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (onClickYES) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_are_you_sure, null);
        Button btnYes = v.findViewById(R.id.btnYes);
        Button btnNo = v.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        String nameClient = getArguments().getString("nameClient");
        client = Client.GetClientbyName(nameClient);

        return v;
    }

    public void onClick(View v1){
        if(v1.getId()==R.id.btnYes){
            client.Delete();
            Toast.makeText(getActivity(), "Клиент удален", Toast.LENGTH_LONG).show();
            mListener.setClientsList();
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
