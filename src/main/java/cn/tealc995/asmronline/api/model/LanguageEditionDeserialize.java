package cn.tealc995.asmronline.api.model;

import cn.tealc995.asmronline.api.model.LanguageEdition;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: Asmr-Online
 * @description: 过于离谱，这LanguageEdition居然还有小伎俩，偶尔传个map类型过来。小样，你以为穿个马甲我就解析不了你了。
 * @author: Leck
 * @create: 2023-08-13 10:47
 */
public class LanguageEditionDeserialize extends JsonDeserializer<List<LanguageEdition>> {
    @Override
    public List<LanguageEdition> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        List<LanguageEdition> list;
        try {
            list=jsonParser.readValueAs(new TypeReference<List<LanguageEdition>>(){});
        }catch (JsonProcessingException e){
            Map<String, LanguageEdition> map = jsonParser.readValueAs(new TypeReference<Map<String, LanguageEdition>>() {
            });
            list=map.values().stream().toList();
        }
        return list;
    }
}