package org.zrutytools.spec;

import java.util.ArrayList;
import java.util.List;

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
  public List<Problem> validate(Object x) {
    List<Problem> rs = new ArrayList<>();

    if (x == null) {
      if (!nullAllowed) {
        rs.add(new Problem(Problem.Kind.NULL_PROPERTY, x, getId()));
      }
      return rs;
    }

    if(x instanceof List) {
      List<?> list = (List) x;
      if(list.size() < minSize) {
        if(minSize == 1) {
          rs.add(new Problem(Problem.Kind.EMPTY_LIST, x, getId() + " must not be empty"));
        } else {
          rs.add(new Problem(Problem.Kind.EMPTY_LIST, x, getId() + " must have at least " + minSize + " elements (has " + list.size() + ")"));
        }
      }
      for(Object ob : list) {
        rs.addAll(nodeType.validate(ob));
      }
    } else {
      rs.add(new Problem(Problem.Kind.UNEXPECTED_OBJECT_TYPE, x, getId()));
    }

    return rs;
  }



}
