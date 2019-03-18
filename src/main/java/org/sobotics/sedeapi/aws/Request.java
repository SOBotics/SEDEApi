package org.sobotics.sedeapi.aws;

import java.util.HashMap;
import java.util.Map;

/**
 * The request object, parse by template mapping
 * 
 * {
 * "headers": {
 * #foreach($header in $input.params().header.keySet())
 * "$header": "$util.escapeJavaScript($input.params().header.get($header))"
 * #if($foreach.hasNext),#end
 * 
 * #end
 * },
 * "method": "$context.httpMethod",
 * "params": {
 * #foreach($param in $input.params().path.keySet())
 * "$param": "$util.escapeJavaScript($input.params().path.get($param))"
 * #if($foreach.hasNext),#end
 * 
 * #end
 * },
 * "query": {
 * #foreach($queryParam in $input.params().querystring.keySet())
 * "$queryParam":
 * "$util.escapeJavaScript($input.params().querystring.get($queryParam))"
 * #if($foreach.hasNext),#end
 * 
 * #end
 * }
 * }
 * 
 * @author Petter Friberg
 *
 */
public class Request {

	private Map<String, String> headers;
	private String method;
	private Map<String, String> params;
	private Map<String, String> query;

	public Map<String, String> getHeaders() {
		if (headers == null) {
			headers = new HashMap<>();
		}
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getParams() {
		if (params == null) {
			params = new HashMap<>();
		}
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getQuery() {
		if (query == null) {
			query = new HashMap<>();
		}
		return query;
	}

	public void setQuery(Map<String, String> query) {
		this.query = query;
	}

	@Override
	public String toString() {
		String retVal = "Header: " + getHeaders().toString() + ", ";
		retVal += "Method: " + getMethod() + ", ";
		retVal += "Query: " + getQuery().toString() + ", ";
		retVal += "Params: " + getParams().toString();
		return retVal;
	}
}
