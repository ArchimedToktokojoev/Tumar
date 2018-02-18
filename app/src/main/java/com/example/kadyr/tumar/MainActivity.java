package com.example.kadyr.tumar;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.kadyr.tumar.DataRepository.Booking;
import com.example.kadyr.tumar.DataRepository.Room;
import com.example.kadyr.tumar.DataRepository.RoomGroup;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.onDestroyListener, View.OnClickListener
        {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        if(toolbar!= null){
            setSupportActionBar((toolbar));
            toolbar.setNavigationIcon(R.drawable.logotip);
            toolbar.setNavigationOnClickListener(this);

        }

         DatabaseHelper.GetInstance(getApplicationContext()).create_db();

        LoginFragment LoginFragment = new LoginFragment();
           LoginFragment.show(getSupportFragmentManager(), "custom");

        SharedPreferences settings = getSharedPreferences("Preferences", MODE_PRIVATE);
        PublicVariables.CurrentFilter = settings.getInt("Filter",Constants.FilterPlace);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }


    }

    public void onClick(View view){
        NavMenuFragment dlg = new NavMenuFragment();

        dlg.show(getFragmentManager(),"openClient");

    }

    @Override
    public void onRestart(){

        super.onRestart();
        changeMainView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_filter, menu);
        return true;
    }

    @Override
    public void changeMainView(){
        setTitle(PublicVariables.CurrentUser.GetName());
        TextView txtFree = findViewById(R.id.textFreePlaces);
        TextView txtBusy = findViewById(R.id.textBusyPlaces);


        int freePlaces = 0;
        int totalPlaces = 0;

        List<RoomGroup> rgs = RoomGroup.GetRoomGroups();

        if(rgs.size()>0)
        {
            freePlaces+=setGroupViewes(rgs.get(0), 1);
            totalPlaces+=RoomGroup.GetCount(rgs.get(0).GetId()) ;
            Button btn = findViewById(R.id.firstFloorFreeRooms) ;
            btn.setTag("free"+rgs.get(0).GetId());
            btn = findViewById(R.id.firstFloorBusyRooms) ;
            btn.setTag("busy"+rgs.get(0).GetId());

        }
        if(rgs.size()>1)
        {
            freePlaces+=setGroupViewes(rgs.get(1), 2);
            totalPlaces+=RoomGroup.GetCount(rgs.get(1).GetId()) ;
            Button btn = findViewById(R.id.secondFloorFreeRooms) ;
            btn.setTag("free"+rgs.get(1).GetId());
            btn = findViewById(R.id.secondFloorBusyRooms) ;
            btn.setTag("busy"+rgs.get(1).GetId());
        }

        int busyPlaces = totalPlaces-freePlaces;
        int freePlacesPercent = (freePlaces* 100)/totalPlaces;

        txtFree.setText(String.valueOf(freePlaces));
        txtFree.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,  freePlacesPercent));

        txtBusy.setText(String.valueOf(busyPlaces));
        txtBusy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 100 - freePlacesPercent));

    }

    private int setGroupViewes(RoomGroup roomGroup, int floor){

        TextView txtTitle = findViewById(floor==1 ? R.id.firstFloor:R.id.secondFloor);
        txtTitle.setText(roomGroup.GetName());
        int id = roomGroup.GetId();
        int free = RoomGroup.getFreePlaces(id);
        long countPlaces = RoomGroup.GetCount(id) ;
        Button button = findViewById(floor==1?R.id.firstFloorFreeRooms:R.id.secondFloorFreeRooms);
        button.setText(String.valueOf(free));
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT,
                (free* 100)/countPlaces));
        button = findViewById(floor==1?R.id.firstFloorBusyRooms:R.id.secondFloorBusyRooms);
        int busy = (int)countPlaces-free;
        button.setText(String.valueOf(busy));
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT,
                ((busy)* 100)/countPlaces));
        ImageView imageView = findViewById(floor==1?R.id.imgGroupLocked1:R.id.imgGroupLocked2);
        imageView.setVisibility(RoomGroup.IsExistBookedRoom(id)?View.VISIBLE:View.INVISIBLE);

        return free ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(!item.isChecked()) item.setChecked(true);
        return setFilter(id, item);
    }

    private boolean setFilter(int id, MenuItem item){
        TextView textFilter = findViewById(R.id.txtFilter);
        ImageView imageFilter =  findViewById(R.id.imgFilter);

        switch(id){
            case R.id.rooms :
                textFilter.setText("Комнаты");
                imageFilter.setImageResource(R.drawable.ic_furniture);
                PublicVariables.CurrentFilter = Constants.FilterPlace;
                return true;
            case R.id.debtForRooms:
                textFilter.setText("Долги (Номера)");
                imageFilter.setImageResource(R.drawable.ic_debt);
                PublicVariables.CurrentFilter = Constants.FilterDebtRoom;
                return true;
            case R.id.debtForKitchen:
                textFilter.setText("Долги (Кухня)");
                imageFilter.setImageResource(R.drawable.ic_debt);
                PublicVariables.CurrentFilter = Constants.FilterDebtKitchen;
                return true;
            case R.id.orderForKitchen:
                textFilter.setText("Заказы (Кухня)");
                imageFilter.setImageResource(R.drawable.ic_kitchen);
                PublicVariables.CurrentFilter = Constants.FilterOrderKitchen;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickGroupRoom(View view){
        try{
            Intent intent = new Intent(this, GroupContentActivity.class);
            String tag = (String)view.getTag();
            int id= Integer.valueOf(tag.substring(4,5)) ;

            intent.putExtra("IdFloor",id) ;
            startActivity(intent);
        }
        catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}


