package sr3u.showvisitskeeper.dto.smart.annotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DbListSerializer extends JsonSerializer<DbList<?>> {

    @Override
    public void serialize(DbList<?> dbList, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        serializerProvider.defaultSerializeValue(dbList.get(), jsonGenerator);
    }

}
