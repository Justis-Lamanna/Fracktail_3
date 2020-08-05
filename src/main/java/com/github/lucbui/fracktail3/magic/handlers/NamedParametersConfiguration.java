package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.exception.CommandUseException;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandContext;
import org.apache.commons.lang3.ArrayUtils;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;

public class NamedParametersConfiguration {
    private final List<Tuple2<String, Range>> parameterMappings;

    public NamedParametersConfiguration() {
        this.parameterMappings = new ArrayList<>();
    }

    public boolean hasKey(String key) {
        return this.parameterMappings.stream()
                .anyMatch(t -> t.getT1().equals(key));
    }

    public void add(String key, Range range) {
        this.parameterMappings.add(Tuples.of(key, range));
    }

    public Parameters resolve(CommandContext context) {
        Map<String, String> params = new HashMap<>(parameterMappings.size());
        for(Tuple2<String, Range> mapping : parameterMappings) {
            String[] inputParams = context.getNormalizedParameters();
            Range range = mapping.getT2();
            if(range.getStart() < 0 || range.getStart() >= inputParams.length ||
                    range.getEnd() < -1 || range.getEnd() >= inputParams.length) {
                throw new CommandUseException("Command range " + range + " does not apply to parameters " + Arrays.toString(inputParams));
            }
            if(range.isUnbounded()) {
                params.put(mapping.getT1(), String.join(" ", ArrayUtils.subarray(inputParams, range.getStart(), inputParams.length)));
            } else if(range.getStart() < inputParams.length && range.getEnd() < inputParams.length) {
                int start = range.getStart();
                int end = range.getEnd();
                if(start == end) {
                    params.put(mapping.getT1(), inputParams[start]);
                } else {
                    params.put(mapping.getT1(), String.join(" ", ArrayUtils.subarray(inputParams, start, end + 1)));
                }
            }
        }
        return new Parameters(params);
    }
}
