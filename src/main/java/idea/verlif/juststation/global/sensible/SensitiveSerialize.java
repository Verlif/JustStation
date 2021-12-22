package idea.verlif.juststation.global.sensible;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

/**
 * 序列化脱敏处理器
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/13 14:54
 */
public class SensitiveSerialize extends JsonSerializer<Object> implements ContextualSerializer {

    private Sensitive sensitive;

    public SensitiveSerialize() {
    }

    public SensitiveSerialize(Sensitive sensitive) {
        this.sensitive = sensitive;
    }

    @Override
    public void serialize(Object s, JsonGenerator generator, SerializerProvider provider) throws IOException {
        switch (sensitive.strategy()) {
            case ALWAYS_VALUE:
                generator.writeObject(sensitive.value());
                break;
            case ALWAYS_NULL:
            default:
                generator.writeNull();
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
        if (sensitive != null) {
            return new SensitiveSerialize(sensitive);
        } else {
            return new DefaultSerialize();
        }
    }

    private static class DefaultSerialize extends JsonSerializer<Object> {

        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeObject(o);
        }
    }
}
