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
	
	public Result auth(String action) throws InterruptedException, ExecutionException{
		
		/* TODO:
		 * 1. get user requested location,methods
		 * 2. Check if user requested URL location and methods exist in endpoint.conf file(permission control)
		 * C,R,U,D = POST,GET,PUT/PATCH,DELETE
		 * 3. oAuth authentication check
		 * 4. blacklist check
		 * 5. rate limiting check
		 * 6. process the request according to location and methods.
		 */
		
		
		String requestedLocation = "https://gturnquist-quoters.cfapps.io/api/random";
		String requestedMethod = "GET";
		String response ="apigateway response...";
		
		List<? extends Config> configList = ConfigFactory.load("endpoints.conf").getConfigList("api.gateway.endpoints");
		int configListSize = configList.size();

		boolean found=false;
		for (int i =0;i<configListSize;i++)
		{
			if (requestedLocation.toLowerCase().trim().equals(configList.get(i).getString("location").toLowerCase().trim()))
			{
				found=true;
			}
		}
		
		if(found){
			if(requestedMethod.equals("GET")){
				CompletionStage<JsonNode> jsonPromise = ws.url(requestedLocation).get()
				        .thenApply(WSResponse::asJson);
				response += jsonPromise.toCompletableFuture().get().toString();
			}
			else if(requestedMethod.equals("POST")){
				
			}
			else if(requestedMethod.equals("DELETE")){
				
			}
			
		}
		else
		{
			response += "access denied";
		}
		
		return ok(response);
	}
}
