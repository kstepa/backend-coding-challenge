package com.engagetech.expenses;

import static spark.Spark.*;

import java.util.Collections;

import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.engagetech.common.APIException;
import com.engagetech.common.DB;
import com.engagetech.common.SvcConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import spark.ResponseTransformer;

public class Application implements ResponseTransformer {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	private final SvcConfig cfg;
	private final Gson gson;
	private final DB db;
	private final ExpenseService expenseSvc;
	
	public Application(SvcConfig cfg) {
		this.cfg = cfg;
		GsonBuilder gb = new GsonBuilder();
		gb.setDateFormat("MM/dd/yyyy");
		if (cfg.debug()) gb.setPrettyPrinting();
		gson = gb.create();
		
		this.db = new DB(this.cfg);
		this.db.migrate();
		this.expenseSvc = new ExpenseService(this.db, new RateService());
	}

	public void up() {
		port(cfg.httpPort());
		enableCORS("*", "GET,PUT,POST,DELETE,OPTIONS", 
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
		setupExceptions();
		setupRoutes();
	}

	public void down() {
		stop();
	}
	
	/**
	 * The following mapping of business and runtime exceptions to HTTP status codes
	 * is a convention that may change
	 */
	private void setupExceptions() {
		exception(APIException.class, (e, req, resp) -> { // expected exception
			logger.info("API exception processing request " + req.pathInfo(), e);
			resp.status(((APIException) e).getCode());
			resp.body(errMsg(e.getMessage()));
		});

		exception(JsonSyntaxException.class, (e, req, resp) -> {
			logger.error("Unexpected request body, can'not parse JSON: " + req.pathInfo() + "\n" + req.body(), e);
			resp.status(400);
			resp.body(errMsg("Unexpected field format: " + e.getMessage()));
		});
		
		exception(Exception.class, (e, req, resp) -> {
			logger.error("Unexpected exception processing request " + req.pathInfo(), e);
			// unexpected exception
			resp.status(500);
			resp.body(errMsg(e.getMessage()));
		});
	}
	
	private String errMsg(String raw) {
		return gson.toJson(Collections.singletonMap("err", raw));
	}

	private void setupRoutes() {
		get("/expenses", 
				(req, resp) -> expenseSvc.all(), this);
		post("/expenses", 
				(req, resp) -> {
					expenseSvc.insert(gson.fromJson(req.body(), Expense.class));
					return null;
				}, 
				this);
	}
	
	// https://sparktutorials.github.io/2016/05/01/cors.html
	private void enableCORS(final String origin, final String methods, final String headers) {
	    options("/*", (request, response) -> {

	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }

	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }

	        return "OK";
	    });

	    before((request, response) -> {
	        response.header("Access-Control-Allow-Origin", origin);
	        response.header("Access-Control-Request-Method", methods);
	        response.header("Access-Control-Allow-Headers", headers);
	        response.type("application/json");
	    });
	}

	
	@Override
	public String render(Object model) throws Exception {
		if (model == null) return "";
		return gson.toJson(model);
	}

	public static void main(String[] args) {
		SvcConfig cfg = ConfigFactory.create(SvcConfig.class, System.getenv(), System.getProperties());
		Application app = new Application(cfg);
		app.up();
	}
}
