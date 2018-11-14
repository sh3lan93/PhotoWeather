package com.shalan.photoweather.data.realm_models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class HistoryModel extends RealmObject {

    @PrimaryKey
    private String name;
    private String imagePath;

    public HistoryModel() {
    }

    public HistoryModel(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
}
