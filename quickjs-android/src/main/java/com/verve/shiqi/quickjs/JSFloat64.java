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

final class JSFloat64 extends JSNumber {

  private final double value;

  JSFloat64(long pointer, JSContext jsContext, double value) {
    super(pointer, jsContext);
    this.value = value;
  }

  private String wrongNumberMessage(String javaType, double value) {
    return "Can't treat " + value + " as " + javaType;
  }

  @Override
  public byte getByte() {
    double value = this.value;
    byte result = (byte) value;
    if (result != value) {
      throw new JSDataException(wrongNumberMessage("byte", value));
    }
    return result;
  }

  @Override
  public short getShort() {
    double value = this.value;
    short result = (short) value;
    if (result != value) {
      throw new JSDataException(wrongNumberMessage("short", value));
    }
    return result;
  }

  @Override
  public int getInt() {
    double value = this.value;
    int result = (int) value;
    if (result != value) {
      throw new JSDataException(wrongNumberMessage("int", value));
    }
    return result;
  }

  @Override
  public long getLong() {
    double value = this.value;
    long result = (long) value;
    if (result != value) {
      throw new JSDataException(wrongNumberMessage("long", value));
    }
    return result;
  }

  @Override
  public float getFloat() {
    return (float) value;
  }

  @Override
  public double getDouble() {
    return value;
  }
}
