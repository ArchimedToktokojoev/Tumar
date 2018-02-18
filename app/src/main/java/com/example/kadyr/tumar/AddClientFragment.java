package com.example.kadyr.tumar;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kadyr.tumar.DataRepository.Client;

/**
 * Created by Kadyr on 2/17/2018.
 */

public class AddClientFragment extends android.app.DialogFragment implements View.OnClickListener {

    private onClickOK mListener;

    public interface onClickOK{
        public void setClientsList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (AddClientFragment.onClickOK) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    View v;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_client, null);

        Button btnOk = v.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        Button btnCancel = v.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        return v;

    }

    public void onClick(View v1){
        if(v1.getId()==R.id.btnOk){
            String name = ((EditText)v.findViewById(R.id.nameClient)).getText().toString();
            String telephone = ((EditText)v.findViewById(R.id.telephoneClient)).getText().toString();
            ImageView imageView = v.findViewById(R.id.pictureClient);
            Bitmap picture=null;
            if(imageView.getDrawable()!= getResources().getDrawable(R.drawable.ic_camera_alt_black_24dp)){
                imageView.buildDrawingCache();
                picture = imageView.getDrawingCache();
            }
            Client newClient = new Client(0, name, picture, telephone);

            try{
                newClient.Insert();
                mListener.setClientsList();
                Toast.makeText(getActivity(),"Данные добавлены",Toast.LENGTH_LONG).show();

                getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            } catch(Exception ex){
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();

            }


        }

        if(v1.getId()==R.id.btnCancel){ getActivity().getFragmentManager().beginTransaction().remove(this).commit();}


    }

    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }


}
