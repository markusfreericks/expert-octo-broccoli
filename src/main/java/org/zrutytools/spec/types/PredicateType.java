package org.zrutytools.spec.types;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.zrutytools.spec.ObjectContext;
import org.zrutytools.spec.Problem;
import org.zrutytools.spec.Problem.Kind;

public class PredicateType implements Type {

  String id;
  private Predicate<Object> pred;

  public PredicateType(String id, Predicate<Object> pred) {
    this.id = id;
    this.pred = pred;
  }

  @Override
  public List<Problem> validate(ObjectContext ctx) {
    Object x = ctx.getObject();
    if (pred != null && pred.test(x)) {
      return Collections.emptyList();
    } else {
      return Collections.singletonList(new Problem(Problem.Kind.PREDICATE_FAIL, ctx, id));
    }
  }

  @Override
  public String getId() {
    return id;
  }

  public static final PredicateType NONEMPTY = new PredicateType("nonempty",
      x -> {
        if (x instanceof String) {
          return !((String) x).isEmpty();
        }
        if (x instanceof Collection) {
          return !((Collection) x).isEmpty();
        }
        return false;
      });

}
