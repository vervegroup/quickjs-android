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

import androidx.annotation.Nullable;

import java.lang.reflect.Type;

class StandardTypeAdapters {

  static final TypeAdapter.Factory FACTORY = new TypeAdapter.Factory() {

    @Nullable
    @Override
    public TypeAdapter<?> create(QuickJS quickJS, Type type) {
      if (type == void.class) return VOID_TYPE_ADAPTER;
      if (type == boolean.class) return BOOLEAN_TYPE_ADAPTER;
      if (type == byte.class) return BYTE_TYPE_ADAPTER;
      if (type == char.class) return CHARACTER_TYPE_ADAPTER;
      if (type == short.class) return SHORT_TYPE_ADAPTER;
      if (type == int.class) return INTEGER_TYPE_ADAPTER;
      if (type == long.class) return LONG_TYPE_ADAPTER;
      if (type == float.class) return FLOAT_TYPE_ADAPTER;
      if (type == double.class) return DOUBLE_TYPE_ADAPTER;
      if (type == Void.class) return VOID_TYPE_ADAPTER;
      if (type == Boolean.class) return BOOLEAN_TYPE_ADAPTER.nullable();
      if (type == Byte.class) return BYTE_TYPE_ADAPTER.nullable();
      if (type == Character.class) return CHARACTER_TYPE_ADAPTER.nullable();
      if (type == Short.class) return SHORT_TYPE_ADAPTER.nullable();
      if (type == Integer.class) return INTEGER_TYPE_ADAPTER.nullable();
      if (type == Long.class) return LONG_TYPE_ADAPTER.nullable();
      if (type == Float.class) return FLOAT_TYPE_ADAPTER.nullable();
      if (type == Double.class) return DOUBLE_TYPE_ADAPTER.nullable();
      if (type == String.class) return STRING_TYPE_ADAPTER.nullable();
      return null;
    }
  };

  private static final TypeAdapter<Void> VOID_TYPE_ADAPTER = new TypeAdapter<Void>() {
    @Override
    public JSValue toJSValue(JSContext context, Void value) {
      return context.createJSNull();
    }

    @Override
    public Void fromJSValue(JSContext context, JSValue value) {
      if (value instanceof JSNull || value instanceof JSUndefined) return null;
      throw new JSDataException("excepted: JSNull or JSUndefined, actual: " + value.getClass().getSimpleName());
    }
  };

  private static final TypeAdapter<Boolean> BOOLEAN_TYPE_ADAPTER = new TypeAdapter<Boolean>() {
    @Override
    public JSValue toJSValue(JSContext context, Boolean value) {
      return context.createJSBoolean(value);
    }

    @Override
    public Boolean fromJSValue(JSContext context, JSValue value) {
      return value.cast(JSBoolean.class).getBoolean();
    }
  };

  private static final TypeAdapter<Byte> BYTE_TYPE_ADAPTER = new TypeAdapter<Byte>() {
    @Override
    public JSValue toJSValue(JSContext context, Byte value) {
      return context.createJSNumber(value);
    }

    @Override
    public Byte fromJSValue(JSContext context, JSValue value) {
      return value.cast(JSNumber.class).getByte();
    }
  };

  private static final TypeAdapter<Character> CHARACTER_TYPE_ADAPTER = new TypeAdapter<Character>() {
    @Override
    public JSValue toJSValue(JSContext context, Character value) {
      return context.createJSString(value.toString());
    }

    @Override
    public Character fromJSValue(JSContext context, JSValue value) {
      String str = value.cast(JSString.class).getString();
      if (str.length() != 1) {
        throw new JSDataException("Can't treat \"" + str + "\" as char");
      }
      return str.charAt(0);
    }
  };

  private static final TypeAdapter<Short> SHORT_TYPE_ADAPTER = new TypeAdapter<Short>() {
    @Override
    public JSValue toJSValue(JSContext context, Short value) {
      return context.createJSNumber(value);
    }

    @Override
    public Short fromJSValue(JSContext context, JSValue value) {
      return value.cast(JSNumber.class).getShort();
    }
  };

  private static final TypeAdapter<Integer> INTEGER_TYPE_ADAPTER = new TypeAdapter<Integer>() {
    @Override
    public JSValue toJSValue(JSContext context, Integer value) {
      return context.createJSNumber(value);
    }

    @Override
    public Integer fromJSValue(JSContext context, JSValue value) {
      return value.cast(JSNumber.class).getInt();
    }
  };

  private static final TypeAdapter<Long> LONG_TYPE_ADAPTER = new TypeAdapter<Long>() {
    @Override
    public JSValue toJSValue(JSContext context, Long value) {
      return context.createJSNumber(value);
    }

    @Override
    public Long fromJSValue(JSContext context, JSValue value) {
      return value.cast(JSNumber.class).getLong();
    }
  };

  private static final TypeAdapter<Float> FLOAT_TYPE_ADAPTER = new TypeAdapter<Float>() {
    @Override
    public JSValue toJSValue(JSContext context, Float value) {
      return context.createJSNumber(value);
    }

    @Override
    public Float fromJSValue(JSContext context, JSValue value) {
      return value.cast(JSNumber.class).getFloat();
    }
  };

  private static final TypeAdapter<Double> DOUBLE_TYPE_ADAPTER = new TypeAdapter<Double>() {
    @Override
    public JSValue toJSValue(JSContext context, Double value) {
      return context.createJSNumber(value);
    }

    @Override
    public Double fromJSValue(JSContext context, JSValue value) {
      return value.cast(JSNumber.class).getDouble();
    }
  };

  private static final TypeAdapter<String> STRING_TYPE_ADAPTER = new TypeAdapter<String>() {
    @Override
    public JSValue toJSValue(JSContext context, String value) {
      return context.createJSString(value);
    }

    @Override
    public String fromJSValue(JSContext context, JSValue value) {
      return value.cast(JSString.class).getString();
    }
  };
}
