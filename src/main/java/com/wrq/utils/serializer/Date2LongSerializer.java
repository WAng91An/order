package com.wrq.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * Created by wangqian on 2019/2/6.
 */
public class Date2LongSerializer extends JsonSerializer<Date> {

    /**
     * 把 Date 格式转换成 Long 类型,默认 1549112586000 -> 转化后 1549112586
     * @param date
     * @param jsonGenerator
     * @param serializers
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException, JsonProcessingException {
        jsonGenerator.writeNumber( date.getTime() / 1000 );
    }
}
