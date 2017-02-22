package com.example.timofey.myapplication.database;

import org.greenrobot.greendao.annotation.*;

import java.io.Serializable;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "NOTE".
 */
@Entity
public class Note implements Serializable{

    public static final String TAG = "NOTE_ITEM";

    @Id(autoincrement = true)
    private Long id;
    private String title;
    private String content;
    private Integer importance;
    private String photoPath;
    private String placeName;
    private Double latitude;
    private Double longitude;

    @Generated
    public Note() {
    }

    public Note(Long id) {
        this.id = id;
    }

    @Generated
    public Note(Long id, String title, String content, Integer importance, String photoPath, String placeName, Double latitude, Double longitude) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.importance = importance;
        this.photoPath = photoPath;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getImportance() {
        return importance;
    }

    public void setImportance(Integer importance) {
        this.importance = importance;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
