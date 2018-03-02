package com.example.kadyr.tumar;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EditDeleteFragment extends DialogFragment implements View.OnClickListener,
        ConfirmationFragment.onClickAction
{
    private OnEditDeleteFragmentListener mListener;


    public void DoConfirmationAction(){
        mListener.onFragmentDoDelete();
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }
    public EditDeleteFragment() {
        // Required empty public constructor
    }

    public void onClick(View v1) {
        if(v1.getId()==R.id.deleteButton){

            ConfirmationFragment dlg = new ConfirmationFragment();
            dlg.show(getFragmentManager(), "deleteAction");

            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
        if(v1.getId()==R.id.editButton){
            mListener.onFragmentDoEdit();
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  = inflater.inflate(R.layout.fragment_edit_delete, container, false);
        v.findViewById(R.id.deleteButton).setOnClickListener(this); ;
        v.findViewById(R.id.editButton).setOnClickListener(this);
        return v;



    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditDeleteFragmentListener) {
            mListener = (OnEditDeleteFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnEditDeleteFragmentListener {
        void onFragmentDoDelete();
        void onFragmentDoEdit();
    }
}
