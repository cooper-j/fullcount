package edu.csulb.android.fullcount;

import java.io.Serializable;

/**
 * Created by james_000 on 3/23/2015.
 */
public class Player implements Serializable {

    private String _name;
    private int _imageId;

    public Player(String name, int imageId){
        _name = name;
        _imageId = imageId;
    }

    public String getName(){
        return this._name;
    }

    public int getImageId(){
        return this._imageId;
    }
}
