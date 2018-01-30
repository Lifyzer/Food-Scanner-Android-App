package com.foodscan.WsHelper.helper;

import java.util.ArrayList;

/**
 * Created by c157 on 23/03/17.
 */

public class FileUploadInput {

    public class Item {
        public Item(String key, String value) {
            this.key = key;
            this.value = value;
        }


        public String key;
        public String value;
        public ArrayList arrayList;
    }


    public ArrayList<Item> paramList = new ArrayList<>();
    public ArrayList<Item> fileList = new ArrayList<>();
}
