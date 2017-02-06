package org.zrutytools.spec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * the parser reads a list of rules. each rule is matched against a number of syntactic patterns, each of which constructs a list of Classifications and/or Rules.
 *
 * a Classification defines a name for a json object
 * a Rule defines some property a json object must have
 *
 * @author mfx
 */
public class Parser {

  private ParsedSpec result;

  ParsedSpec parse(Reader src) throws IOException {
    BufferedReader lines = new BufferedReader(src);
    this.result = new ParsedSpec();

    String line;
    int n = 0;
    while ((line = lines.readLine()) != null) {
      parseLine(line, n++);
    }

    return result;
  }

  private void parseLine(String line, int lineNo) {

    List<Method> matchingMethods = new ArrayList<>();

    for (Method m : getClass().getDeclaredMethods()) {
      Syntax syntax = m.getAnnotation(Syntax.class);
      if (syntax != null) {
        System.out.println("testing syntax " + syntax);
        if (matches(syntax.value(), line)) {
          matchingMethods.add(m);

          addRule(syntax.value(), line, m);
        }
      } else {
        System.out.println("no syntax for " + m);
      }
    }
    // every line must match exactly one method
    if (matchingMethods.isEmpty()) {
      throw new IllegalArgumentException("line " + lineNo + " does not match any method");
    }
    if (matchingMethods.size() > 1) {
      throw new IllegalArgumentException("line " + lineNo + " matches " + matchingMethods.size() + " methods: " + matchingMethods);
    }
  }

  private void addRule(String value, String line, Method m) {
    Matcher matcher = Pattern.compile(value).matcher(line);
    if (!matcher.matches()) {
      // matches() muss einmal aufgerufen werden
      throw new IllegalArgumentException("unable to parse argument line `" + line + "` against syntax `" + value + "`");
    }
    int groupCount = matcher.groupCount();
    Class<?>[] parameterTypes = m.getParameterTypes();
    Object[] params = new String[groupCount];
    if (parameterTypes.length != params.length) {
      throw new IllegalArgumentException("unable to parse " + line + " against method " + m + ": expected " + parameterTypes.length + " parameters, got " + params.length);
    }

    for (int g = 0; g < params.length; g++) {
      try {
        // gruppen werden ab 1 gezählt; 0 ist der gesamt-input
        String text = matcher.group(g + 1);
        params[g] = convertParam(parameterTypes[g], text);

      } catch (Exception ex) {
        throw new IllegalArgumentException("unable to parse argument " + g + " in line `" + line + "` against syntax `" + value + "`", ex);
      }
    }
    try {
      m.invoke(this, params);
    } catch (Exception ex) {
      throw new IllegalArgumentException("unable to parse line `" + line + "` against syntax `" + value + "`", ex);
    }
  }

  private Object convertParam(Class<?> targetClass, String text) {
    if (targetClass == String.class) {
      return text;
    }
    throw new IllegalArgumentException("don't know how to convert " + text + " to " + targetClass.getSimpleName());
  }

  private boolean matches(String syntax, String line) {
    return line.matches(syntax);
  }

  /**
   * ignore comments
   *
   * @return
   */
  @Syntax("#.*")
  void parseComment() {
    // NOP
  }

  /**
   * ignore empty lines
   *
   * @return
   */
  @Syntax(" *")
  void parseEmpty() {
    // NOP
  }

  /**
   *
   * @param name
   * @return a type definition and a name definition
   */
  @Syntax("the document contains an object called (.*)")
  void announceDocumentName(String name) {
    result.addValidatorForType(Types.TYPE_DOCUMENT, new BaseTypValidator(Types.TYPE_OBJECT));
  }

  @Syntax("the (.+) MUST contain a field (.+) that contains a list of (.+) elements.")
  void announceFieldElement(String objectType, String fieldName, String subObjectName) {
    result.addValidatorForType(objectType, new FieldTypValidator(fieldName, subObjectName));
  }

  @Syntax("a (.+) MUST contain a field (.+) that contains a unique (.+) string.")
  void announceUniqueElement(String objectType, String fieldName, String idName) {
    result.addValidatorForType(objectType, new FieldTypValidator(fieldName, Types.TYPE_STRING));
  }

}
