package compgc01.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import compgc01.model.User;

public class AutenticacaoServico {
	private String token;
	
	public AutenticacaoServico() {
		super();
	}

	public User autenticar(String email, String password) throws ClientProtocolException, IOException, ParseException{
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://zcinema-auth-microservice.herokuapp.com/auth");
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/json");
		JSONObject json = new JSONObject();
		
		json.put("email", email);
		json.put("password", password);
		
		StringEntity entity = new StringEntity(json.toJSONString(), ContentType.APPLICATION_JSON);
		
	    httpPost.setEntity(entity);

	    CloseableHttpResponse response = client.execute(httpPost);
	    
	    if (response.getStatusLine().getStatusCode() != 200) {
	    	throw new IOException();
	    }
	    
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

		JSONParser parser = new JSONParser();
		JSONObject jsonRes = (JSONObject) parser.parse(responseBody);
		JSONObject user = (JSONObject) jsonRes.get("user");
		setToken((String) jsonRes.get("token"));
		
	    return new User((String) user.get("first_name"), (String) user.get("last_name"), 
	    		"", password, email, (String) user.get("avatar"), (String) user.get("profile"),
	    		(Long) user.get("id"));
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
