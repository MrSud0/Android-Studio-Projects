package com.example.koumakis.lab5;

/**
 * Created by Jason on 12/18/2017.
 */
import android.app.Application;



public class GlobalClass extends Application{

    private boolean flag;
    public boolean getFlag() {return flag;}
    public void setFlag(boolean value) {flag = value;}
    public void Switch() {if(flag==true) {flag=false;}else {flag=true;}}
    public GlobalClass(){
        flag=false;
    }


}