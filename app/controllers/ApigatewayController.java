package controllers;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

public class ApigatewayController extends Controller {

	
	 
	@Inject WSClient ws;
	
	private String serviceDispatch(String requestedMethod,String requestedLocation) throws InterruptedException, ExecutionException{
		String response="";
		
		switch (requestedMethod) {
		case "GET":
			CompletionStage<JsonNode> jsonPromise = ws.url(requestedLocation).get()
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
		
		return response;
	}
	
	public Result auth(String requestedLocation) throws InterruptedException, ExecutionException{
		
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
		
		//Hard code the user request for testing
		requestedLocation = "https://gturnquist-quoters.cfapps.io/api/random";
		
		String requestedMethod = "GET";
		String response ="apigateway response...";
		
		List<? extends Config> configList = ConfigFactory.load("endpoints.conf").getConfigList("api.gateway.endpoints");
		int configListSize = configList.size();

		boolean found=false;
		int i=0;
		while(!found && i<configListSize)
		{
			if (requestedLocation.toLowerCase().trim().equals(configList.get(i).getString("location").toLowerCase().trim()))
			{
				found=true;
			}
			i++;
		}
		
		if(found){
			
			response += serviceDispatch(requestedMethod, requestedLocation);
		}
		else
		{
			response += "access denied";
		}
		
		return ok(response);
	}
}
