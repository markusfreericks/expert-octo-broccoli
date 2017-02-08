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
 */
public class Parser {

  private EmptySyntax syntax;

  public Parser(EmptySyntax syntax) {
    this.syntax = syntax;
  }

  ParsedSpec parse(Reader src) throws IOException {
    BufferedReader lines = new BufferedReader(src);
    ParsedSpec result = new ParsedSpec();
    syntax.setSpec(result);

    String line;
    int n = 1;
    while ((line = lines.readLine()) != null) {
      parseLine(line, n++);
    }

    return result;
  }

  private void parseLine(String line, int lineNo) {

    List<Method> matchingMethods = new ArrayList<>();

    String cleaned = cleanLine(line);

    for (Method m : syntax.getClass().getMethods()) {
      Syntax syntax = m.getAnnotation(Syntax.class);
      if (syntax != null) {
        if (matches(syntax.value(), cleaned)) {
          matchingMethods.add(m);

          addRule(syntax.value(), cleaned, m);
        }
      }
    }
    // every line must match exactly one method
    if (matchingMethods.isEmpty()) {
      String anno = "@Syntax(\"" + cleaned.replaceAll("[\"]", "\\\"") + "\")";
      throw new IllegalArgumentException("line " + lineNo + " does not match any method. Missing annotation:\n" + anno);
    }
    if (matchingMethods.size() > 1) {
      throw new IllegalArgumentException("line " + lineNo + " matches " + matchingMethods.size() + " methods: " + matchingMethods);
    }
  }

  /**
   * entferne tabs und mehrfach-leerzeichen; entferne leerzeichen an den enden
   *
   * @param line
   *          non-null String
   * @return cleaned up String
   */
  private String cleanLine(String line) {
    String r = line.replaceAll("\\s+", " ");
    // in-line kommentar (at the end)
    r = r.replaceAll(" \\(.*\\)$", "");
    // space in front
    r = r.replaceAll("^ ", "");
    // space in theend
    r = r.replaceAll(" $", "");
    // dot at the end.
    r = r.replaceAll("\\.$", "");
    return r;
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
      m.invoke(syntax, params);
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
   * unquote
   * @param id
   * @return
   */
  String unq(String id) {
    return id.replace("\"", "");
  }

}
