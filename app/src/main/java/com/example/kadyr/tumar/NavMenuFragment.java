package com.example.kadyr.tumar;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kadyr.tumar.DataRepository.Client;
import com.example.kadyr.tumar.DataRepository.Room;

/**
 * Created by Kadyr on 2/17/2018.
 */

public class NavMenuFragment extends DialogFragment implements View.OnClickListener{

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navmenu, null);

        Button btnClient = v.findViewById(R.id.client);
        btnClient.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), Clients.class);
        startActivity(intent);
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }


    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }



    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
