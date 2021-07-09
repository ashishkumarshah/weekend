/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.phodu.weekend;

import java.util.Properties;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.configuration.PropertiesBasedGreenMailConfigurationBuilder;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.PropertiesBasedServerSetupBuilder;
import com.icegreen.greenmail.util.ServerSetup;

public class Weekend {
	private GreenMail greenMail;

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(PropertiesBasedServerSetupBuilder.GREENMAIL_SETUP_TEST_ALL, "true");
		properties.put(PropertiesBasedGreenMailConfigurationBuilder.GREENMAIL_USERS, "admin:admin");
		new Weekend().doRun(properties);
	}

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

	private void loadMessagesFromDisk(Properties properties) {
		if (!properties.contains(WeekendConstants.WEEKEND_HOME)) {
			System.out.println("WEEKEND_HOME is not defined in the properties");
		}
	}

	void stop() {
		if (greenMail != null)
			greenMail.stop();
	}
}
