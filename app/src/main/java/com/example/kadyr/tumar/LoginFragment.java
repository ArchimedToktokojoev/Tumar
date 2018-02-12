package com.example.kadyr.tumar;

/**
 * Created by Kadyr on 1/31/2018.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kadyr.tumar.DataRepository.User;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends DialogFragment {
    View viewA;
    SharedPreferences settings;
    private onDestroyListener mListener;

    interface onDestroyListener{
        public void changeMainView();
    }

    @Override
    public void onAttach(Activity activity){
    super.onAttach(activity);
        try {
            mListener = (onDestroyListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewA = LayoutInflater.from(getContext()).inflate(R.layout.login_dialog,(ViewGroup)getView(),false);


        settings = getActivity().getSharedPreferences("Preferences", MODE_PRIVATE);
        String login = settings.getString("Login","");
         EditText loginText = (EditText) viewA.findViewById(R.id.loginText);
        loginText.setText(login);

        DialogInterface.OnClickListener mCancelLintener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int i) {
                getActivity().finish(); }
        };


        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        Dialog dialog =
                builder.setTitle("Вход в систему")
                      .setView(viewA)
                      .setNegativeButton("Отмена", mCancelLintener)
                .setPositiveButton("OK", null)
                      .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface d) {

                Button button = ((AlertDialog) d).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                      onClickOK();
                        //Dismiss once everything is OK.

                    }
                });
            }
        });

return dialog;

    }

    private void onClickOK(){



        List<User> users =  User.GetUsers();
        final EditText login = (EditText) viewA.findViewById(R.id.loginText);
        final EditText password = (EditText) viewA.findViewById(R.id.passwordText);

        boolean existUser=false;
       for (User user: users
                ) {
            if(user.GetLogin().equals(login.getText().toString())) {
                existUser=true;
                //               if(user.GetPassword().equals(password.getText().toString())){

                    ActionOK(user);
                    getDialog().dismiss();
//                    break;
//                }
//                else {
//                    Toast.makeText( getActivity(),"Пароль неверный!",Toast.LENGTH_LONG).show();
//
//                }
            }
        }

        if(!existUser){
            Toast.makeText( getActivity(),"Логин неверный!",Toast.LENGTH_LONG).show();
        }

    }

    private void ActionOK(User user){
        PublicVariables.CurrentUser = user;
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Login",user.GetLogin());
        editor.apply();
        mListener.changeMainView();
    }


}
