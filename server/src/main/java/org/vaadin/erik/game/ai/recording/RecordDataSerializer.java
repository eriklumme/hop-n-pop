package org.vaadin.erik.game.ai.recording;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.vaadin.erik.game.shared.Direction;

import java.io.IOException;
import java.util.Map;

public class RecordDataSerializer extends StdSerializer<RecordData> {

    public RecordDataSerializer() {
        this(null);
    }

    protected RecordDataSerializer(Class<RecordData> t) {
        super(t);
    }

    @Override
    public void serialize(RecordData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("startX", value.getStartNode().getIndexX());
        gen.writeNumberField("startY", value.getStartNode().getIndexY());
        gen.writeNumberField("endX", value.getEndNode().getIndexX());
        gen.writeNumberField("endY", value.getEndNode().getIndexY());
        gen.writeArrayFieldStart("actions");
        for (Map.Entry<Double, Direction[]> action: value.getActionMap().entrySet()) {
            gen.writeStartArray();
            gen.writeNumber(action.getKey());
            gen.writeArray(Direction.toIntArray(action.getValue()), 0, action.getValue().length);
            gen.writeEndArray();
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
