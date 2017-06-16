package com.engagetech.expenses;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class EndToEndTest {
	static Application app;

	@BeforeClass
	public static void setup() {
		TestUtil tu = TestUtil.get();
		tu.cleanTables();
		app = new Application(tu.cfg);
		app.up();
	}

	@AfterClass
	public static void tearDown() {
		app.down();
	}

	@Test
	public void smoke() throws Exception {
		JSONObject exp = new JSONObject();
		exp.put("date", "01/01/2017");
		exp.put("reason", "New Year");
		exp.put("amount", 100.01);
		assertEquals(200, Unirest.post(url("/expenses")).body(exp).asString().getStatus());
		exp.put("amount", -100.01);
		assertEquals(422, Unirest.post(url("/expenses")).body(exp).asString().getStatus());

		HttpResponse<JsonNode> resp = Unirest.get(url("/expenses")).asJson();
		assertEquals(200, resp.getStatus());
		assertEquals(1, resp.getBody().getArray().length());
	}

	private static String url(String rel) {
		return "http://localhost:4567" + rel;
	}
}
