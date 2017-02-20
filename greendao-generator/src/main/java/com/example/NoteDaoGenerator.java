package com.example;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

import java.io.IOException;

public class NoteDaoGenerator {

    private static final String PATH = "com.example.timofey.myapplication.database";
    private static final int VERSION = 1;


    public static void main(String[] args) {
        Schema schema = new Schema(VERSION, PATH);

        Entity item = schema.addEntity("Note");
        item.addIdProperty().autoincrement();
        item.addStringProperty("title");
        item.addStringProperty("content");
        item.addIntProperty("importance");

        try {
            new DaoGenerator().generateAll(schema, "./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
