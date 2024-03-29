
package com.shalan.photoweather.data.models;

import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("deg")
    private Double mDeg;
    @SerializedName("speed")
    private Double mSpeed;

    public Double getDeg() {
        return mDeg;
    }

    public void setDeg(Double deg) {
        mDeg = deg;
    }

    public Double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Double speed) {
        mSpeed = speed;
    }

}
