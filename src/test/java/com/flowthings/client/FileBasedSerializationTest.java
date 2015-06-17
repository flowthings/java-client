package com.flowthings.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;

public abstract class FileBasedSerializationTest {
	@BeforeClass
	public static void setup()
	{
		Logger logger = Logger.getLogger("com.flow.client");
		logger.setLevel(Level.WARNING);
	}

	public static String readFromFile(String filename)
	{
		try
		{
			InputStream is = FileBasedSerializationTest.class.getResourceAsStream(filename);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder b = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null)
			{
				b.append(line);
			}
			return b.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
}
