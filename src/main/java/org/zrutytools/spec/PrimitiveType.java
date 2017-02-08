package org.zrutytools.spec;

import java.util.Collections;
import java.util.List;

/**
 * pre-defined classes
 */
public class PrimitiveType implements Type {

  Class<?> expectedClass;

  @Override
  public List<Problem> validate(Object x) {
    if (x != null && expectedClass.isInstance(x)) {
      return Collections.emptyList();
    }
    return Collections.singletonList(new Problem(Problem.Kind.UNEXPECTED_OBJECT_TYPE, x, "epected " + expectedClass.getSimpleName()));
  }

  public PrimitiveType(Class<?> expectedClass) {
    this.expectedClass = expectedClass;
  }

  @Override
  public String getId() {
    return expectedClass.getSimpleName();
  }

}