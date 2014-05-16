package org.rssoftware.sentiment.service;

import java.util.Properties;

import org.rssoftware.sentiment.db.con.DbConnection;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

public class ExportDataToRDBMS {

	public static final void dataTransferViaShell() {
		SSHExec ssh = null;
		Properties prop = new Properties();
		try {
			prop.load(DbConnection.class.getClassLoader().getResourceAsStream("config.properties"));
			// Initialize a ConnBean object, parameter list is ip, username,password
			ConnBean cb = new ConnBean(prop.getProperty("HOST"), prop.getProperty("SSH_USER"), prop.getProperty("SSH_PASS"));
			// Put the ConnBean instance as parameter for SSHExec static method
			// getInstance(ConnBean) to retrieve a singleton SSHExec instance
			ssh = SSHExec.getInstance(cb);
			// Connect to server
			ssh.connect();
			String command = "/usr/local/sqoop-1.4.4/bin/sqoop export --direct --connect jdbc:mysql://localhost/sentiment --table twitter_sentiment --username root --password admin123 --export-dir /user/sentimentoutput.txt;";
			// System.out.println("Command  : "+ command);
			CustomTask sampleTask = new ExecCommand(command);

			ssh.exec(sampleTask);
		} catch (TaskExecFailException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ssh.disconnect();
	}
}
