package org.zrutytools.spec;

public class Spec1Syntax extends EmptySyntax {
  /**
   *
   * @param name
   * @return a type definition and a name definition
   */
  @Syntax("the document is object of type (.*)")
  public void announceDocumentName(String name) {
    // introduceType removes any quotes around the name
    NodeType nodeType = introduceType(name);
    setRootType(nodeType);
  }

  @Syntax("an? (.+) MUST contain a field (.+) that contains a list of (.+) elements")
  public void announceFieldElement(String objectType, String fieldName, String subObjectName) {
    NodeType t = lookupType(objectType);
    NodeType s = introduceType(subObjectName);
    ListType listOfT = new ListType(s);
    addMandatoryField(fieldName, t, listOfT);
  }

  /*
   * all-spaces should also be considered empty
   */
  static final PredicateType NOT_EMPTY_STRING= new PredicateType("nonnull", x -> !((String)x).isEmpty());

  @Syntax("an? (.+) MUST contain a nonempty string field (.+)")
  public void announceUniqueElement(String objectType, String fieldName) {
    NodeType t = lookupType(objectType);
    t.addMandatoryField(unq(fieldName),
          PrimitiveType.STRING,
          NOT_EMPTY_STRING);
  }

}
