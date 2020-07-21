package org.vaadin.erik.game.client;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public class Logger {

    @JSBody(params = "message", script = "console.log(message)")
    public static native void log(JSObject message);

    @JSBody(params = "message", script = "console.log(message)")
    public static native void log(String message);

    @JSBody(params = "message", script = "console.warn(message)")
    public static native void warn(JSObject message);

    @JSBody(params = "message", script = "console.warn(message)")
    public static native void warn(String message);
}
