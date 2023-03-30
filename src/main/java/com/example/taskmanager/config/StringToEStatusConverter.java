package com.example.taskmanager.config;

import com.example.taskmanager.model.Status;
import org.springframework.core.convert.converter.Converter;

public class StringToEStatusConverter implements Converter<String, Status> {
    @Override
    public Status convert(String source) {
        // In case if user pass incorrect task status in URL parameter,
        // an exception will be thrown and null will be returned.
        try {
            return Status.valueOf(source.toUpperCase());
        }catch (Exception e){
            return null;
        }
    }
}
