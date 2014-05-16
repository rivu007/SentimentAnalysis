package org.rssoftware.sentiment.utility;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.rssoftware.sentiment.db.con.DbConnection;

public class PostJobUtil {

	public void cleanUp() {

		deleteRows();
		deleteHDFSFiles();
	}

	/*
	 * Delete the rows from database
	 */

	public void deleteRows() {
		Connection con = null;
		Statement stmt = null;

		try {
			con = DbConnection.getConnection();
			stmt = con.createStatement();
			String sql = "DELETE FROM twitter_sentiment";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					stmt.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Delete the output files and folders from HDFS
	 */

	public void deleteHDFSFiles() {
		try{
			Properties prop = new Properties();
			Configuration conf = new Configuration();
	        conf.addResource(new Path("/usr/local/hadoop/conf/core-site.xml"));
	        conf.addResource(new Path("/usr/local/hadoop/conf/hdfs-site.xml"));
	        FileSystem fileSystem = FileSystem.get(conf);
	        fileSystem.delete(new Path("/user/sentimentoutput.txt"), true);
	        fileSystem.delete(new Path("/user/hduser/output"), true);
	        fileSystem.close();
	        
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
