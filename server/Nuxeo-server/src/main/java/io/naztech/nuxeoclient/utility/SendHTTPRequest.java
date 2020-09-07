package io.naztech.nuxeoclient.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2020-03-11
 **/
public class SendHTTPRequest {
	private static Logger log = LoggerFactory.getLogger(SendHTTPRequest.class);
	private static final String POST = "POST";

	public SendHTTPRequest() {
	}

	public static String abbyData(String targetURL) {

		String resp = null;

		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

			HttpGet request = new HttpGet(targetURL);
			request.addHeader("content-type", "application/x-www-form-urlencoded");
			org.apache.http.HttpResponse response = httpClient.execute(request);

			log.info("Response from ABBY server: {}", response.getStatusLine());

			resp = response.toString();

		} catch (Exception ex) {
			log.error("Error from remote server: {}", ex.getMessage());
		}

		return resp;
	}

	public static String executePost(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;

		try {
			// Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(POST);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
