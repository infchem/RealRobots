package infchem.realrobots.connector;


import infchem.realrobots.RealRobots;
import infchem.realrobots.config.ConfigHandler;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class ConnectorHTTP {


	@SuppressWarnings("resource")
	public String request( String req ) throws Exception
	{
		String r = "";
		URL u = new URL( "http://"+ConfigHandler.piIP+"?" + req );
		try {
		r = new Scanner( u.openStream() ).useDelimiter( "\\Z" ).next();
		} catch (Exception $e) {
			
		}
		return r;
	}
}
