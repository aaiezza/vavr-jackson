/**
 * Copyright 2015 The Javaslang Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javaslang.jackson.datatype.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import javaslang.control.Either;

import java.io.IOException;

class EitherDeserializer extends ValueDeserializer<Either<?, ?>> {

    private static final long serialVersionUID = 1L;

    private final JavaType javaType;
    private JsonDeserializer<?> stringDeserializer;

    EitherDeserializer(JavaType valueType) {
        super(valueType, 2);
        this.javaType = valueType;
    }

    @Override
    public Either<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        boolean right = false;
        Object value = null;
        int cnt = 0;
        while (p.nextToken() != JsonToken.END_ARRAY) {
            cnt++;
            switch (cnt) {
                case 1:
                    String def = (String) stringDeserializer.deserialize(p, ctxt);
                    if ("right".equals(def)) {
                        right = true;
                    } else if ("left".equals(def)) {
                        right = false;
                    } else {
                        throw ctxt.mappingException(javaType.getRawClass());
                    }
                    break;
                case 2:
                    if (right) {
                        value = deserializer(1).deserialize(p, ctxt);
                    } else {
                        value = deserializer(0).deserialize(p, ctxt);
                    }
                    break;
            }
        }
        if (cnt != 2) {
            throw ctxt.mappingException(javaType.getRawClass());
        }
        if (right) {
            return Either.right(value);
        } else {
            return Either.left(value);
        }
    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        super.resolve(ctxt);
        stringDeserializer = ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), null);
    }
}
