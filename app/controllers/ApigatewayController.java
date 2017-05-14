package controllers;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.Logger;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

public class ApigatewayController extends Controller {

	@Inject WSClient ws;
	
	public Result auth(String service) throws InterruptedException, ExecutionException{
		
		/* TODO:
		 * 1. get user requested location,methods
		 * 2. Check if user requested URL location and methods exist in endpoint.conf file(permission control)
		 * C,R,U,D = POST,GET,PUT/PATCH,DELETE
		 * 3. oAuth authentication check
		 * 4. blacklist check
		 * 5. rate limiting check
		 * 6. process the request according to location and methods.
		 * 7. Circuit breaker check
		 * 8. Dispatch the request
		 */
		
		/*
		 * apigateway - initial request: GET http://localhost:9000/api/userservice
		 * apigateway - Request: GET https://gturnquist-quoters.cfapps.io/api/random
		 * userservice - process display saved json detail
		   apigateway - Response 200
		 * 
		 */
		
		
		
		String response="";
		String requestLocation=getRequestLocation();
		
		if(Strings.isNullOrEmpty(requestLocation))
		{
			response = "Access Denied...";
		}
		else
		{
			switch (request().method()) {
			case "GET":
				CompletionStage<JsonNode> jsonPromise = ws.url(requestLocation).get()
		        .thenApply(WSResponse::asJson);
				response += jsonPromise.toCompletableFuture().get().toString();
				
				break;
			case "POST":
				break;
			case "DELETE":
				break;
			case "PATCH":
				break;
			case "PUT":
				break;
			default:
				response +="unknown endpoint method";
				break;
			}
		
		}
		
		Logger.info("Response " + response);
		return ok(response);
	}
	
	private String getRequestLocation()
	{
		String requestPath=request().path().toLowerCase().trim();
		String requestMethod=request().method().toLowerCase().trim();
		String requestLocation="";
		
		List<? extends Config> configList = ConfigFactory.load("endpoints.conf").getConfigList("api.gateway.endpoints");
		int configListSize = configList.size();

		boolean found=false;
		int i=0;
		while(!found && i<configListSize)
		{
			boolean pathExist = false;
			boolean methodExist = false;
			pathExist = requestPath.equals(configList.get(i).getString("path").toLowerCase().trim());
			methodExist = requestMethod.equals(configList.get(i).getString("method").toLowerCase().trim());
			
			if (pathExist && methodExist)
			{
				requestLocation=configList.get(i).getString("location").trim();
				found=true;
			}
			i++;
		}
		Logger.info("Request:" + requestMethod + " " + requestLocation);
		
		return requestLocation;
	}

}
