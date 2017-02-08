package org.zrutytools.spec.types;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.zrutytools.spec.ObjectContext;
import org.zrutytools.spec.Problem;
import org.zrutytools.spec.Problem.Kind;

/**
 * this class tests that an attribute of the current object has the same value as a (differnt) attribute in a parent object
 */
public class AttributeAgreementType implements Type {
  String attr;
  NodeType parentType;
  private String parentAttr;

  public AttributeAgreementType(String attr, String parentAttr, NodeType parentType) {
    this.attr = attr;
    this.parentAttr = parentAttr;
    this.parentType = parentType;
  }

  @Override
  public List<Problem> validate(ObjectContext ctx) {

    Map map = (Map) ctx.getObject();
    String v = (String) map.get(attr);
    if(v == null) {
      return Collections.singletonList(new Problem(Kind.MISSING_PROPERTY, ctx, attr));
    }
    ObjectContext parent = ctx.lookupParent(parentType);
    if(parent == null) {
      return Collections.singletonList(new Problem(Kind.PREDICATE_FAIL, ctx, "no parent of type " + parentType.getId()));
    }
    Map pMap= (Map) parent.getObject();
    String pV = (String) pMap.get(parentAttr);
    if(pV == null) {
      return Collections.singletonList(new Problem(Kind.MISSING_PROPERTY, parent, attr));
    }
    if(Objects.equals(v, pV)) {
      return Collections.emptyList();
    } else {
      return Collections.singletonList(new Problem(Kind.PREDICATE_FAIL, ctx, "parent has " + parentAttr + "=" + pV + " does not agree with local " + attr + "=" + v));
    }
  }

  @Override
  public String getId() {
    return "agreement on " + attr;
  }
}