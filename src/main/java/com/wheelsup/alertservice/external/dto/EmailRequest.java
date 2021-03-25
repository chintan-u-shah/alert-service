package com.wheelsup.alertservice.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailRequest {
    private List<String> to;
    private List<Attachment> attachments;
    private String from;
    private List<String> cc;
    private List<String> bcc;
    private String subject;
    private String html;
    private List<String> tags;
    private Date deliveryTime; //null means immediate delivery
    private List<Map<String,String>> recipientVariables;

    /**
     *  Example of batch send email call with recipient variables, "to" list size must match "recipientVariables" list size
     *
     *           "to" : ["wheelsup1@gmail.com", "wheelsup2@gmail.com", "wheelsup3@gmail.com"],
     *           "from" : "wheelsup4@gmail.com",
     *           "subject" : "test",
     *           "html" : "If you wish to unsubscribe, %recipient.first%",
     *           "recipientVariables" : [{"first":"Alice", "id":1}, {"first":"Bob", "id": 2}, {"first":"Claire", "id": 3}]
     *
     */

    @Getter
    @Setter
    @Accessors(chain = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attachment {
        private String name;
        private FileExtension extension;
        private byte[] data;

        @Getter
        @RequiredArgsConstructor
        public enum FileExtension {
            PDF(".pdf"),
            EXCEL(".xls");

            private final String extension;
        }
    }
}
