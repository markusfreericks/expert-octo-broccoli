package org.zrutytools.spec;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

  private ParsedSpec spec;

  /**
   * the empty spec contains no constraints, and therefore allows any object
   */
  @Test
  public void testThatEmptySpecAllowsEverything() throws IOException {

    Parser p = new Parser();
    ParsedSpec emptySpec = p.parse(new StringReader(""));

    assertFalse(emptySpec.validate(null).hasProblems());
    assertFalse(emptySpec.validate(1).hasProblems());
    assertFalse(emptySpec.validate("test").hasProblems());
    assertFalse(emptySpec.validate(true).hasProblems());
    assertFalse(emptySpec.validate(new ArrayList()).hasProblems());
    assertFalse(emptySpec.validate(new HashMap()).hasProblems());

  }

  @Before
  public void setup() throws UnsupportedEncodingException, IOException {

    InputStream is = getClass().getResourceAsStream("spec-1.txt");
    assertNotNull(is);

    Parser p = new Parser();
    this.spec = p.parse(new InputStreamReader(is, "utf-8"));
    assertNotNull(spec);
  }

  @Test
  public void testThatTheRootTypeIsTested() {

    ValidationResult vr;

    // falscher typ: null
    vr = spec.validate(null);
    assertTrue(vr.hasProblems());

    // falscher typ: number
    vr = spec.validate(42);
    assertTrue(vr.hasProblems());

    // falscher typ: liste
    vr = spec.validate(new ArrayList<>());
    assertTrue(vr.hasProblems());

  }

  @Test
  public void testContentOfRootObject() {

    Map<String, Object> doc = new HashMap<>();
    List<Object> nodes = new ArrayList<>();
    doc.put("list", nodes);

    ValidationResult vr;

    // richtiger typ: object. fehlendes feld: "list"
    vr = spec.validate(new HashMap<>());
    assertTrue(vr.hasProblems());

    // formal erst einmal richtig
    vr = spec.validate(doc);
    assertFalse(vr.hasProblems());

  }

}
