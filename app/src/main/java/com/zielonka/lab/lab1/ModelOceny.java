package com.zielonka.lab.lab1;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ModelOceny implements Parcelable {
    private String nazwa;
    private int ocena;

    public ModelOceny(String nazwa, int ocena) {
        this.nazwa = nazwa;
        this.ocena = ocena;
    }

    protected ModelOceny(Parcel in) {
        nazwa = in.readString();
        ocena = in.readInt();
    }

    public static final Creator<ModelOceny> CREATOR = new Creator<ModelOceny>() {
        @Override
        public ModelOceny createFromParcel(Parcel in) {
            return new ModelOceny(in);
        }

        @Override
        public ModelOceny[] newArray(int size) {
            return new ModelOceny[size];
        }
    };

    public String getNazwa() {
        return nazwa;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(nazwa);
        parcel.writeInt(ocena);
    }
}
