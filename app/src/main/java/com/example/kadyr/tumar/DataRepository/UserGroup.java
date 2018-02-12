package com.example.kadyr.tumar.DataRepository;

/**
 * Created by Kadyr on 2/2/2018.
 */

public class UserGroup {
    private int Id;
    private String Name;

    public UserGroup(int id, String name){
        Id = id;
        this.Name = name;
    }

    public int getId(){  return Id;    }
    public void setId(int id){ Id=id; }

    public String getName(){ return Name;}
    public void setName(String name){ Name=name;}
}
