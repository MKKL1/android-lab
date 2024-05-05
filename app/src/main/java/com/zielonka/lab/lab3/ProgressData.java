package com.zielonka.lab.lab3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ProgressData implements Parcelable {
    private int progressBytes;
    private int fileSize;
    private String status;

    public ProgressData(int progress, int filesize, String status) {
        this.progressBytes = progress;
        this.fileSize = filesize;
        this.status = status;
    }

    public ProgressData(Parcel parcel){
        progressBytes = parcel.readInt();
        fileSize = parcel.readInt();
        status = parcel.readString();
    }

    public int getProgressBytes() {
        return progressBytes;
    }

    public void setProgressBytes(int progressBytes) {
        this.progressBytes = progressBytes;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(progressBytes);
        dest.writeInt(fileSize);
        dest.writeString(status);
    }

    public static final Creator<ProgressData> CREATOR = new Creator<ProgressData>() {
        @Override
        public ProgressData createFromParcel(Parcel source) {
            return new ProgressData(source);
        }

        @Override
        public ProgressData[] newArray(int size) {
            return new ProgressData[size];
        }
    };
}
