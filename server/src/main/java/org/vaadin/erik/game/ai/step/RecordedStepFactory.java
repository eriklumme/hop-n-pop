package org.vaadin.erik.game.ai.step;

import org.vaadin.erik.game.ai.recording.RecordData;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.Player;

import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.Optional;

public class RecordedStepFactory extends AbstractStepFactory {

    private final RecordData recordData;
    private final double weight;

    public RecordedStepFactory(RecordData recordData) {
        super(recordData.getEndNode());
        this.recordData = recordData;

        Optional<Double> optionalWeight = recordData.getActionMap().keySet().stream()
                .skip(recordData.getActionMap().size() - 1)
                .findFirst();
        if (!optionalWeight.isPresent()) {
            throw new IllegalArgumentException("Failed to find the last key in the action map");
        }
        weight = optionalWeight.get();
    }

    @Override
    public Step getInstance(Player forPlayer) {
        return new RecordStep();
    }

    @Override
    public double getWeight() {
        return weight;
    }

    public class RecordStep extends AbstractStep {

        private final Iterator<Double> iterator;
        private double currentValue;
        private double sumDelta = 0;

        private RecordStep() {

            iterator = recordData.getActionMap().keySet().iterator();
            currentValue = iterator.next();
        }

        @Override
        public @NotNull Direction[] getCommand(double delta) {
            sumDelta += delta;

            while (sumDelta > currentValue && iterator.hasNext()) {
                currentValue = iterator.next();
            }
            return recordData.getActionMap().get(currentValue);
        }

        @Override
        public boolean targetReached() {
            return !iterator.hasNext();
        }
    }
}
