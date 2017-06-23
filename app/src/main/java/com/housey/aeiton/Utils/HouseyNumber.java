package com.housey.aeiton.Utils;

/**
 * Created by User on 14-Feb-17.
 */

public class HouseyNumber {

    int num;
    boolean selected, active;

    public  HouseyNumber(){
        num = 0;
        selected = false;
        active = false;
    }
    public HouseyNumber(int num, boolean active){
        this.num = num;
        this.active = active;
        this.selected = false;
    }

    public HouseyNumber(int num){
        this.num = num;
        this.active = true;
        this.selected = false;
    }

    public HouseyNumber(boolean active){
        this.num = 0;
        this.selected = false;
        this.active = false;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isSelected() {
        return selected;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate(){
        this.active = false;
    }

    public void selected() {
        this.selected = true;
    }

    public void unSelect(){
        this.selected = false;
    }
}
