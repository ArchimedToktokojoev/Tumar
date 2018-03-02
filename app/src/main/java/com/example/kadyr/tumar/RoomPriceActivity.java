package com.example.kadyr.tumar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.kadyr.tumar.DataRepository.Client;
import com.example.kadyr.tumar.DataRepository.RoomPrice;

import java.util.List;

public class RoomPriceActivity extends AppCompatActivity  implements EditDeleteFragment.OnEditDeleteFragmentListener ,
        AddRoomPriceFragment.OnDataRefreshListener,   ConfirmationFragment.onClickAction {

    RoomPriceAdapter priceAdapter;
    List<RoomPrice> roomPrices;
    ListView roomPricesList;
    RoomPrice roomPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_prices_new);


        roomPrices = RoomPrice.GetRoomPrices();

        priceAdapter = new RoomPriceAdapter(this, roomPrices);

        roomPricesList=findViewById(R.id.priceList) ;
        roomPricesList.setAdapter(priceAdapter);

        roomPricesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomPrice = (RoomPrice) parent.getItemAtPosition(position);
                EditDeleteFragment dlg = new EditDeleteFragment();
                dlg.show(getFragmentManager(), "");
            }
        });




    }

    public void DoConfirmationAction(){
    //          mListener.onFragmentDoDelete();
    //            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        onFragmentDoDelete();
    }



    public void onFragmentDoDelete(){
        roomPrice.Delete();
        roomPrices.remove(roomPrice) ;
        priceAdapter.SetPrices(roomPrices);

    }
    public void onFragmentDoEdit(){

        AddRoomPriceFragment newRoomPrice = new AddRoomPriceFragment();
        Bundle args = new Bundle();
        args.putInt("IdEntity", roomPrice.Id);
        Log.d("axa","Put Id="+String.valueOf(roomPrice.Id));
        newRoomPrice.setArguments(args);
        newRoomPrice.show(getFragmentManager(), "newPrice");
    }
    public void OnClickAddRoomPrice(View view){
            AddRoomPriceFragment newRoomPrice = new AddRoomPriceFragment();
            newRoomPrice.show(getFragmentManager(), "newPrice");
        }
        public void OnDataRefresh(){
            roomPrices = RoomPrice.GetRoomPrices();
            priceAdapter.SetPrices(roomPrices);
        }

}
