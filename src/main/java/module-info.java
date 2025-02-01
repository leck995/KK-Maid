module cn.tealc.kkmaid {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires org.controlsfx.controls;
    requires atlantafx.base;
    requires eventbus.java;
    requires com.jfoenix;
    requires javafx.media;
    requires jdk.crypto.cryptoki;
    requires net.lingala.zip4j;
    requires org.slf4j;
    requires uk.co.caprica.vlcj;
    requires jaudiotagger;
    requires JRegistry;
    requires net.coobird.thumbnailator;
    requires filters;
    requires javafx.swing;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    // add icon pack modules
    requires org.kordamp.ikonli.material2;

    opens cn.tealc995.kkmaid to javafx.fxml;
    opens cn.tealc995.kkmaid.ui to javafx.fxml;
    exports cn.tealc995.kkmaid;
    exports cn.tealc995.api;
    exports cn.tealc995.api.model;
    exports cn.tealc995.kkmaid.ui;
    exports cn.tealc995.kkmaid.zip;
    exports cn.tealc995.kkmaid.event;
    exports cn.tealc995.api.model.playList;
    exports cn.tealc995.aria2.model;
    exports cn.tealc995.aria2;
}