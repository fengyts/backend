package com.backend.deserialers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author DELL
 */
@Slf4j
public class CustomJsonPercentDeserializer extends JsonDeserializer<Double> {

    private static final String PERCENT_SYMBEL = "%";

    @Override
    public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String param = null;
        try {
            param = p.getText();
            if (StringUtils.isBlank(param)) {
                return null;
            }
            if(!param.endsWith(PERCENT_SYMBEL)){
                return Double.valueOf(param);
            }
            NumberFormat nf = NumberFormat.getPercentInstance();
            final Number parse = nf.parse(param);
            return parse.doubleValue();
        } catch (ParseException e) {
            log.info("receive param converter exception, param: {} , exception: {}", param, e);
        }
        return null;
    }

}
