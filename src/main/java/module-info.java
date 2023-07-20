module cn.tealc995.asmronline {
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


    opens cn.tealc995.asmronline to javafx.fxml;
    opens cn.tealc995.asmronline.ui to javafx.fxml;
    exports cn.tealc995.asmronline;
    exports cn.tealc995.asmronline.api;
    exports cn.tealc995.asmronline.api.model;
    exports cn.tealc995.asmronline.ui;
    exports cn.tealc995.asmronline.zip;
    exports cn.tealc995.asmronline.event;
}