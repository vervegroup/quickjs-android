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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * Represents a java method or a java static method.
 */
public final class JavaMethod {

  @Nullable
  public static JavaMethod create(Type type, Method rawMethod) {
    Class<?> rawType = JavaTypes.getRawType(type);
    Type returnType = JavaTypes.resolve(type, rawType, rawMethod.getGenericReturnType());
    // It's not resolved
    if (returnType instanceof TypeVariable) return null;

    String name = rawMethod.getName();

    Type[] originParameterTypes = rawMethod.getGenericParameterTypes();
    Type[] parameterTypes = new Type[originParameterTypes.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      parameterTypes[i] = JavaTypes.resolve(type, rawType, originParameterTypes[i]);
      // It's not resolved
      if (parameterTypes[i] instanceof TypeVariable) return null;
    }

    return new JavaMethod(returnType, name, parameterTypes);
  }

  final Type returnType;
  final String name;
  final Type[] parameterTypes;

  public JavaMethod(Type returnType, String name, Type[] parameterTypes) {
    this.returnType = canonicalize(returnType);
    this.name = name;
    this.parameterTypes = new Type[parameterTypes.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      this.parameterTypes[i] = canonicalize(parameterTypes[i]);
    }
  }

  private static Type canonicalize(Type type) {
    return JavaTypes.removeSubtypeWildcard(JavaTypes.canonicalize(type));
  }

  private static String getTypeSignature(Type type) {
    // Array
    if (type instanceof GenericArrayType) {
      return "[" + getTypeSignature(((GenericArrayType) type).getGenericComponentType());
    }

    // Primitive
    if (type instanceof Class && ((Class<?>) type).isPrimitive()) {
      if (type == void.class) return "V";
      if (type == boolean.class) return "Z";
      if (type == byte.class) return "B";
      if (type == char.class) return "C";
      if (type == short.class) return "S";
      if (type == int.class) return "I";
      if (type == long.class) return "J";
      if (type == float.class) return "F";
      if (type == double.class) return "D";
    }

    // Class
    Class<?> clazz = JavaTypes.getRawType(type);
    String name = clazz.getName();
    StringBuilder sb = new StringBuilder(name.length() + 2);
    sb.append("L");
    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);
      sb.append(c == '.' ? '/' : c);
    }
    sb.append(";");
    return sb.toString();
  }

  // For jni
  String getSignature() {
    StringBuilder sb = new StringBuilder();

    sb.append("(");
    for (Type parameterType : parameterTypes) {
      sb.append(getTypeSignature(parameterType));
    }
    sb.append(")");
    sb.append(getTypeSignature(returnType));

    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(returnType);
    sb.append(" ");
    sb.append(name);
    sb.append("(");
    for (int i = 0; i < parameterTypes.length; i++) {
      if (i != 0) sb.append(", ");
      sb.append(parameterTypes[i]);
    }
    sb.append(")");
    return sb.toString();
  }

  @Override
  public int hashCode() {
    int result = 0;
    result = 31 * result + returnType.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + Arrays.hashCode(parameterTypes);
    return result;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (!(obj instanceof JavaMethod)) return false;
    JavaMethod other = (JavaMethod) obj;
    return returnType.equals(other.returnType)
        && name.equals(other.name)
        && Arrays.equals(parameterTypes, other.parameterTypes);
  }
}
