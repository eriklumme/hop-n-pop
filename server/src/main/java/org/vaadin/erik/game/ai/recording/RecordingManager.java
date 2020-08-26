package org.vaadin.erik.game.ai.recording;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.erik.game.ai.pathing.NodeData;
import org.vaadin.erik.game.ai.pathing.PathingData;
import org.vaadin.erik.game.ai.pathing.PathingManager;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Direction;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class RecordingManager {

    private static final Logger logger = LogManager.getLogger(RecordingManager.class);
    private static final String PATH = "server/static/pathing.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static RecordData[][] recordDataMap = new RecordData[Constants.BLOCKS_VERTICAL][Constants.BLOCKS_HORIZONTAL];

    static {
        try {
            long time = System.nanoTime();

            PathingData pathingData = PathingManager.getPathingData();
            String data = new BufferedReader(new FileReader(PATH))
                    .lines().collect(Collectors.joining());
            Map<?, ?>[][] dataMap = objectMapper.readValue(data, Map[][].class);

            for (int y = 0; y < dataMap.length; y++) {
                for (int x = 0; x < dataMap[y].length; x++) {
                    Map<String, Object> recordData = (Map<String, Object>) dataMap[y][x];
                    if (recordData == null) {
                        continue;
                    }
                    NodeData startNode = pathingData.getClosestNode(
                            (int) recordData.get("startX"),
                            (int) recordData.get("startY"),
                            PathingData.SearchMode.EXACT);
                    NodeData endNode = pathingData.getClosestNode(
                            (int) recordData.get("endX"),
                            (int) recordData.get("endY"),
                            PathingData.SearchMode.EXACT);
                    Map<Double, Direction[]> actionMap = ((List<?>) recordData.get("actions")).stream()
                            .map(List.class::cast)
                            .collect(Collectors.toMap(
                                    list -> (double) list.get(0),
                                    list -> {
                                        List<Integer> values = (List<Integer>) list.get(1);
                                        return values.stream()
                                                .map(i -> Direction.values()[i])
                                                .toArray(Direction[]::new);
                                    },
                                    (u, v) -> { throw new IllegalStateException("Duplicate key " + u); },
                                    LinkedHashMap::new));
                    recordDataMap[y][x] = new RecordData(startNode, endNode, actionMap);
                }
            }

            logger.info("Loaded path records in {} ms", (System.nanoTime() - time)/1000000);
        } catch (FileNotFoundException | JsonProcessingException e) {
            logger.error("Error reading path records", e);
            throw new RuntimeException(e);
        }
    }

    public static RecordData getRecordedPathFrom(int x, int y) {
        return recordDataMap[y][x];
    }

    public static void addRecordData(RecordData recordData) {
        NodeData startNode = recordData.getStartNode();
        recordDataMap[startNode.getIndexY()][startNode.getIndexX()] = recordData;
    }

    public static void writeToFile() {
        try {
            File file = new File(PATH);
            objectMapper.writeValue(file, recordDataMap);
        } catch (IOException e) {
            logger.error("Error writing recorded path data to file", e);
        }
    }
}
