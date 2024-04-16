package com.zielonka.lab.lab2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "phone")
public class Phone implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "producer")
    private String producer;
    @ColumnInfo(name = "model")
    private String model;
    @ColumnInfo(name = "version")
    private String version;
    @ColumnInfo(name = "website")
    private String website;

    public Phone(String producer, String model, String version, String website) {
        this.producer = producer;
        this.model = model;
        this.version = version;
        this.website = website;
    }

    public Phone(Parcel source) {
        id = source.readInt();
        producer = source.readString();
        model = source.readString();
        version = source.readString();
        website = source.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(producer);
        dest.writeString(model);
        dest.writeString(version);
        dest.writeString(website);
    }

    public static final Creator<Phone> CREATOR = new Creator<Phone>() {
        @Override
        public Phone createFromParcel(Parcel source) {
            return new Phone(source);
        }

        @Override
        public Phone[] newArray(int size) {
            return new Phone[size];
        }
    };
}
