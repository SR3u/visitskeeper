package sr3u.showvisitskeeper.dto.smart.annotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DbObjectSerializer extends JsonSerializer<DbObject<?>> {

    @Override
    public void serialize(DbObject<?> dbObj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Object value = dbObj.getOptional().orElse(null);
        if (value != null) {
            serializerProvider.defaultSerializeValue(value, jsonGenerator);
        } else {
            serializerProvider.defaultSerializeNull(jsonGenerator);
        }
    }

}
