package org.zrutytools.spec;

import java.util.List;

/**
 * tests an object against one or more rules. returns null or a Problem
 */
@FunctionalInterface
public interface Validator {

  List<Problem> validate(Object ob);

}
