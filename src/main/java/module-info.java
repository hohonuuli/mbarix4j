/**
 * @author Brian Schlining
 * @since 2019-08-23T14:45:00
 */
module mbarix4j {
    requires java.desktop;
    requires java.prefs;
    requires java.scripting;
    requires java.sql;

    exports mbarix4j3.awt;
    exports mbarix4j3.awt.event;
    exports mbarix4j3.awt.image;
    exports mbarix4j3.awt.layout;
    exports mbarix4j3.concurrent;
    exports mbarix4j3.geometry;
    exports mbarix4j3.gis.util;
    exports mbarix4j3.io;
    exports mbarix4j3.math;
    exports mbarix4j3.model.solar;
    exports mbarix4j3.model.tidal;
    exports mbarix4j3.net;
    exports mbarix4j3.ocean;
    exports mbarix4j3.script.js;
    exports mbarix4j3.solar;
    exports mbarix4j3.text;
    exports mbarix4j3.util;
    exports mbarix4j3.util.prefs;
    exports mbarix4j3.util.stream;

    opens mbarix4j3.images;
    opens mbarix4j3.js;

}