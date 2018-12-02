package com.anthem.jiraTool;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class Test {

	public static void main(String[] args) {

		String https_url = "http://localhost:8080/rest/api/latest/issue/JIR-1";
		URL url;
		try {
			url = new URL(https_url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			String userName="Amolak2797";
			String password="jira123";
			String credentials=userName+":"+password;
			String basicAuth="Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));
			System.out.println("Basic Authentication : "+basicAuth);
			con.setRequestProperty("Authorization", basicAuth);
			String dataRetrieved="";
			if (con != null) {
				System.out.println("Connection established successfully");
				try {

					System.out.println("****** Content of the URL ********");
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

					String input;

					while ((input = br.readLine()) != null) {
						System.out.println(input);
						dataRetrieved+=input;
					}
					br.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(dataRetrieved);
			Object obj=JSONValue.parse(dataRetrieved);
			JSONObject jsonObject=(JSONObject)obj;
			JSONObject fieldJsonObject=(JSONObject)jsonObject.get("fields");
			JSONObject statusJsonObject=(JSONObject)fieldJsonObject.get("status");
			String status=(String)statusJsonObject.get("name");
			System.out.println("Current status of issue is : "+status);
			
			if(status.equals("In Progress") || status.equals("To Do"))
			{
				JSONArray attachmentArray=(JSONArray)fieldJsonObject.get("attachment");
				if(attachmentArray!=null)
				{
						System.out.println("Total number of attachments : "+attachmentArray.size());
						for(int i=0;i<attachmentArray.size();i++)
						{
							JSONObject content=(JSONObject)attachmentArray.get(i);
							String contentURL=(String)content.get("content");
							System.out.println("Content URL is : "+contentURL);
							Desktop.getDesktop().browse(new URI(contentURL));
						}
				}
				else
					System.out.println("No attachment found for the Issue");
			}
			
		System.out.println(con.getResponseCode());
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

}
