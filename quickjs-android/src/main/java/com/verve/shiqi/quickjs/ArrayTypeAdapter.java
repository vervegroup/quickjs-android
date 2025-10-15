/*
 * Copyright 2019 Hippo Seven
 * Copyright 2023-Present Shiqi Mei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.verve.shiqi.quickjs;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

class ArrayTypeAdapter extends TypeAdapter<Object> {

  public static final Factory FACTORY = (depot, type) -> {
    Type elementType = JavaTypes.arrayComponentType(type);
    if (elementType == null) return null;
    Class<?> elementClass = JavaTypes.getRawType(elementType);
    TypeAdapter<Object> elementAdapter = depot.getAdapter(elementType);
    return new ArrayTypeAdapter(elementClass, elementAdapter).nullable();
  };

  private final Class<?> elementClass;
  private final TypeAdapter<Object> elementAdapter;

  private ArrayTypeAdapter(Class<?> elementClass, TypeAdapter<Object> elementAdapter) {
    this.elementClass = elementClass;
    this.elementAdapter = elementAdapter;
  }

  @Override
  public JSValue toJSValue(JSContext context, Object value) {
    JSArray result = context.createJSArray();
    for (int i = 0, length = Array.getLength(value); i < length; i++) {
      result.setProperty(i, elementAdapter.toJSValue(context, Array.get(value, i)));
    }
    return result;
  }

  @Override
  public Object fromJSValue(JSContext context, JSValue value) {
    JSArray array = value.cast(JSArray.class);
    int length = array.getLength();
    Object result = Array.newInstance(elementClass, length);
    for (int i = 0; i < length; i++) {
      Array.set(result, i, elementAdapter.fromJSValue(context, array.getProperty(i)));
    }
    return result;
  }
}
