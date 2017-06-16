package com.app.berguig.veggeat;

import java.util.UUID;

/**
 * Created by stagiaire on 16/06/2017.
 */

public class Restaurant {
    public UUID mId;
    public String mName;
    public long mLAT;
    public long mLONG;


    public Restaurant(){
        this(UUID.randomUUID());
    }

    public Restaurant(UUID id){
        mId = id;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public long getmLAT() {
        return mLAT;
    }

    public void setmLAT(long mLAT) {
        this.mLAT = mLAT;
    }

    public long getmLONG() {
        return mLONG;
    }

    public void setmLONG(long mLONG) {
        this.mLONG = mLONG;
    }
}

