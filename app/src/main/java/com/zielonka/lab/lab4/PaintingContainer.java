package com.zielonka.lab.lab4;

import java.util.ArrayList;
import java.util.List;

public class PaintingContainer {

    public static final List<PaintingItem> paintings = new ArrayList<PaintingItem>();

    public static void addItem(PaintingItem item) {
        paintings.add(item);
    }
    public static List<PaintingItem> getPaintings(){
        return paintings;
    }

    public static class PaintingItem{

        public String filename;
        public String filepath;

        public PaintingItem(String filename, String filepath) {
            this.filename = filename;
            this.filepath = filepath;
        }

        public String getFilepath() {
            return filepath;
        }

        public String getFilename() {
            return filename;
        }

        @Override
        public String toString() {
            return filename;
        }
    }
}