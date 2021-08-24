package i5.las2peer.services.CitrecHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

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
				title = "las2peer Template Service",
				version = "1.0.0",
				description = "A las2peer Template Service for demonstration purposes.",
				termsOfService = "http://your-terms-of-service-url.com",
				contact = @Contact(
						name = "John Doe",
						url = "provider.com",
						email = "john.doe@provider.com"),
				license = @License(
						name = "your software license name",
						url = "http://your-software-license-url.com")))
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
		try {
			bodyJson = (JSONObject) p.parse(body);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(bodyJson);
		String context = bodyJson.getAsString("rec");
		// get recommendation result from python
		try {
			String line = null;
			StringBuilder sb = new StringBuilder ();
			String res = null;

			URL url = UriBuilder.fromPath("http://localhost:5000/rec")
						.path(context)
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
			bodyJson.put("text", "An error has occurred.");
			return Response.ok().entity(bodyJson.toString()).build();
		}
	}

	/**
	 * Function for more papers
	 *
	 */
	@POST
	@Path("/more")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(
			value = "REPLACE THIS WITH AN APPROPRIATE FUNCTION NAME",
			notes = "REPLACE THIS WITH YOUR NOTES TO THE FUNCTION")
	@ApiResponses(
			value = {@ApiResponse(
					code = HttpURLConnection.HTTP_OK,
					message = "REPLACE THIS WITH YOUR OK MESSAGE")})
	public Response more(String body) {
		JSONParser p = new JSONParser(JSONParser.MODE_PERMISSIVE);
		JSONObject bodyJson = null;
		try {
			bodyJson = (JSONObject) p.parse(body);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(bodyJson);
		bodyJson.put("text", "Button clicked");
		return Response.ok().entity(bodyJson.toString()).build();

	}
}
