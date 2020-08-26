package org.vaadin.erik.game.ai.recording;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.vaadin.erik.game.ai.pathing.NodeData;
import org.vaadin.erik.game.shared.Direction;

import java.util.Map;

@JsonSerialize(using = RecordDataSerializer.class)
public class RecordData {

    private final NodeData startNode;
    private final NodeData endNode;
    private final Map<Double, Direction[]> actionMap;

    public RecordData(NodeData startNode, NodeData endNode, Map<Double, Direction[]> actionMap) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.actionMap = actionMap;
    }

    public NodeData getStartNode() {
        return startNode;
    }

    public NodeData getEndNode() {
        return endNode;
    }

    public Map<Double, Direction[]> getActionMap() {
        return actionMap;
    }
}
