package com.example.kadyr.tumar;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kadyr.tumar.DataRepository.Client;
import com.example.kadyr.tumar.DataRepository.Room;

public class OperationFragment extends DialogFragment implements OnClickListener {


    Room room ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_operation, null);

        String idRoom = getArguments().getString("idRoom");
        room = Room.GetRoom(Integer.valueOf(idRoom));
        boolean isFree = room.getStatus()==Constants.RoomStatusFree ;;
        TextView vd = v.findViewById(R.id.title);
        vd.setText("Комната - "+room.getName());
        Button btn = v.findViewById(R.id.doIn);
        btn.setOnClickListener(this);
        btn.setEnabled(isFree);

        TextView clientName = v.findViewById(R.id.clientName);
        Client client = room.GetClient();
        clientName.setText(client==null?"":client.getName());

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
            args.putString("idRoom", String.valueOf(room.getId()));
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