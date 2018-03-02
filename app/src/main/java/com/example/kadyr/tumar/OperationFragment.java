package com.example.kadyr.tumar;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kadyr.tumar.DataRepository.Client;
import com.example.kadyr.tumar.DataRepository.Room;

import java.nio.channels.SelectableChannel;
import java.time.Period;

public class OperationFragment extends DialogFragment implements OnClickListener {


    Room room ;
    Client selectedClient;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_operation, null);

        String idRoom = getArguments().getString("idRoom");
        room = Room.GetRoom(Integer.valueOf(idRoom));
        boolean isFree = room.getStatus()==Constants.RoomStatusFree ;

        TextView vd = v.findViewById(R.id.title);
        vd.setText("Комната - "+room.getName());
        Button btn = v.findViewById(R.id.doIn);
        btn.setOnClickListener(this);
        btn.setEnabled(isFree);

        Button btnEditIn = v.findViewById(R.id.editIn);
        btnEditIn.setOnClickListener(this);
        btnEditIn.setEnabled(!isFree);

        TextView clientName = v.findViewById(R.id.clientName);
        TextView clientTelephone = v.findViewById(R.id.clientTelephone);
        ImageView imageView = v.findViewById(R.id.pictureClintInOperation);
        Client client = room.GetClient();
        boolean isClientExist = client!=null;
        int visibility = isClientExist?View.VISIBLE:View.INVISIBLE;
        imageView.setVisibility(visibility);
        clientName.setVisibility(visibility);
        clientTelephone.setVisibility(visibility);

        if(isClientExist){
            imageView.setImageBitmap(client.getPicture());
            clientName.setText(client==null?"":client.getName());
            clientTelephone.setText(client.getTelephone());
        }




        btn =  v.findViewById(R.id.doOut);
        btn.setOnClickListener(this);
        btn.setEnabled(!isFree);
        v.findViewById(R.id.doBooking).setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        if(v.getId()==R.id.doIn) {
            CheckInFragment dlg = new CheckInFragment();

            Bundle args = new Bundle();
            args.putInt("idRoom", room.getId());
            args.putInt(Constants.RegimKey, Constants.RegimAdd);
            dlg.setArguments(args);
            dlg.show(getFragmentManager(), "doIn");
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
        if(v.getId()==R.id.editIn) {
            CheckInFragment dlg = new CheckInFragment();

            Bundle args = new Bundle();
            args.putInt("idRoom", room.getId());
            args.putInt(Constants.RegimKey, Constants.RegimEdit);
            dlg.setArguments(args);
            dlg.show(getFragmentManager(), "doIn");
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
        if(v.getId()==R.id.doOut) {
            CheckOutFragment dlg = new CheckOutFragment();

            Bundle args = new Bundle();
            args.putString("idRoom", String.valueOf(room.getId()));

            dlg.setArguments(args);
            dlg.show(getFragmentManager(), "doIn");
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