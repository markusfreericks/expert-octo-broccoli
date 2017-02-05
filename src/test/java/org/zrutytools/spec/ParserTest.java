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

	private Validator v;

	@Test
	public void testEmpty() throws IOException {
		
		Parser p = new Parser();
		ParsedSpec r = p.parse(new StringReader(""));
		assertNotNull(r);
		
	}

	@Before
	public void setup() throws UnsupportedEncodingException, IOException{
		
		InputStream is = getClass().getResourceAsStream("spec-1.txt");
		assertNotNull(is);
		
		Parser p = new Parser();
		ParsedSpec r = p.parse(new InputStreamReader(is, "utf-8"));
		assertNotNull(r);

		this.v = r.createValidator();

	}
	
	@Test
	public void testThatTheRootTypeIsTested() {
		
		ValidationResult vr;
		
		// falscher typ: null
		vr = v.validate(null);
		assertTrue(vr.hasProblems());
		
		// falscher typ: number
		vr = v.validate(42);
		assertTrue(vr.hasProblems());
		
		// falscher typ: liste
		vr = v.validate(new ArrayList<>());
		assertTrue(vr.hasProblems());
		
	}
	
	@Test
	public void testContentOfRootObject() {

		Map<String, Object> doc = new HashMap<>();
		List<Object> nodes = new ArrayList<>();
		doc.put("list", nodes);
		
		ValidationResult vr;

		// richtiger typ: object. fehlendes feld: "list"
		vr = v.validate(new HashMap<>());
		assertTrue(vr.hasProblems());
		
		// formal erst einmal richtig
		vr = v.validate(doc);
		assertFalse(vr.hasProblems());
		
	}

}
