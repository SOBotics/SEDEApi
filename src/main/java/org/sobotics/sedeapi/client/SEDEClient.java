package org.sobotics.sedeapi.client;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection.Response;

/**
 * The SEDE client, user should already be logged in 
 * @author Petter Friberg
 *
 */
public class SEDEClient {

	public static final String BASE_URL_QUERY = "http://data.stackexchange.com/%1$s/csv/%2$s";

	private HttpClient client = new HttpClient();

	private Map<String, String> cookies;

	public SEDEClient(Map<String, String> cookies) {
		super();
		this.cookies = cookies;
	}

	public String getCSV(String site, long idQuery, String... parameters) throws IOException {
		Response r = client.get(String.format(BASE_URL_QUERY, site, idQuery), cookies, parameters);
		if (r.statusCode()!=200){
			throw new IOException("Incorrect status code from SEDE query, check parameters. Status code" + r.statusCode());
		}
		//check content type
		String ct = r.header("Content-Type");
		if (ct==null || !ct.contains("text/csv")){
			throw new IOException("Content-Type returned from query is not correct: " + ct);
		}
		return r.body();
	}

}
