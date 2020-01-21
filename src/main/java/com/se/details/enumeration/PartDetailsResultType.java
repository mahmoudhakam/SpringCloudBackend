package com.se.details.enumeration;

import com.se.details.util.PDMicroserviceConstants;

import java.util.HashSet;
import java.util.Set;

public enum PartDetailsResultType {

    STATISTICS("statistics", getPartDetailsStatisticsFields()),
    RESPONSE("response", getPartDetailsResponseFields()),
    FULL("full", getPartDetailsFullFields());

    private String name;
    private Set<String> fields;

    PartDetailsResultType(String name, Set<String> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public Set<String> getFields() {
        return fields;
    }

    private static Set<String> getPartDetailsStatisticsFields() {
        Set<String> statisticsFields = getPartDetailsDefaultFields();
        statisticsFields.add(PDMicroserviceConstants.PartDetailsEndPointResponseColumns.STATISTICS);
        return statisticsFields;
    }

    private static Set<String> getPartDetailsResponseFields() {
        Set<String> responseFields = getPartDetailsDefaultFields();
        responseFields.add(PDMicroserviceConstants.PartDetailsEndPointResponseColumns.PART_FEATURES);
        return responseFields;
    }

    private static Set<String> getPartDetailsFullFields() {
        Set<String> fullFields = getPartDetailsDefaultFields();
        fullFields.add(PDMicroserviceConstants.PartDetailsEndPointResponseColumns.STATISTICS);
        fullFields.add(PDMicroserviceConstants.PartDetailsEndPointResponseColumns.PART_FEATURES);
        return fullFields;
    }


    private static Set<String> getPartDetailsDefaultFields() {
        Set<String> defaultFields = new HashSet<>();
        defaultFields.add(PDMicroserviceConstants.PartDetailsEndPointResponseColumns.STATUS);
        defaultFields.add(PDMicroserviceConstants.PartDetailsEndPointResponseColumns.SERVICE_TIME);
        return defaultFields;
    }
}
