package i5.las2peer.services.CitrecHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import i5.las2peer.restMapper.RESTService;
import i5.las2peer.restMapper.annotations.ServicePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import i5.las2peer.api.Context; 
import i5.las2peer.api.logging.MonitoringEvent;
import i5.las2peer.connectors.webConnector.client.ClientResponse;
import i5.las2peer.connectors.webConnector.client.MiniClient;

// TODO Describe your own service
/**
 * las2peer-Template-Service
 * 
 * This is a template for a very basic las2peer service that uses the las2peer WebConnector for RESTful access to it.
 * 
 * Note: If you plan on using Swagger you should adapt the information below in the SwaggerDefinition annotation to suit
 * your project. If you do not intend to provide a Swagger documentation of your service API, the entire Api and
 * SwaggerDefinition annotation should be removed.
 * 
 */
// TODO Adjust the following configuration
@Api
@SwaggerDefinition(
		info = @Info(
				title = "las2peer CitRec Handler Service",
				version = "1.0.0",
				description = "A las2peer Service that handler the messages comes from citbot.",
				termsOfService = "",
				contact = @Contact(
						name = "Chenyang Li",
						url = "provider.com",
						email = "chenyang.li@rwth-aachen.de"),
				license = @License(
						name = "",
						url = "")))
@ServicePath("/CitrecHandler")
public class CitrecHandlerService extends RESTService {

