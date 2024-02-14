/**
 * Copyright © 2010-2020 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jsonschema2pojo.integration;

import static org.hamcrest.Matchers.*;
import static org.jsonschema2pojo.integration.util.CodeGenerationHelper.config;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.jsonschema2pojo.integration.util.Jsonschema2PojoRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RequiredJackson2IT {

    @ClassRule public static  Jsonschema2PojoRule classSchemaRule = new Jsonschema2PojoRule();

    private static Class<?> classWithRequired;

    @BeforeClass
    public static void generateClasses() throws IOException, ClassNotFoundException {

        ClassLoader resultsClassLoader = classSchemaRule.generateAndCompile("/schema/required/required.json", "com.example",
            config("annotationStyle", "jackson2"));
        
        classWithRequired = resultsClassLoader.loadClass("com.example.Required");
    }

    @Test
    public void requiredFieldHasJacksonIncludeAlwaysAnnotation() throws NoSuchFieldException, SecurityException {
        Field field = classWithRequired.getDeclaredField("requiredProperty");
        assertThat(field.getAnnotation(JsonInclude.class).value(), is(JsonInclude.Include.ALWAYS));

    }


    @Test
    public void nonRequiredFieldHasNoJacksonIncludeAlwaysAnnotation() throws NoSuchFieldException, SecurityException {
        Field field = classWithRequired.getDeclaredField("nonRequiredProperty");
        assertThat(field.getAnnotation(JsonInclude.class), is(nullValue()));

    }

    @Test
    public void notRequiredIsTheDefault() throws NoSuchFieldException, SecurityException {
        Field field = classWithRequired.getDeclaredField("defaultNotRequiredProperty");
        assertThat(field.getAnnotation(JsonInclude.class), is(nullValue()));

    }

}
