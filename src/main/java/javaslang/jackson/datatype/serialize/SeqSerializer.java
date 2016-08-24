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
package javaslang.jackson.datatype.serialize;

import com.fasterxml.jackson.databind.JavaType;
import javaslang.collection.Seq;

import java.io.IOException;

class SeqSerializer extends ValueSerializer<Seq<?>> {

    private static final long serialVersionUID = 1L;

    SeqSerializer(JavaType type) {
        super(type);
    }

    @Override
    Object toJavaObj(Seq<?> value) throws IOException {
        return value.toJavaList();
    }

}