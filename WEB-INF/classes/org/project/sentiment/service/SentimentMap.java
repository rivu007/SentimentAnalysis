package org.rssoftware.sentiment.service;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class SentimentMap extends Mapper<LongWritable, Text, Text,Text> {
	
	
	Path keywordfilepath;
	BufferedReader keybuffreader;
	Text tweetval = new Text();
	Log log = LogFactory.getLog(SentimentMap.class);
	 Pattern p;
	 Matcher m;
	 
	 
	public void map(LongWritable key, Text value, Context context)  {		
		try{	
			
			//--------------------Dictionary for sms translation --Start-------------------------
			Map<String, String> dict = new HashMap<String, String>();
		    dict.put("wrd", "word");
		    dict.put("congrats", "congratulations");
		    dict.put("cngrats", "congratulations");
		    dict.put("cngra8s", "congratulations");
		    dict.put("cngrts", "congratulations");
		    dict.put("oswm", "awesome");
		    dict.put("awsum", "awesome");
		    dict.put("wat", "what");
		    dict.put("exp", "experience");
		    dict.put("2MOR", "tomorrow");
		    dict.put("u", "you");
		    dict.put("ofc", "office");
		    dict.put("1CE", "once");
		    dict.put("0", "zero");
		    dict.put("1", "one");
		    dict.put("10", "ten");
		    dict.put("10Q", "thank you");
		    dict.put("2", "to");
		    dict.put("2BONO", "to be or not to be");
		    dict.put("2DAY", "today");
		    dict.put("2U2", "to you, too");
		    dict.put("3", "three");
		    dict.put("4ever", "forever");
		    dict.put("@TEOTD", "At The End Of The Day");
		    dict.put("A3", "Anytime");
		    dict.put("AAF", "As A Friend");
		    dict.put("AAR8", "At Any Rate");
		    dict.put("AAR", "At Any Rate");
		    dict.put("AAS", "Alive And Smiling");
		    dict.put("AAYF", "As Always, Your Friend");
		    dict.put("ABT2", "About To");
		    dict.put("ACD", "Alt Control Delete");
		    dict.put("ACK", "Acknowledgement");
		    dict.put("r", "are");
		    dict.put("frnd", "friend");
		    dict.put("AFIAA", "As Far As I Am Aware");
		    dict.put("ADN", " Any day now");
		    dict.put("ADBB", "All Done Bye Bye");
		    dict.put("ADR", "Address");
		    dict.put("AFC", "Away From Computer");
		    dict.put("BAU", "Business As Usual");  
		    dict.put("CTN", "Can't Talk Now");   
		    dict.put("ATM", "At The Moment");    
		    dict.put("FC", "Fingers Crossed");
		    dict.put("AFAGAY", "A Friend As Good As You ");
		    dict.put("AEAP", "As Early As Possible");
		    dict.put("ADIP", "Another Day In Paradise");
		    dict.put("ADIH", "Another Day In Hell");
		    dict.put("AFAIC", "As far as I'm concerned");
		    dict.put("AFAIUI", "As Far As I Understand It");
		    dict.put("CU", "See You");
		    dict.put("CUL", "See you later");
		    dict.put("CUL8ER", "See You Later");
		    dict.put("CUL8R", "See You Later");
		    dict.put("CUL8TR", "See you later");
		    dict.put("AFAP", "As Far As Possible");
		    dict.put("AFIAA", "As Far As I Am Aware");
		    dict.put("AFK", "Away From Keyboard");
		    dict.put("AFU", "All fucked up");
		    dict.put("AGKWE", "And Good Knows What Else");
		    dict.put("AIH", "As It Happens");
		    dict.put("AIMB", "As I Mentioned Before");
		    dict.put("AISE", "As I Said Earlier");
		    dict.put("AISI", "As I see it");
		    dict.put("AKA", "Also Known As");
		    dict.put("ALAP", "As Late As Possible");
		    dict.put("ALTG", "Act Locally, Think Globally");
		    dict.put("HHOJ", "Ha-Ha, Only Joking");
		    dict.put("HHOK", "Ha ha, only kidding");
		    dict.put("HHOS", "Ha-Ha, Only Being Serious");
		    dict.put("BAU", "Business As Usual");
			dict.put("BC", "Because");
			dict.put("B4", "Before");
			dict.put("B", "be");
			dict.put("B4N", "Bye For Now");
			dict.put("BITD", "Back in the day");
			dict.put("BITMT", "But in the meantime");
			dict.put("BKA", "Better Known As");
			dict.put("BM", "Byte me");
			dict.put("BMOTA", "Byte me on the ass");
			dict.put("CUZ", "Because");
			dict.put("CWOT", "Complete Waste Of Time");
			dict.put("CWYL", "Chat with you later");    
			dict.put("CY", "Calm Yourself");
			dict.put("CYA", "See Ya");
			dict.put("CYL", "See You Later");
			dict.put("CYT", "See You Tomorrow");
			dict.put("COB", "Close Of Business");
			dict.put("DBAU", "Doing business as usual");
			dict.put("FYA", "For your amusement");
			dict.put("FYI", "For Your Information");
			dict.put("FYIFV", "fuck you, I'm fully vested");
			dict.put("FYIV", "fuck you, I'm vested");
			dict.put("FYM", "For your misinformation");
			dict.put("FYYSOS", "Fuck you, you sack of shit");
			dict.put("G2G", "Got to Go");
			dict.put("G9", "Genius");
			dict.put("GA", "Go ahead");
			dict.put("GAL", "Get A Life");
			dict.put("IC", "I See");
			dict.put("grt", "great");
			dict.put("FTITM", "First Thing In The Morning");
			dict.put("FTL", "Faster than light");
			dict.put("FTMFW", "For The Mother Fucking Win");
			dict.put("FTMFWB", "For The Mother Fuckin Win Bitches");
			dict.put("FTTB", "For the time being");
			dict.put("FTTYYGTS", "Fart That Tells You You Got To Shit");
			dict.put("FUBAB", "fucked up beyond all belief");
			dict.put("FUBAR", "fucked up beyond all reason");
			dict.put("FUBB", "Fucked Up Beyond Belief");
			dict.put("FUBIO", "Fuck You Buddy I'm Out");
			dict.put("FUD", "Fear, Uncertainty and Doubt");
			dict.put("FWD", "Forward");
			dict.put("FWIW", "For What It's Worth");
			dict.put("ICBW", "I Could Be Wrong");
			dict.put("ICQ", "I Seek you");
			dict.put("ID10T", "Idiot");
			dict.put("IDC", "I Don't Care");
			dict.put("gr8", "great");
			dict.put("fuk", "fuck");
			dict.put("fck", "fuck");
			dict.put("fk", "fuck");
			dict.put("vry", "very");
			dict.put("idk", "I Don't Know");
			dict.put("IFAB", "I found a bug");
			dict.put("ifu", "I fucked up");
			dict.put("KISS", "Keep It Simple, Stupid");
			dict.put("IGTP", "I Get The Point");
			dict.put("ihtfp", "I Hate This Fucking Place");
			dict.put("KIT", "Keep in touch");
			dict.put("LTNS", "Long Time No See");
			dict.put("LTNT", "Long time, no talk");
			dict.put("LTR", "Long term relationship");
			dict.put("l8", "late");
			dict.put("l8er", "later");
			dict.put("l8r", "later");
			dict.put("L8RS", "Laters");
			dict.put("l8tr", "Later");
			dict.put("LAGNAF", "Let's all get naked and fuck!");
			dict.put("msg", "message");
			dict.put("lagnaf", "Let's all get naked and fuck!");
			dict.put("mtbf", "Mean Time Between Failure");
			dict.put("mte", "My Thoughts Exactly");
			dict.put("mtfbwu", "Mean Time Between Failure");
			dict.put("mtfbwy", "Mean Time Between Failure");
			dict.put("mubar", "Mean Time Between Failure");
			dict.put("MWBRL", "More Will Be Revealed Later");
			dict.put("MYOB", "Mind your own Business");
			dict.put("n1", "Nice One");
			dict.put("n2m", "Not To Mention");
			dict.put("na", "Not applicable or Not affiliated");
			dict.put("nak", "Nursing at keyboard");
			dict.put("nalopkt", "Not A Lot of People Know That");
			dict.put("nap", "Not a problem");
			dict.put("naz", "Name, Address, Zip");
			dict.put("nbd", "No big deal");	
			dict.put("nbif", "No basis in fact");
			dict.put("nc", "No comment");
			dict.put("ncg", "New college graduate");
			dict.put("ne", "any");
			dict.put("ne1", "anyone");
			dict.put("ne1 er", "anyone here?");
			dict.put("nethng", "anything");
			dict.put("nfc", "No fucking clue");
			dict.put("nfi", "No fucking idea");
			dict.put("nfw", "No fucking way");
			dict.put("cud", "could");
			dict.put("cld", "could");
			dict.put("cd", "could");
			dict.put("cod", "could");
			dict.put("wd", "would");
			dict.put("wld", "would");
			dict.put("wod", "would");
			dict.put("wud", "would");
			dict.put("vry", "very");
			dict.put("verrry", "very");
			dict.put("vryyy", "very");
			dict.put("veerrryyy", "very");
			dict.put("por", "poor");
			dict.put("b4", "before");
			dict.put("bfor", "before");
			dict.put("bfore", "before");
			dict.put("rdy", "ready");
			dict.put("4", "for");
			dict.put("fav", "favourite");
			dict.put("probs", "problem");
			dict.put("yup", "yes");
			dict.put("2o", "too");
			dict.put("dis", "this");
			dict.put("ths", "thus");
			dict.put("da", "the");
			dict.put("d", "the");
			dict.put("dud", "dude");
			dict.put("h8", "hate");
			dict.put("wtf", "what the fuck");
			dict.put("ne", "any");
			dict.put("jst", "just");
			dict.put("9c", "nice");
			dict.put("gd", "good");
			dict.put("vry", "very");
			
			
						
		//-----------------------------------------------------------------------------------
			
			
			Configuration conf = new Configuration();
			 
		 	conf.addResource(new Path("/usr/local/hadoop/conf/core-site.xml"));
	        conf.addResource(new Path("/usr/local/hadoop/conf/hdfs-site.xml"));
			
		/*	
			log.info("Hit Map class****************");
			keywordfilepath=new Path("/usr/inputs/workspace/SentimentAnalysis_Twitter/files/keywords.txt");
			log.info("====Keyword File Path====="+keywordfilepath);
            FileSystem fs1 = FileSystem.get(URI.create("files/keywords.txt"),new Configuration());
            log.info("----After file system-------");
            keybuffreader=new BufferedReader(new InputStreamReader(fs1.open(keywordfilepath)));
            log.info("----After keybuffreader-------");
            String keyword = "";
            log.info("----After reading file path-------");
            
            while(keybuffreader.ready())
            {            	
            	keyword=keybuffreader.readLine().trim();           
            
            }
          
            */
	        String keyword = "bellagio";
            log.info("after reading keyword in MAP-------"+keyword);
            final Text searchKey = new Text(keyword);
            
            log.info("Entered keyword in Map: "+ searchKey);
            
                  
            
            if(value == null){
			return;
            } 
            else {			
				StringTokenizer tokens = new StringTokenizer(value.toString(),",");
				int count = 0;
				while(tokens.hasMoreTokens()) {
					count ++;
					if(count <=1)
					continue;
					String tweet = tokens.nextToken();
				
				  if(tweet.contains(keyword)){	
					  StringBuffer result = new StringBuffer();
					//============Machine Translation ---Start============
		            p = Pattern.compile("\\w+");
					m = p.matcher(tweet);

					while (m.find()) {
					    String toInsert = m.group();
					    if (dict.containsKey(toInsert))
					        toInsert = dict.get(toInsert);
					   // System.out.println("========Inside in While loop===========");
					    m.appendReplacement(result, toInsert);
					
					}

					m.appendTail(result);
					//============Machine Translation ---End===============
					
					
					
					//if(tweet.contains(keyword.toLowerCase().trim())) {	
						
					if(result.toString().contains(keyword.toLowerCase().trim())) {	
					log.info("------------Tweets contains keyword-------------"+searchKey);
					
					tweetval.set(result.toString());
					log.info("------------Tweets contains -------------"+result);
					context.write(searchKey,tweetval);
				}	
			  }
			}//while
            }//else
		}
		catch(Exception e){
			System.out.println("Exception in Map Phase*****************: ");
			e.printStackTrace();
			//e.printStackTrace();
		}
	}//map
}