/**
 * @author Brian Schlining
 * @since 2019-08-23T14:45:00
 */
module mbarix4j {
    requires java.desktop;
    requires java.prefs;
    requires java.scripting;
    requires java.sql;
    requires org.slf4j;

    exports mbarix4j.awt;
    exports mbarix4j.awt.event;
    exports mbarix4j.awt.image;
    exports mbarix4j.awt.layout;
    exports mbarix4j.concurrent;
    exports mbarix4j.geometry;
    exports mbarix4j.gis.util;
    exports mbarix4j.io;
    exports mbarix4j.math;
    exports mbarix4j.model.solar;
    exports mbarix4j.model.tidal;
    exports mbarix4j.nativelib;
    exports mbarix4j.net;
    exports mbarix4j.ocean;
    exports mbarix4j.solar;
    exports mbarix4j.sql;
    exports mbarix4j.swing;
    exports mbarix4j.swing.actions;
    exports mbarix4j.swing.table;
    exports mbarix4j.swing.text;
    exports mbarix4j.swingworker;
    exports mbarix4j.text;
    exports mbarix4j.util;
    exports mbarix4j.util.prefs;
    exports mbarix4j.util.stream;

    opens mbarix4j.images;
    opens mbarix4j.js;
    opens mbarix4j.model.solar;

}