open module cn.tealc.kkmaid {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.media;
    requires atlantafx.base;
    requires com.jfoenix;
    requires uk.co.caprica.vlcj;
    requires org.controlsfx.controls;

    requires jdk.crypto.cryptoki;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires eventbus.java;
    requires jaudiotagger;
    requires JRegistry;
    requires net.coobird.thumbnailator;
    requires filters;

    requires org.slf4j;
    requires ch.qos.logback.classic;



    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.material2;
    requires java.net.http;
    requires org.apache.commons.compress;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.codec;
    requires java.naming;


    exports cn.tealc995.kkmaid;
    exports cn.tealc995.kikoreu;
    exports cn.tealc995.kikoreu.model;
    exports cn.tealc995.kkmaid.ui;
    exports cn.tealc995.kkmaid.zip;
    exports cn.tealc995.kkmaid.event;
    exports cn.tealc995.kikoreu.model.playList;
    exports cn.tealc995.aria2.model;
    exports cn.tealc995.aria2;
    exports cn.tealc995.kikoreu.api;
    exports cn.tealc995.kkmaid.config;
}