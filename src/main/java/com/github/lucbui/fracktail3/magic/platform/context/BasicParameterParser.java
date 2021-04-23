package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.command.Command;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Pattern;

public class BasicParameterParser implements ParameterParser {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\s+");

    @Override
    public Parameters parseParametersFromMessage(Command command, String message) {
        List<Command.Parameter> parameters = command.getParameters();
        if(parameters.size() == 0) {
            //Legacy support I guess
            return process(command, message, SPLIT_PATTERN.split(message));
        }
        String[] paramsAsStr = StringUtils.isEmpty(message) ?
                validateAndNormalize(parameters, new String[0]) :
                validateAndNormalize(parameters, SPLIT_PATTERN.split(message, parameters.size()));

        Queue<Integer> indexes = getParameterIndices(parameters, paramsAsStr);

        Object[] params = new Object[parameters.size()];
        int paramsIdx = 0;
        while(!indexes.isEmpty()) {
            params[indexes.remove()] = paramsAsStr[paramsIdx++];
        }
        return process(command, message, params);
    }

    protected Parameters process(Command command, String message, Object[] calculate) {
        return new Parameters(message, calculate);
    }

    private Queue<Integer> getParameterIndices(List<Command.Parameter> parameters, String[] paramsAsStr) {
        //Figure out the mapping between provided String params, and the parameters themselves.
        //Required parameters are mapped first, while optional ones are applied left-to-right until
        //all parameters are resolved. Extras are discarded.
        List<Integer> requireds = new ArrayList<>();
        Queue<Integer> optionals = new PriorityQueue<>();

        for(int idx = 0; idx < parameters.size(); idx++) {
            if(parameters.get(idx).isOptional()) {
                optionals.add(idx);
            } else {
                requireds.add(idx);
            }
        }

        Queue<Integer> indexes = new PriorityQueue<>(requireds);
        while(indexes.size() < paramsAsStr.length && !optionals.isEmpty()) {
            indexes.add(optionals.remove());
        }
        return indexes;
    }

    private String[] validateAndNormalize(List<Command.Parameter> parameters, String[] paramsAsStr) {
        long maxParams = parameters.size();
        long minParams = maxParams - parameters.stream().filter(Command.Parameter::isOptional).count();

        if(paramsAsStr.length < minParams) {
            throw new IllegalArgumentException("Not enough parameters specified.");
        }
        return paramsAsStr;
    }
}
