package org.sobotics.sedeapi.aws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sobotics.sedeapi.client.SEDEClient;
import org.sobotics.sedeapi.client.StackExchangeClient;
import org.sobotics.sedeapi.security.JwtApiKeyUtil;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * The handler (lambda)
 * @author Petter Friberg
 *
 */
public class SEDEQueryHandler implements RequestHandler<Request, QueryResponse> {


	@Override
	public QueryResponse handleRequest(Request request, Context context) {

		//Get parameters
		String site = request.getParams().get("site");
		long idQuery;
		try {
			idQuery = Long.parseLong(request.getParams().get("idquery"));
		} catch (NumberFormatException e) {
			throw new LambdaException("idquery is not nummeric", e);
		}

		// Login to SE
		Map<String, String> cookies = loginSE(request);

		
		//Convert the query map to a list
		List<String> parameters = new ArrayList<>();
		request.getQuery().entrySet().stream().forEach(e -> {
			parameters.add(e.getKey());
			parameters.add(e.getValue());
		});
		
		// Execute the query
		SEDEClient sede = new SEDEClient(cookies);
		try {
			String csv = sede.getCSV(site, idQuery, (String[]) parameters.toArray(new String[parameters.size()]));
			return new QueryResponse(csv);
		} catch (IOException e) {
			throw new LambdaException("Error getting query result", e);
		}
	}

	public Map<String, String> loginSE(Request request) {
		Object token = request.getHeaders().get("bearer");
		if (token == null) {
			throw new LambdaException("Authorization token not provided");
		}

		JwtApiKeyUtil jwt = new JwtApiKeyUtil(System.getenv("token_secret"));
		String email = jwt.getEmailFromToken(token.toString());
		String pwd = jwt.getPwdFromToken(token.toString());
		try (StackExchangeClient client = new StackExchangeClient()) {
			return client.seLogin(email, pwd);
		} catch (IOException e) {
			throw new LambdaException("Could not login to SE", e);
		}
	}

}
