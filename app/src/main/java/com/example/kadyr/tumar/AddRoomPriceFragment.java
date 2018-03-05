package com.example.kadyr.tumar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kadyr.tumar.DataRepository.Client;
import com.example.kadyr.tumar.DataRepository.RoomPrice;
import com.example.kadyr.tumar.DataRepository.RoomType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kadyr on 2/17/2018.
 */

public class AddRoomPriceFragment extends android.app.DialogFragment implements View.OnClickListener
        {
            private OnDataRefreshListener mListener;


    View v;
    boolean Regim;
    final boolean Add = false;
    final boolean Edit = true;
    Date dt = new Date();
    List<String> roomTypeNames = new ArrayList<>() ;
    RoomTypeAdapter typeAdapter ;
    EditText roomTypeName ;
    ListView listViewRoomTypes;
    TextView dtOperation ;
    EditText price ;
    RoomPrice roomPrice ;

            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_room_price, null);

        v.findViewById(R.id.btnOk).setOnClickListener(this);
        v.findViewById(R.id.btnCancel).setOnClickListener(this);
        roomTypeName = v.findViewById(R.id.roomTypeName);
        dtOperation = v.findViewById(R.id.dateChange);
        price=v.findViewById(R.id.roomPrice) ;

        Bundle arg = getArguments();


        if(arg!=null) {
            Regim=Edit;
            int id  = arg.getInt("IdEntity");
            roomPrice = RoomPrice.GetRoomPrice(Integer.valueOf(id)) ;
            dtOperation.setText(CommonFunctions.DateToString(roomPrice.DateOperation));
            price.setText(String.valueOf(roomPrice.Price));
            RoomType roomType = RoomType.GetRoomType(roomPrice.IdRoomType);
            roomTypeName.setText(roomType.getName());

        } else{
            Regim=Add;
            dtOperation.setText(CommonFunctions.DateToString(dt));

        }
        dtOperation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog tpd = new DatePickerDialog(getActivity(), myCallBack,
                        dt.getYear() + 1900, dt.getMonth(), dt.getDate());
                tpd.show();
            }
        });

        listViewRoomTypes = v.findViewById(R.id.roomTypeList);
        listViewRoomTypes.setVisibility(View.INVISIBLE);
        price = v.findViewById(R.id.roomPrice);

        return v;

    }



    @Override
    public void onResume() {
        super.onResume();
        try {

            List<RoomType> rt = RoomType.GetRoomTypes();
            for (RoomType type:rt )
                roomTypeNames.add(type.getName());

            typeAdapter = new RoomTypeAdapter(getActivity(),  roomTypeNames);
            roomTypeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    listViewRoomTypes.setVisibility(hasFocus ? View.VISIBLE :
                            View.INVISIBLE);
                }
            });
            if(!roomTypeName.getText().toString().isEmpty())
                typeAdapter.getFilter().filter(roomTypeName.getText().toString());

            // установка слушателя изменения текста
            roomTypeName.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) { }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                // при изменении текста выполняем фильтрацию
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    roomTypeNames=RoomType.GetFilteredArray(s.toString());
                    typeAdapter.SetItems(roomTypeNames);
                    typeAdapter.getFilter().filter(s.toString());

                }
            });
            listViewRoomTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the selected item text from ListView
                    String selectedItem = (String) parent.getItemAtPosition(position);

                    // Display the selected item text on TextView
                    roomTypeName.setText(selectedItem);
                }
            });


            listViewRoomTypes.setAdapter(typeAdapter);
        }
        catch (SQLException ex){}
    }


    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dtOperation.setText(String.valueOf(dayOfMonth) + "/" + monthOfYear + "/" + year);
            }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void onClick(View v1){

        if(v1.getId()==R.id.btnOk){
            if(Regim==Add) {
                try {
                    int idNew = (int)RoomPrice.Insert(dtOperation.getText().toString(),
                            price.getText().toString(),
                            roomTypeName.getText().toString());
                    Log.d("axa", String.valueOf(idNew));
                }
                catch (Exception ex){
                    Log.d("axa", ex.getMessage());

                    Toast.makeText(getActivity(),
                            ex.getMessage(), Toast.LENGTH_SHORT); ;
                }
            }

            if(Regim==Edit){
                try {
                   // roomPrice.
                    roomPrice.UpdateWithParams(dtOperation.getText().toString(),
                            price.getText().toString(),
                            roomTypeName.getText().toString());
                    }
                catch (Exception ex){
                    Log.d("axa", ex.getMessage());
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT);
                }

            }
            mListener.OnDataRefresh();
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }



        if(v1.getId()==R.id.btnCancel){ getActivity().getFragmentManager().beginTransaction().remove(this).commit();}


    }




    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
            public interface OnDataRefreshListener {
                void OnDataRefresh();
            }


            @Override
            public void onAttach(Context context) {
                super.onAttach(context);
                if (context instanceof OnDataRefreshListener) {
                    mListener = (OnDataRefreshListener) context;
                } else {
                    throw new RuntimeException(context.toString()
                            + " must implement OnFragmentInteractionListener");
                }
            }


        }
