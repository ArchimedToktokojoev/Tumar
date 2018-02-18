package com.example.kadyr.tumar;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.kadyr.tumar.DataRepository.Client;

/**
 * Created by Kadyr on 2/18/2018.
 */


public class DeleteEditClient extends DialogFragment implements View.OnClickListener{

    String nameClient;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delete_edit_client, null);

        Button btnDelete = v.findViewById(R.id.deleteClient);
        Button btnEdit = v.findViewById(R.id.editClient);
        nameClient = getArguments().getString("nameClient");

        ImageView pictureClient = v.findViewById(R.id.pictureSelectedClient);
        Bitmap bmp = Client.GetClientbyName(nameClient).getPicture();
        if(bmp!=null)
            pictureClient.setImageBitmap(bmp);

        btnDelete.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        return v;
    }

    public void onClick(View v1) {
        if(v1.getId()==R.id.deleteClient){
            AreYouSureFragment dlg = new AreYouSureFragment();

            Bundle args = new Bundle();
            args.putString("nameClient", nameClient);
            dlg.setArguments(args);
            dlg.show(getFragmentManager(), "deleteClient");
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
        if(v1.getId()==R.id.editClient){
            AddClientFragment dlg = new AddClientFragment();

            Bundle args = new Bundle();
            args.putString("nameClient", nameClient);
            dlg.setArguments(args);
            dlg.show(getFragmentManager(), "editClient");
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }



        public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }



    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
