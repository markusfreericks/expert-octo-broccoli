package org.zrutytools.spec;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FieldTypValidator implements Validator {

  String expectedType;
  private String fieldName;

  public FieldTypValidator(String fieldName, String expectedType) {
    this.fieldName = fieldName;
    this.expectedType = expectedType;
  }

  @Override
  public List<Problem> validate(Object ob) {
    if (ob instanceof Map) {
      Map map = (Map) ob;
      if (!map.containsKey(fieldName)) {
        return Collections.singletonList(new Problem(fieldName, "missing field " + fieldName));
      }
      Object x = map.get(fieldName);
      String xType = Types.getBaseType(x);
      if (! Objects.equals(expectedType, xType)) {
        return Collections.singletonList(new Problem(fieldName, "field " + fieldName + " contains a " + xType + " instead of the expected " + expectedType));
      }
      return Collections.emptyList();
    } else {
      return Collections.singletonList(new Problem("", "this is not an object: " + ob));
    }
  }

}
