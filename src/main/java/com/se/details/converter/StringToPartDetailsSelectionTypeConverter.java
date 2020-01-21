package com.se.details.converter;

import com.se.details.enumeration.PartDetailsResultType;
import org.springframework.core.convert.converter.Converter;

/**
 * Converter used by spring to match user input string to {@link PartDetailsResultType} enum instance
 */
public class StringToPartDetailsSelectionTypeConverter implements Converter<String, PartDetailsResultType> {

    @Override
    public PartDetailsResultType convert(String source) {
        return PartDetailsResultType.valueOf(source.toUpperCase());
    }

}
