package com.example.kadyr.tumar;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kadyr.tumar.DataRepository.Room;

import java.util.List;

public class GroupContentActivity extends AppCompatActivity
        implements CheckInFragment.OnCheckInFragmentListener, CheckOutFragment.OnCheckOutFragmentListener{




    List<Room> rooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_content);
        Toolbar toolbar =  findViewById(R.id.toolbar1);
        if(toolbar!= null)
        {
            setSupportActionBar((toolbar));
            toolbar.setLogo(R.drawable.logotip);
            setTitle(PublicVariables.CurrentUser.GetName());
        }
        InitData();

    }


    @Override
    public void onFloorUpdate() {
        InitData();
    }

    private void InitData(){

            int idFloor = getIntent().getExtras().getInt("IdFloor");
            rooms = Room.GetRooms(idFloor);
            String packageName=getPackageName();
            for (int i = 1; i<= 8; i++) {
                if(i<=rooms.size()){
                    Room room = rooms.get(i-1);
                    int bedsCount = room.GetBedsCount();
                    int idBtn = getResources().getIdentifier("btn"+i, "id", packageName);
                    Button btn =  findViewById(idBtn); // get the element
                    btn.setTag(room.getId());


                    int idDayTxt = getResources().getIdentifier("day"+i, "id", packageName);
                    TextView dayTxt =  findViewById(idDayTxt); // get the element
                    if(room.getStatus()==Constants.RoomStatusFree)
                        dayTxt.setText("");
                    else{

                        dayTxt.setText(String.valueOf(room.CheckOutDays()));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin=12;
                        dayTxt.setLayoutParams(params);
                    }

                    int idImgBtn = getResources().getIdentifier("img"+i, "id", packageName);
                    ImageButton imageButton =  findViewById(idImgBtn); // get the element
                    imageButton.setTag(room.getId());

                    Drawable imgLock = getResources().getDrawable( R.drawable.ic_lock80 );
                    btn.setText(String.valueOf(room.getName()));
                    if(room.IsBooked()) {
                        btn.setCompoundDrawablesWithIntrinsicBounds(null,null,null,imgLock);
                    }
                    else btn.setCompoundDrawables(null,null,null,null);
                    if(room.getStatus()==Constants.RoomStatusFree)
                        imageButton.setImageResource(bedsCount==2?
                                R.drawable.ic_two_beds_horizontal_green:
                                R.drawable.ic_three_beds_horizontal_green);
                    else
                        imageButton.setImageResource(bedsCount==2?
                                R.drawable.ic_two_beds_horizontal_red:
                                R.drawable.ic_three_beds_horizontal_red);
                }
            }
        }

    public void onClickRoom(View view){
        Room room=null ;
        try{
            int tag = (int)view.getTag();
            for (Room r: rooms ) {
                if(r.getId()==tag) room=r ;
            }
            if(room !=null){
                DialogFragment dlg = new OperationFragment();
                Bundle args = new Bundle();
                args.putString("idRoom", String.valueOf(room.getId()));
                dlg.setArguments(args);
                dlg.show(getFragmentManager(), "dlg1");

            }

        }
        catch(Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    }

