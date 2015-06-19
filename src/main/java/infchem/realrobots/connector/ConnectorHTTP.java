package infchem.realrobots.connector;


import infchem.realrobots.RealRobots;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class ConnectorHTTP {


	public String request( String req ) throws Exception
	{
		String r = "";
		URL u = new URL( "http://"+RealRobots.piIp+"?" + req );
		try {
		r = new Scanner( u.openStream() ).useDelimiter( "\\Z" ).next();
		} catch (Exception $e) {
			
		}
		return r;
	}
}