	/**
	 * Function for citation recommendation
	 *
	 */
	@POST
	@Path("/rec")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
			value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
			notes = "REPLACE THIS WITH YOUR NOTES TO THE FUNCTION")
	@ApiResponses(
			value = {@ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "REPLACE THIS WITH YOUR OK MESSAGE")})
	public Response rec(String body) {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		JSONObject bodyJson = null;
		JSONObject payloadJson = new JSONObject();
		JSONObject monitorEvent51 = new JSONObject();
		final long start = System.currentTimeMillis();
		try {
			bodyJson = (JSONObject) p.parse(body);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		payloadJson.put("context", bodyJson.getAsString("rec"));
		payloadJson.put("channel", bodyJson.getAsString("channel"));
		// get recommendation result from python
		try {
			String line = null;
			StringBuilder sb = new StringBuilder ();
			String res = null;

			URL url = UriBuilder.fromPath("http://localhost:5000/rec")
						.path(URLEncoder.encode(payloadJson.toString(), "UTF-8").replace("+","%20"))
						.build()
						.toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			BufferedReader rd  = new BufferedReader( new InputStreamReader(connection.getInputStream(), "UTF-8"));

			while ((line = rd.readLine()) != null ) {
				sb.append(line);
			}
			res = sb.toString();
			monitorEvent51.put("Task", "Recommendation Search");
			monitorEvent51.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_51,monitorEvent51.toString());
			return Response.ok().entity(res).build();
		} catch (IOException e) {
			e.printStackTrace();
			bodyJson.put("text", "An error has occurred.");
			monitorEvent51.put("Task", "Recommendation Search Error");
			monitorEvent51.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_51,monitorEvent51.toString());
			return Response.ok().entity(bodyJson.toString()).build();
		}
	}

	/**
	 * Function for citation recommendation
	 *
	 */
	@POST
	@Path("/recRC")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
			value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
			notes = "REPLACE THIS WITH YOUR NOTES TO THE FUNCTION")
	@ApiResponses(
			value = {@ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "REPLACE THIS WITH YOUR OK MESSAGE")})
	public Response recRC(String body) {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		JSONObject bodyJson = null;
		JSONObject payloadJson = new JSONObject();
		JSONObject monitorEvent51 = new JSONObject();
		final long start = System.currentTimeMillis();
		try {
			bodyJson = (JSONObject) p.parse(body);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		payloadJson.put("context", bodyJson.getAsString("rec"));
		payloadJson.put("channel", bodyJson.getAsString("channel"));
		// get recommendation result from python
		try {
			String line = null;
			StringBuilder sb = new StringBuilder ();
			String res = null;

			URL url = UriBuilder.fromPath("http://localhost:5000/rec")
						.path(URLEncoder.encode(payloadJson.toString(), "UTF-8").replace("+","%20"))
						.build()
						.toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			BufferedReader rd  = new BufferedReader( new InputStreamReader(connection.getInputStream(), "UTF-8"));

			while ((line = rd.readLine()) != null ) {
				sb.append(line);
			}
			res = sb.toString();
			JSONObject resj = null;
			try {
				resj =(JSONObject) p.parse(res);
				if(resj.get("blocks")!=null){
				res = "";
				JSONArray blocks = null;
				if (resj.get("blocks") instanceof JSONArray){
					blocks = (JSONArray) resj.get("blocks");
				}else if (resj.get("blocks") instanceof String){
					String blocksString = (String) resj.get("blocks");
					blocks = (JSONArray) p.parse(blocksString);
				}
				for (int i = 0, size = blocks.size(); i < size; i++)
    			{
					JSONObject section = (JSONObject) blocks.get(i);
					if(section.getAsString("type").equals("section")){
						JSONObject sectionTextObject = (JSONObject) section.get("text");
						String t = sectionTextObject.getAsString("text");
						res += t +"\n";
					}
				}
				resj = new JSONObject();
				resj.put("text", res);
				res = resj.toJSONString();
			}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			monitorEvent51.put("Task", "Recommendation Search");
			monitorEvent51.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_51,monitorEvent51.toString());
			
			return Response.ok().entity(res).build();
		} catch (IOException e) {
			e.printStackTrace();
			bodyJson.put("text", "An error has occurred.");
			monitorEvent51.put("Task", "Recommendation Search Error");
			monitorEvent51.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_51,monitorEvent51.toString());
			return Response.ok().entity(bodyJson.toString()).build();
		}
	}




	/**
	 * Function for actions
	 *
	 */
	@POST
	@Path("/actions")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
			value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
			notes = "REPLACE THIS WITH YOUR NOTES TO THE FUNCTION")
	@ApiResponses(
			value = {@ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "REPLACE THIS WITH YOUR OK MESSAGE")})
	public Response actions(String body) {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		JSONObject bodyJson = null;
		try {
			bodyJson = (JSONObject) p.parse(body);
			// Delete useless key-values
			bodyJson.remove("intent");
			bodyJson.remove("entities");
			bodyJson.remove("botName");
			bodyJson.remove("contextOn");
			bodyJson.remove("email");
			// Extract useful information from the input string
			if(bodyJson.getAsString("msg").startsWith("[{")){
				JSONArray jsonArray = (JSONArray) p.parse(bodyJson.getAsString("msg"));
				Iterator<Object> attributeIterator = jsonArray.iterator();
				List<String> valueList = new ArrayList<>();
				while (attributeIterator.hasNext()) {
					valueList.add(((JSONObject) attributeIterator.next()).getAsString("value"));
				}
				bodyJson.put("msg", valueList);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			JSONObject json = null;
			json.put("text", "An error has occurred.");
			return Response.ok().entity(json.toString()).build();
		}
		try {
			String line = null;
			StringBuilder sb = new StringBuilder();
			String res = null;

			URL url = UriBuilder.fromPath("http://localhost:5000/actions")
					.path(URLEncoder.encode(bodyJson.toString(), "UTF-8").replace("+","%20"))
					.build()
					.toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			BufferedReader rd  = new BufferedReader( new InputStreamReader(connection.getInputStream(), "UTF-8"));

			while ((line = rd.readLine()) != null ) {
				sb.append(line);
			}
			res = sb.toString();
			return Response.ok().entity(res).build();
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject json = null;
			json.put("text", "An error has occurred.");
			return Response.ok().entity(json.toString()).build();
		}
	}


	/**
	 * Function for marking list
	 *
	 */
	@POST
	@Path("/lists")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
			value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
			notes = "REPLACE THIS WITH YOUR NOTES TO THE FUNCTION")
	@ApiResponses(
			value = {@ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "REPLACE THIS WITH YOUR OK MESSAGE")})
	public Response lists(String body) {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		JSONObject bodyJson = null;
		JSONObject monitorEvent52 = new JSONObject();
        final long start = System.currentTimeMillis();
		try {
			bodyJson = (JSONObject) p.parse(body);
			// Delete useless key-values
			bodyJson.remove("intent");
			bodyJson.remove("entities");
			bodyJson.remove("botName");
			bodyJson.remove("contextOn");
			bodyJson.remove("email");
			bodyJson.remove("list");
			bodyJson.remove("time");
		} catch (ParseException e) {
			e.printStackTrace();
			JSONObject json = null;
			json.put("text", "An error has occurred.");
			monitorEvent52.put("Task", "Returns list Error");
			monitorEvent52.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_52,monitorEvent52.toString());
			return Response.ok().entity(json.toString()).build();
		}
		try {
			String line = null;
			StringBuilder sb = new StringBuilder();
			String res = null;

			URL url = UriBuilder.fromPath("http://localhost:5000/lists")
					.path(URLEncoder.encode(bodyJson.toString(), "UTF-8").replace("+","%20"))
					.build()
					.toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			BufferedReader rd  = new BufferedReader( new InputStreamReader(connection.getInputStream(), "UTF-8"));

			while ((line = rd.readLine()) != null ) {
				sb.append(line);
			}
			res = sb.toString();
			monitorEvent52.put("Task", "Returns list");
			monitorEvent52.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_52,monitorEvent52.toString());
			return Response.ok().entity(res).build();
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject json = null;
			json.put("text", "An error has occurred.");
			monitorEvent52.put("Task", "Returns list Error");
			monitorEvent52.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_52,monitorEvent52.toString());
			return Response.ok().entity(json.toString()).build();
		}
	}

	/**
	 * Function for keywords searching
	 *
	 */
	@POST
	@Path("/keywords")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
			value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
			notes = "REPLACE THIS WITH YOUR NOTES TO THE FUNCTION")
	@ApiResponses(
			value = {@ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "REPLACE THIS WITH YOUR OK MESSAGE")})
	public Response keywords(String body) {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		JSONObject bodyJson = null;
		JSONObject payloadJson = new JSONObject();
		JSONObject monitorEvent51 = new JSONObject();
		final long start = System.currentTimeMillis();
		try {
			bodyJson = (JSONObject) p.parse(body);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		payloadJson.put("keywords", bodyJson.getAsString("kw"));
		payloadJson.put("channel", bodyJson.getAsString("channel"));
		// get search results from python
		try {
			String line = null;
			StringBuilder sb = new StringBuilder ();
			String res = null;

			URL url = UriBuilder.fromPath("http://localhost:5000/keywords")
					.path(URLEncoder.encode(payloadJson.toString(), "UTF-8").replace("+","%20"))
					.build()
					.toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			BufferedReader rd  = new BufferedReader( new InputStreamReader(connection.getInputStream(), "UTF-8"));

			while ((line = rd.readLine()) != null ) {
				sb.append(line);
			}
			res = sb.toString();
			monitorEvent51.put("Task", "Keyword Search");
			monitorEvent51.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_51,monitorEvent51.toString());
			return Response.ok().entity(res).build();
		} catch (IOException e) {
			e.printStackTrace();
			bodyJson.put("text", "An error has occurred.");
			monitorEvent51.put("Task", "Keyword Search Error");
			monitorEvent51.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_51,monitorEvent51.toString());
			return Response.ok().entity(bodyJson.toString()).build();
		}
	}


	/**
	 * Function for keywords searching
	 *
	 */
	@POST
	@Path("/keywordsRC")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
			value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
			notes = "REPLACE THIS WITH YOUR NOTES TO THE FUNCTION")
	@ApiResponses(
			value = {@ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "REPLACE THIS WITH YOUR OK MESSAGE")})
	public Response keywordsRC(String body) {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		JSONObject bodyJson = null;
		JSONObject payloadJson = new JSONObject();
		JSONObject monitorEvent51 = new JSONObject();
		final long start = System.currentTimeMillis();
		try {
			bodyJson = (JSONObject) p.parse(body);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		payloadJson.put("keywords", bodyJson.getAsString("kw"));
		payloadJson.put("channel", bodyJson.getAsString("channel"));
		String botName = bodyJson.getAsString("botName");
		String sbfURL = bodyJson.getAsString("sbfURL");
		// get search results from python
		try {
			String line = null;
			StringBuilder sb = new StringBuilder ();
			String res = null;

			URL url = UriBuilder.fromPath("http://localhost:5000/keywords")
					.path(URLEncoder.encode(payloadJson.toString(), "UTF-8").replace("+","%20"))
					.build()
					.toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			BufferedReader rd  = new BufferedReader( new InputStreamReader(connection.getInputStream(), "UTF-8"));

			while ((line = rd.readLine()) != null ) {
				sb.append(line);
			}
			res = sb.toString();
			JSONObject resj = null;
			try {
				resj =(JSONObject) p.parse(res);
				if(resj.get("blocks")!=null){
				res = "";
				JSONArray blocks = null;
				if (resj.get("blocks") instanceof JSONArray){
					blocks = (JSONArray) resj.get("blocks");
				}else if (resj.get("blocks") instanceof String){
					String blocksString = (String) resj.get("blocks");
					blocks = (JSONArray) p.parse(blocksString);
				}
				for (int i = 0, size = blocks.size(); i < size; i++)
    			{
					JSONObject section = (JSONObject) blocks.get(i);
					if(section.getAsString("type").equals("section")){
						JSONObject sectionTextObject = (JSONObject) section.get("text");
						String t = sectionTextObject.getAsString("text");
						res += t +"\n";
					}
				}
				resj = new JSONObject();
				botWebhook(res, bodyJson.getAsString("channel"), sbfURL + "/" + botName);
				resj.put("text", "");
				res = resj.toJSONString();
			}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			monitorEvent51.put("Task", "Keyword Search");
			monitorEvent51.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_51,monitorEvent51.toString());
			return Response.ok().entity(res).build();
		} catch (IOException e) {
			e.printStackTrace();
			bodyJson.put("text", "An error has occurred.");
			monitorEvent51.put("Task", "Keyword Search Error");
			monitorEvent51.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_51,monitorEvent51.toString());
			return Response.ok().entity(bodyJson.toString()).build();
		}
	}


	
	private void botWebhook(String message, String channel, String botURL){
		MiniClient client = new MiniClient();
		client.setConnectorEndpoint(botURL);
		JSONObject information = new JSONObject();
		information.put("message", message);
		information.put("channel", channel);
		information.put("event", "chat_message");
		try {
			ClientResponse result = client.sendRequest("POST", "",
					information.toJSONString());
			System.out.println(result.toString());
			System.out.println(result.getHttpCode());
			System.out.println(result.getRawResponse());
			System.out.println(result.getResponse());
			// JSONObject answer = (JSONObject) parser.parse(result.getResponse());
			// System.out.println(answer);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("pepe");
		}
	}

	/**
	 * Function for greeting message
	 *
	 */
	@POST
	@Path("/greeting")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
			value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
			notes = "REPLACE THIS WITH YOUR NOTES TO THE FUNCTION")
	@ApiResponses(
			value = {@ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "REPLACE THIS WITH YOUR OK MESSAGE")})
	public Response greeting() {
		JSONObject monitorEvent50 = new JSONObject();
		final long start = System.currentTimeMillis();
		monitorEvent50.put("Task", "Greeting");
		try {
			String line = null;
			StringBuilder sb = new StringBuilder();
			String res = null;

			URL url = UriBuilder.fromPath("http://localhost:5000/greeting").build().toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			BufferedReader rd  = new BufferedReader( new InputStreamReader(connection.getInputStream(), "UTF-8"));

			while ((line = rd.readLine()) != null ) {
				sb.append(line);
			}
			res = sb.toString();
			monitorEvent50.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_50,monitorEvent50.toString());
			return Response.ok().entity(res).build();
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject json = null;
			json.put("text", "An error has occurred.");
			monitorEvent50.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_50,monitorEvent50.toString());
			return Response.ok().entity(json.toString()).build();
		}
	}

	/**
	 * Function for greeting message
	 *
	 */
	@POST
	@Path("/greetingRC")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
			value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
			notes = "REPLACE THIS WITH YOUR NOTES TO THE FUNCTION")
	@ApiResponses(
			value = {@ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "REPLACE THIS WITH YOUR OK MESSAGE")})
	public Response greetingRC() {
		JSONObject monitorEvent50 = new JSONObject();
		final long start = System.currentTimeMillis();
		monitorEvent50.put("Task", "Greeting");
		try {
			String line = null;
			StringBuilder sb = new StringBuilder();
			String res = null;

			URL url = UriBuilder.fromPath("http://localhost:5000/greeting").build().toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			BufferedReader rd  = new BufferedReader( new InputStreamReader(connection.getInputStream(), "UTF-8"));

			while ((line = rd.readLine()) != null ) {
				sb.append(line);
			}
			res = sb.toString();
			JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
			JSONObject resj = null;
			try {
				resj =(JSONObject) p.parse(res);
				if(resj.get("blocks")!=null){
				res = "";
				JSONArray blocks = (JSONArray) resj.get("blocks");
				for (int i = 0, size = blocks.size(); i < size; i++)
    			{
					JSONObject section = (JSONObject) blocks.get(i);
					if(section.getAsString("type").equals("section")){
						JSONObject sectionTextObject = (JSONObject) section.get("text");
						String t = sectionTextObject.getAsString("text");
						res += t +"\n";
					}
				}
				resj = new JSONObject();
				resj.put("text", res);
				res = resj.toJSONString();
			}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			monitorEvent50.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_50,monitorEvent50.toString());
			return Response.ok().entity(res).build();
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject json = null;
			json.put("text", "An error has occurred.");
			monitorEvent50.put("Process time", System.currentTimeMillis() - start);
            Context.get().monitorEvent(MonitoringEvent.SERVICE_CUSTOM_MESSAGE_50,monitorEvent50.toString());
			return Response.ok().entity(json.toString()).build();
		}
	}
}
