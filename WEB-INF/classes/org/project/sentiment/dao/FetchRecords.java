package org.rssoftware.sentiment.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.rssoftware.sentiment.db.con.DbConnection;

public class FetchRecords {

	public ArrayList<String> showReport() {

		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		ArrayList<String> sentimentResult = new ArrayList<String>();
		try {
			con = DbConnection.getConnection();
			if (con != null) {

				stmt = con.createStatement();
				String sql;
				sql = "SELECT searchkey, totalrec, pospercent, negpercent,neutralpercent FROM twitter_sentiment WHERE searchkey='bellagio'";
				rs = stmt.executeQuery(sql);

				// Extract data from result set
				while (rs.next()) {
					// Retrieve by column name
					String key = rs.getString("searchkey");
					double totalTweet = rs.getInt("totalrec");
					double positivePerc = rs.getInt("pospercent");
					double negetivePerc = rs.getInt("negpercent");
					double neutralPerc = rs.getInt("neutralpercent");
					// Evaluating overall sentiment
					double sentimentPerc =  (positivePerc/(100-neutralPerc)) * 100;
					System.out.println("sentimentPerc:" + sentimentPerc);
					// Inserting the Sentiment Result into List
					sentimentResult.add(key);
					sentimentResult.add(""+sentimentPerc);
					
					// Display values
					System.out.print("key: " + key);
					System.out.print(", totalTweetCount: " + totalTweet);
					System.out.print(", positivePerc: " + positivePerc);
					System.out.println(", negetivePerc: " + negetivePerc);
					System.out.println(", ToralPerc: " + sentimentPerc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				rs.close();
				stmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return sentimentResult;
	}
}
