package com.wheelsup.alertservice.external.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Getter;
import lombok.Setter;


import java.io.IOException;
import java.util.Map;
@Getter
@Setter
public class FileGeneratorRequest {

    private Metadata metadata;
    // @JsonSerialize(using = UserDataSerializer.class)
    private Object data;

    @Getter
    @Setter
    public static class Metadata {
        private String template;
        private String fileName;
        private String fileType;
        private String reportType;
        private Map<String, Object> params;
    }

    /*public  static class UserDataSerializer extends StdSerializer<UserData> {

        public UserDataSerializer(Class<UserData> t) {
            super(t);
        }
        public UserDataSerializer() {
            this(null);
        }


        @Override
        public void serialize(
                UserData userData, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("userName", userData.getUserName());
            jsonGenerator.writeNumberField("userId", userData.getUserId());
            jsonGenerator.writeEndObject();

        }
    }*/

}
