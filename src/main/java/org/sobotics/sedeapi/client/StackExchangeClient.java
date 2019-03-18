package org.sobotics.sedeapi.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * Client used to authenticate with Stack Exchange.
 * 
 * Reduced StackExchangeClient from ChatExchange https://github.com/SOBotics/chatexchange
 * 
 * @author Tunaki & FelixSFD
 */
public class StackExchangeClient implements AutoCloseable {

		
	private HttpClient httpClient;
	private Map<String, String> cookies = new HashMap<>();


	
	/**
	 * true, if the account for a user should automatically be created when logging in to a new site
	 */
	private boolean autoCreateAccount = true;

	/**
	 * Constructs the client 
	 */
	public StackExchangeClient() {
		httpClient = new HttpClient();
	}
	
	/**
	 * Logs in to s given site
	 * @param email The user's e-mail-address
	 * @param password The password
	 * */
	public Map<String,String> seLogin(String email, String password) throws IOException {
		String host = "meta.stackexchange.com";
		
		//The login-form has a hidden field called "fkey" which needs to be sent along with the mail and password
		Response response = httpClient.get("https://"+host+"/users/login", cookies);
		String fkey = response.parse().select("input[name='fkey']").val();
		
		response = httpClient.post("https://"+host+"/users/login", cookies, "email", email, "password", password, "fkey", fkey);
		
		//Create account on that site if necessary
		Element formElement = response.parse().getElementById("logout-user");
		if (formElement != null) {
			if (!this.autoCreateAccount) {
				throw new IllegalStateException("Unable to login to Stack Exchange. The user does not have an account");
			} // if autoCreate
			
			Elements formInputs = formElement.getElementsByTag("input");
			List<String> formData = new ArrayList<>();
			
			for (Element input : formInputs) {
				String key = input.attr("name");
				String value = input.val();
				
				if (key == null || key.isEmpty())
					continue;
				
				formData.add(key);
				formData.add(value);
			} // for formInputs
			
			String[] formDataArray = formData.toArray(new String[formData.size()]);
			
			String formUrl = "https://" + host + formElement.attr("action");
			
			Response formResponse = httpClient.post(formUrl, cookies, formDataArray);
			if (formResponse.parse().getElementsByClass("js-inbox-button").first() == null) {
				throw new IllegalStateException("Unable to create account on " + host + "! Please create the account manually.");
			} // if
		} // if
		
		
		// check if login succeeded
		Response checkResponse = httpClient.get("https://"+host+"/users/current", cookies);
		if (checkResponse.parse().getElementsByClass("js-inbox-button").first() == null) {
			throw new IllegalStateException("Unable to login to Stack Exchange. (Site: " + host + ")");
		} // if
		
		return cookies;
	} // seLogin



	/**
	 * true, if the account for a user should automatically be created when logging in to a new site
	 */
	public boolean getAutoCreateAccount() {
		return autoCreateAccount;
	}

	/**
	 * Controls, if the account for a user should automatically be created when logging in to a new site
	 * @param autoCreateAccount new value
	 */
	public void setAutoCreateAccount(boolean autoCreateAccount) {
		this.autoCreateAccount = autoCreateAccount;
	}
	

	/**
	 * Closes this client by making the logged-in user leave all the chat rooms they joined.
	 * <p>Multiple invocations of this method has no further effect.
	 */
	@Override
	public void close() {
		//do nothing for now, it would be nice to log out
	}

}
