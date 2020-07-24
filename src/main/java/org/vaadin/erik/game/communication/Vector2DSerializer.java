package org.vaadin.erik.game.communication;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.IOException;

public class Vector2DSerializer extends StdSerializer<Vector2D> {

    public Vector2DSerializer() {
        this(null);
    }

    protected Vector2DSerializer(Class<Vector2D> t) {
        super(t);
    }

    @Override
    public void serialize(Vector2D value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("velocityY", value.getX());
        gen.writeNumberField("velocityY", value.getY());
        gen.writeEndObject();
    }
}
