package org.rssoftware.sentiment.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.sqoop.SqoopOptions;
//import org.apache.sqoop.tool.ExportTool;
//import com.cloudera.sqoop.SqoopOptions;


import uk.ac.wlv.sentistrength.SentiStrength;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.WordStemmer;

 public class SentimentReducer extends Reducer<Text,Text,Text,Text> implements RandomAccess
{
	 	Log log = LogFactory.getLog(SentimentReducer.class);
	 	Path posfilepath;
	    Path negfilepath;
	    Path outputfilepath;
	    Path keywordfilepath;
	    
	   // BufferedReader posbuffreader;
	   // BufferedReader negbuffreader;
	    BufferedReader keybuffreader; 
	    
	    static Double totalrecords=new Double("0");
	    static Double total49ersrecords=new Double("0");
	   
	   	    
	    static Double posCount=new Double("0");
	    static Double negCount=new Double("0");
	    static Double neuCount=new Double("0");
	    
	   
	    static Double negpercent=new Double("0");
	    static Double pospercent=new Double("0");
	    static Double neupercent=new Double("0");
	   
	    HTable table;
	    Configuration config = HBaseConfiguration.create();
	   
	    
	
	    Pattern pattern;
	    Matcher matcher1;
	    static int row=0;
	    FSDataOutputStream out;
	 

    public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException
    {
       
    	log.info("------------Hit Reducer-------------");
    	
    	Configuration conf1 = new Configuration();
        conf1.addResource(new Path("/usr/local/hadoop/conf/core-site.xml"));
        conf1.addResource(new Path("/usr/local/hadoop/conf/hdfs-site.xml"));
  
        String check1=key.toString();
     
        FileSystem fileSystem = FileSystem.get(conf1);
        FileSystem fileSystemPosNeg = FileSystem.get(conf1);
        
        Path path = new Path("/user/sentimentoutput.txt");
        Path pathPosNegTweets = new Path("/user/posnegetiveoutput.txt");
        
        
           
        if (!fileSystem.exists(path)) {           
            log.info("File does not exists=====================");
            out = fileSystem.create(path);
            //out1 = fileSystemPosNeg.create(pathPosNegTweets);
           
        }  
            
          
        log.info("------------key is -------------"+key.toString());
                  
        if(check1.equals(key.toString().toLowerCase()))
        {
        	log.info("------------In Reducer method with key-----------"+key.toString());
        	
            Iterator<Text> it = values.iterator(); 
            while(it.hasNext()){
        	//for(Text twit:values)
            //{	
            	Text twit = it.next();
            	++totalrecords;
         
            	//===========Start--Parser Grammar & Sentiment Detection=============
           
            	LexicalizedParser lp = LexicalizedParser.loadModel(
        				"edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz"
        			);
        		
        			lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});

        			//String sent = "My name is Rahul";
        			 TreebankLanguagePack tlp = lp.getOp().langpack();
        			  GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

        			
        			
        			Iterable<List<? extends HasWord>> sentences;
        			
        			
        			//String sent2 = "wowww!!!!this phone is awesome";
        			
        			//String sent2 = "just fuck off";
                            
        		    Tokenizer<? extends HasWord> toke =
        		      tlp.getTokenizerFactory().getTokenizer(new StringReader(twit.toString()));
        		    List<? extends HasWord> sentence2 = toke.tokenize();
        		    List<List<? extends HasWord>> tmp =
        		      new ArrayList<List<? extends HasWord>>();
        		    //tmp.add(sentence);
        		    tmp.add(sentence2);
        		    	sentences = tmp;
        			
        			
        			//List<HasWord> sentence = null;
        			
        			Tree parse = (Tree) lp.apply(sentence2);
        			double score = parse.score();
        			parse.pennPrint();
        			
        			//--------typed-dependency ---start--------
        			 WordStemmer ls = new WordStemmer(); 
        			ArrayList<String> words = new ArrayList();
        			ArrayList<String> stems = new ArrayList();
        			ArrayList<String> tags = new ArrayList();
        			
        			// Get words and Tags
        			for (TaggedWord tw : parse.taggedYield()){
        				words.add(tw.word());
        				tags.add(tw.tag());
        			}
        		 	
        			// Get stems
        		    ls.visitTree(parse); // apply the stemmer to the tree
        			for (TaggedWord tw : parse.taggedYield()){
        				stems.add(tw.word());
        			}
        			// Get dependency tree
        						//TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        				 	  //  GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        				 	    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        				 	    Collection tdl = gs.typedDependenciesCollapsed();
        				 	    
        						// And print!
        					    //System.out.println("words: "+words); 
        					   // System.out.println("POStags: "+tags); 
        						//System.out.println("stemmedWordsAndTags: "+stems); 
        						//System.out.println("typedDependencies: "+tdl); 
        			
        			
        			
        			SentiStrength sentiStrength = new SentiStrength();
        			String ssthInitialisation[] = {"sentidata", "/home/hduser/Downloads/SentiStrength/", "explain"};
        			sentiStrength.initialise(ssthInitialisation); //Initialise
        			
        			//System.out.println("Sentiment score is"+sentiStrength.computeSentimentScores(twit.toString())); 
        			//---------------start--tokenize scoring--------------------
        			//log.info("--The value is in reduce---"+twit.toString());
        			String firststscore = sentiStrength.computeSentimentScores(twit.toString()).substring(0, 1); 
        			String secondscore = sentiStrength.computeSentimentScores(twit.toString()).substring(3, 4); 
        			
        			double first = Double.parseDouble(firststscore);
        			double second = Double.parseDouble(secondscore);
        			
        			
        			if(first > second){
        				++posCount;
        				//System.out.println("This is a sentence with positive sentiment..........count is "+posCount);
        			}
        			if(first < second){
        			++negCount;
        				//System.out.println("This is a sentence with negetive sentiment..........count is "+negCount);
        			}
        			
        			if (first==second){
        				++neuCount;
        				//System.out.println("This is a sentence with neutral sentiment..........count is "+neuCount);
        			}
        			
        			//===========End--Parser Grammar & Sentiment Detection=============
        			
        			//System.out.println("firststscore is"+firststscore);
        			//System.out.println("secondscore is"+secondscore);
        			
        			//---------------end--tokenize scoring--------------------
        			
        			//2 -1 The phone is good[2][sentence: 2,-1]
        			
        			
        			//-----------------------Sentiment Detection -- End-------------------------
            	
        					
        			
            }//for
             
            
            //System.out.println("Positive count is...... "+posCount);
            //System.out.println("Negetive count is...... "+negCount);
            //System.out.println("Neutral count is...... "+neuCount);
            
