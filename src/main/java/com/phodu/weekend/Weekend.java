/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.phodu.weekend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.configuration.PropertiesBasedGreenMailConfigurationBuilder;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.PropertiesBasedServerSetupBuilder;
import com.icegreen.greenmail.util.ServerSetup;

/**
 * Simple utility to read eml files from a folder and make them available over
 * pop/imap
 * 
 * This utility assumes that you put your eml file(s) in your
 * ~/Downloads/weekend folder. To view the eml in a pop3 inbox, connect to
 * 127.0.0.1 on port 3110 with admin/admin. These defaults can be overridden in
 * a file weekend.properties.
 * 
 * @author ashishkumar.shah
 *
 */
public class Weekend {
	private GreenMail greenMail;

	public static void main(String[] args) {
		Properties properties = loadProperties();
		new Weekend().doRun(properties);
	}

	/**
	 * Function to load the properties for configuring the mock email server
	 * 
	 * @return Properties object
	 */
	private static Properties loadProperties() {

		Properties properties = new Properties();

		try {
			FileReader reader = new FileReader("weekend.properties");
			properties.load(reader);
		} catch (IOException ioException) {
			System.out.println("weekend.properties is not present assuming defaults");
		}

		properties.put(PropertiesBasedServerSetupBuilder.GREENMAIL_SETUP_TEST_ALL, "true");

		if (!properties.containsKey(PropertiesBasedGreenMailConfigurationBuilder.GREENMAIL_USERS)) {
			properties.put(PropertiesBasedGreenMailConfigurationBuilder.GREENMAIL_USERS, "admin:admin");
		}
		if (!properties.containsKey(WeekendConstants.WEEKEND_HOME)) {
			properties.put(WeekendConstants.WEEKEND_HOME, System.getProperty("user.home") + "/Downloads/weekend");
		}
		return properties;
	}

	/**
	 * Function to start green mail server
	 * 
	 * @param properties Properties for initializing greenmail
	 */
	private void doRun(Properties properties) {

		ServerSetup[] serverSetups = new PropertiesBasedServerSetupBuilder().build(properties);

		if (serverSetups.length == 0) {
			System.out.println("Check properties");

		} else {
			greenMail = new GreenMail(serverSetups);
			GreenMailConfiguration configuration = new PropertiesBasedGreenMailConfigurationBuilder().build(properties);
			greenMail.withConfiguration(configuration).start();
			loadMessagesFromDisk(properties);
		}
	}

	/**
	 * Function to load the eml's from disk and delivering them to an inbox
	 * 
	 * @param properties
	 */
	private void loadMessagesFromDisk(Properties properties) {
		File homeFolder;
		try {
			homeFolder = new File(properties.getProperty(WeekendConstants.WEEKEND_HOME));
			if (!homeFolder.isDirectory() || !homeFolder.canRead()) {
				System.out.println("Please check if the path points to a directory and it is readable");
				return;
			}
			File[] folderContents = homeFolder.listFiles();
			GreenMailUser user = null;
			if (folderContents.length > 0) {
				user = greenMail.setUser("admin", "admin", "admin");
			}
			for (File folderContent : folderContents) {
				if (!folderContent.isFile()) {
					continue;
				}
				InputStream is = new FileInputStream(folderContent);
				MimeMessage message = GreenMailUtil.newMimeMessage(is);
				user.deliver(message);
				is.close();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void stop() {
		if (greenMail != null)
			greenMail.stop();
	}
}
