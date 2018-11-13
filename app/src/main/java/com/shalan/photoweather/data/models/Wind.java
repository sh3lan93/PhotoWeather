
package com.shalan.photoweather.data.models;

import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("deg")
    private Long mDeg;
    @SerializedName("speed")
    private Double mSpeed;

    public Long getDeg() {
        return mDeg;
    }

    public void setDeg(Long deg) {
        mDeg = deg;
    }

    public Double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Double speed) {
        mSpeed = speed;
    }

}
