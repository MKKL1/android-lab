package com.zielonka.lab.lab3;

public class DownloadFileInfo {
    private final String type;
    private final int size;

    public DownloadFileInfo(String type, int size) {
        this.type = type;
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}
