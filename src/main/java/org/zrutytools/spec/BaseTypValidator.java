package org.zrutytools.spec;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BaseTypValidator implements Validator {

  String expectedType;

  public BaseTypValidator(String expectedType) {
    this.expectedType = expectedType;
  }

  @Override
  public List<Problem> validate(Object ob) {
    String foundType = Types.getBaseType(ob);
    if(Objects.equals(expectedType, foundType)) {
      return Collections.emptyList();
    } else {
      return Collections.singletonList(new Problem("", "expected " + expectedType + " found " + foundType));
    }
  }

}