            pospercent=posCount/totalrecords*100;
            negpercent=negCount/totalrecords*100;
            neupercent=neuCount/totalrecords*100;
            
            
            
          try{
        	  
        	log.info("Keyword in reducer before writing in HDFS: ********************"+key.toString());  
	        out.writeBytes("\n"+key);
	        out.writeBytes(","+totalrecords); //total search
            out.writeBytes(","+pospercent);//positive
            out.writeBytes(","+negpercent); //negative
            out.writeBytes(","+neupercent);//neutral
          
            out.close();
            fileSystem.close();
          }catch(Exception e){
        	  e.printStackTrace();
          }
          /*
            // Entry into HBase
            Put p = new Put(Bytes.toBytes("Hadoop"));

            table = new HTable(config, "TwitterTweets");
            p.add(Bytes.toBytes("Tweets"),Bytes.toBytes("positive%"),Bytes.toBytes(pospercent.toString()));
            table.put(p);

            p.add(Bytes.toBytes("Tweets"),Bytes.toBytes("negitive%"),Bytes.toBytes(negpercent.toString()));
            table.put(p);
            p.add(Bytes.toBytes("Tweets"),Bytes.toBytes("neutral%"),Bytes.toBytes(neupercent.toString()));
            table.put(p);
            p.add(Bytes.toBytes("Tweets"),Bytes.toBytes("Hadoop"),Bytes.toBytes(totalrecords.toString()));
            table.put(p);
            // conn.closeTable();
            table.close();
            */
          ExportDataToRDBMS.dataTransferViaShell();
        }//if
       
       
       
         
    }//reduce(-,-,-)
}//reducer class
