package ru.otus.dataprocessor;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        // группирует выходящий список по name, при этом суммирует поля value
        Map<String, Double> map = new TreeMap<>();
        for (Measurement measurement : data) {
            Double currentValue = map.get(measurement.name());
            map.put(measurement.name(), currentValue != null ? currentValue + measurement.value() : measurement.value());
        }
        return map;
    }
}
