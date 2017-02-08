package org.zrutytools.spec;

import java.util.List;
import java.util.Map;

public interface Type {

  /**
   * test that x is an obejct his this type
   *
   * @param x
   * @return list of problems, or empty list
   */
  List<Problem> validate(Object x);

  /**
   * type id
   * @return
   */
  String getId();

  static Type STRING = new PrimitiveType(String.class);
  static Type NUMBER = new PrimitiveType(Number.class);
  static Type BOOLEAN = new PrimitiveType(Boolean.class);
  static Type OBJECT = new PrimitiveType(Map.class);
  static Type LIST = new PrimitiveType(List.class);

}
