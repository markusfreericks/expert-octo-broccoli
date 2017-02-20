package org.zrutytools.spec.types;

import java.util.ArrayList;
import java.util.List;

import org.zrutytools.spec.ObjectContext;
import org.zrutytools.spec.Problem;

public class ListType implements Type {

  private Type nodeType;
  private boolean nullAllowed = false;
  private int minSize = 0;

  public ListType(Type nodeType) {
    this.nodeType = nodeType;
  }

  @Override
  public String getId() {
    return "List<" + nodeType.getId() + ">";
  }

  public void setNodeType(Type nodeType) {
    this.nodeType = nodeType;
  }

  public void setMinSize(int minSize) {
    this.minSize = minSize;
  }

  @Override
  public List<Problem> validate(ObjectContext ctx) {
    List<Problem> rs = new ArrayList<>();
    Object x = ctx.getObject();

    if (x == null) {
      if (!nullAllowed) {
        rs.add(new Problem(Problem.Kind.NULL_PROPERTY, ctx, getId()));
      }
      return rs;
    }

    if(x instanceof List) {
      List<?> list = (List) x;
      if(list.size() < minSize) {
        if(minSize == 1) {
          rs.add(new Problem(Problem.Kind.EMPTY_LIST, ctx, getId() + " must not be empty"));
        } else {
          rs.add(new Problem(Problem.Kind.EMPTY_LIST, ctx, getId() + " must have at least " + minSize + " elements (has " + list.size() + ")"));
        }
      }
      int n=0;
      for(Object ob : list) {
        rs.addAll(nodeType.validate(ctx.element(n++, nodeType, ob)));
      }
    } else {
      rs.add(new Problem(Problem.Kind.UNEXPECTED_OBJECT_TYPE, ctx, getId() + " expected List"));
    }

    return rs;
  }



}
