package com.wheelsup.alertservice.external.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class HtmlFileGeneratorRequest {
    private Metadata metadata;
    private Map<String, Object> data;


    @Getter
    @Setter
    @ToString
    public static class Metadata {
        private String template;
    }

}