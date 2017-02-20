package org.zrutytools.spec.cjson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * JSON parser that creates concrete syntax tree: every parsed element has positional elements (start/end line and char position)
 *
 * this is implemented by using the jackson {@link JsonParser} and observing the {@link JsonLocation} at each parse point.
 *
 */
public class ConcreteParser {

	private JsonParser jp;

	public ConcreteParser(String src) throws JsonParseException, IOException {
		this(new ByteArrayInputStream((src).getBytes("utf-8")));
	}

	public ConcreteParser(InputStream src) throws JsonParseException, IOException {

		 JsonFactory jsonFactory = new JsonFactory();
		 this.jp = jsonFactory.createParser(src);

	}

	public SourceLocation parse() throws IOException {
		JsonToken token = jp.nextToken();
		return parseToken(token);

	}

	private SourceLocation parseToken(JsonToken token) throws IOException {
		JsonLocation loc = jp.getCurrentLocation();
		// the current location is the location *after* recognizing the token
		// therefore the "current" columnNr is one *past* the token
		// and the start of the token is determined by going back "len" chars
		int len = jp.getTextLength();
		int startLine = loc.getLineNr();
		int startChar = loc.getColumnNr()  - len ;
		int endLine = loc.getLineNr();
		int endChar = loc.getColumnNr()-1;
		switch(token){
		case START_ARRAY:
			return parseArray();
		case START_OBJECT:
			return parseObject();
		case VALUE_NULL:
			return new ConcreteValue<>(startLine, startChar, endLine, endChar, null);
		case VALUE_TRUE:
			return new ConcreteValue<>(startLine, startChar, endLine, endChar, Boolean.TRUE);
		case VALUE_FALSE:
			return new ConcreteValue<>(startLine, startChar, endLine, endChar, Boolean.FALSE);
		case VALUE_NUMBER_FLOAT:
			return new ConcreteValue<>(startLine, startChar, endLine, endChar, jp.getDoubleValue());
		case VALUE_NUMBER_INT:
			return new ConcreteValue<>(startLine, startChar, endLine, endChar, jp.getLongValue());
		case VALUE_STRING:
		    // for a string, everything is different: the parser returns the VALUE_STRING after reading the starting quote
			return new ConcreteValue<>(startLine, loc.getColumnNr()-1, endLine, loc.getColumnNr()+len, jp.getText());
		default:
			throw new IllegalArgumentException("unable to parse token of type " + token + " at " + loc);
		}
	}

	private ConcreteObject parseObject() throws IOException {
		JsonLocation startLoc = jp.getCurrentLocation();

		Map<ConcreteValue<?>, SourceLocation> map = new HashMap<>();
		List<String> keys = new ArrayList<>();


		JsonToken token = jp.nextToken();
		while(token != JsonToken.END_OBJECT){
			if(token != JsonToken.FIELD_NAME){
				throw new IllegalStateException("expected FIELD_NAME or END_OBJECT, got " + token);
			}
			String fieldName = jp.getText();
			keys.add(fieldName);
			JsonLocation loc = jp.getCurrentLocation();

			ConcreteValue<String> key = new ConcreteValue<>(loc.getLineNr(), loc.getColumnNr(), loc.getLineNr(), loc.getColumnNr() + fieldName.length(), fieldName);
			SourceLocation  value = parse();
			map.put(key, value);
			token = jp.nextToken();
		}

		JsonLocation endLoc = jp.getCurrentLocation();

		return new ConcreteObject(startLoc.getLineNr(), startLoc.getColumnNr()-1, endLoc.getLineNr(), endLoc.getColumnNr()-1, keys, map);
	}

	private ConcreteList parseArray() throws IOException {
		JsonLocation startLoc = jp.getCurrentLocation();

		List<SourceLocation> values = new ArrayList<>();

		JsonToken token = jp.nextToken();
		while(token != JsonToken.END_ARRAY){
			values.add(parseToken(token));
			token = jp.nextToken();
		}

		JsonLocation endLoc = jp.getCurrentLocation();

		return new ConcreteList(startLoc.getLineNr(), startLoc.getColumnNr()-1, endLoc.getLineNr(), endLoc.getColumnNr()-1, values);
	}
}
