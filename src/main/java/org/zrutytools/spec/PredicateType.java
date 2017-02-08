package org.zrutytools.spec;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.zrutytools.spec.Problem.Kind;

public class PredicateType implements Type {

  String id;
  private Predicate<Object> pred;

  public PredicateType(String id, Predicate<Object> pred) {
    this.id = id;
    this.pred = pred;
  }

  @Override
  public List<Problem> validate(Object x) {
    if(pred != null && pred.test(x)) {
      return Collections.emptyList();
    } else {
      return Collections.singletonList(new Problem(Kind.PREDICATE_FAIL, x, id));
    }
  }

  @Override
  public String getId() {
    return id;
  }

}
