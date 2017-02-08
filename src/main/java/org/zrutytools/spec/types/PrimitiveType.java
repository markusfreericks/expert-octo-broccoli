package org.zrutytools.spec.types;

import java.util.Collections;
import java.util.List;

import org.zrutytools.spec.ObjectContext;
import org.zrutytools.spec.Problem;
import org.zrutytools.spec.Problem.Kind;

/**
 * pre-defined classes
 */
public class PrimitiveType implements Type {

  Class<?> expectedClass;

  @Override
  public List<Problem> validate(ObjectContext ctx) {
    Object x = ctx.getObject();
    if (x != null && expectedClass.isInstance(x)) {
      return Collections.emptyList();
    }
    return Collections.singletonList(new Problem(Problem.Kind.UNEXPECTED_OBJECT_TYPE, ctx, "epected " + expectedClass.getSimpleName()));
  }

  public PrimitiveType(Class<?> expectedClass) {
    this.expectedClass = expectedClass;
  }

  @Override
  public String getId() {
    return expectedClass.getSimpleName();
  }

}