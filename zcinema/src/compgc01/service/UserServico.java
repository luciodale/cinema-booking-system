package compgc01.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import compgc01.model.User;

public class UserServico {

	public UserServico() {
		super();
	}

	public void atualizarInformacoes(User usuario, String token) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPatch httpPatch = new HttpPatch("https://zcinema-server-test.herokuapp.com/users/" + usuario.getId());
		httpPatch.setHeader("Content-Type", "application/json");
		httpPatch.setHeader("Accept", "application/json");
		httpPatch.setHeader("Authorization", "Bearer "+token);

		StringEntity entity = new StringEntity(usuario.toJSONString(), ContentType.APPLICATION_JSON);

		httpPatch.setEntity(entity);

		CloseableHttpResponse response = client.execute(httpPatch);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException();
		}
	}
}
