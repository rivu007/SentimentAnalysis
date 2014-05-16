package org.rssoftware.sentiment.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.rssoftware.sentiment.service.SentimentMap;
import org.rssoftware.sentiment.service.SentimentReducer;

/**
 * Servlet implementation class MRService
 */
// @WebServlet("/MRService")
public class MRService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public MRService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			Log log = LogFactory.getLog(MRService.class);
			log.info("======In Driver Class=======");

			Configuration conf = new Configuration();

			Job job = new Job(conf, "MRService.class");
			job.setJarByClass(MRService.class);
			job.setJobName("SentimentAnalysis");

			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);

			job.setMapperClass(SentimentMap.class);
			job.setNumReduceTasks(30);
			job.setCombinerClass(SentimentReducer.class);
			job.setReducerClass(SentimentReducer.class);

			// Log log = LogFactory.getLog(SentimentMap.class);
			// Log log = LogFactory.getLog(SentimentReducer.class);

			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);

			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);

			// FileInputFormat.addInputPath(job, new
			// Path("/usr/inputs/workspace/SentimentAnalysis_Twitter/input/"));
			// FileOutputFormat.setOutputPath(job,new
			// Path("/usr/inputs/workspace/SentimentAnalysis_Twitter/output"));

			FileInputFormat.addInputPath(job, new Path(
					"hdfs://localhost:54310/user/hduser/input/"));
			FileOutputFormat.setOutputPath(job, new Path(
					"hdfs://localhost:54310/user/hduser/output"));

			job.waitForCompletion(true);
			RequestDispatcher reqDisp = request.getRequestDispatcher("./JobCompleted.jsp");
			reqDisp.forward(request, response);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
