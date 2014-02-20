package com.soteradefense.betweenness.giraph.io;

import java.io.IOException;

import org.apache.giraph.io.EdgeReader;
import org.apache.giraph.io.ReverseEdgeDuplicator;
import org.apache.giraph.io.formats.TextEdgeInputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * Reads a comma separated, line delimited, edge file from hdfs.
 *
 * WARNINGS / NOTES
 * 
 * id (src/target) values for all vertices must be in 0,1,2,...N where N=vertex.count-1 as set in the job configuration.
 * 
 * The input file is read as a DIRECTED GRAPH. If you want to run
 * over an undirected graph you'll need to pre-process your input to add edges
 * in both directions where needed, or set the io.edge.reverse.duplicator custom argument to true.
 * 
 * Format:
 * src,target,weight
 * OR
 * src,target (if no weight is specified a weight of 1 is assumed.
 * 
 * 
 * 
 * @author Eric Kimbrel - Sotera Defense, eric.kimbrel@soteradefense.com
 *
 */
public class HBSEEdgeInputFormat extends TextEdgeInputFormat<IntWritable,IntWritable>{

	  
	@Override
	public EdgeReader<IntWritable, IntWritable> createEdgeReader(
			InputSplit split, TaskAttemptContext context) throws IOException {
		String duplicator = this.getConf().get("io.edge.reverse.duplicator");
		boolean useDuplicator =  (null != duplicator) ? Boolean.parseBoolean(duplicator) : false;
		EdgeReader<IntWritable,IntWritable> reader = (useDuplicator) ? new ReverseEdgeDuplicator<IntWritable, IntWritable>(new SBEdgeReader()) : new SBEdgeReader();
		return reader;
	}
	
	
	class SimpleEdge{
		int src;
		int target;
		int weight;
		
		public SimpleEdge(String[] tokens){
			src = Integer.parseInt(tokens[0]);
			target = Integer.parseInt(tokens[1]);
			weight = (tokens.length > 2) ? Integer.parseInt(tokens[2]) : 1;
		}
	}
	
	  
	public class SBEdgeReader extends TextEdgeReaderFromEachLineProcessed<SimpleEdge>{

		@Override
		protected SimpleEdge preprocessLine(Text line) throws IOException {
			String[] tokens = line.toString().split(",");
			return new SimpleEdge(tokens);
		}

		@Override
		protected IntWritable getTargetVertexId(SimpleEdge line) throws IOException {
			return new IntWritable(line.target);
		}

		@Override
		protected IntWritable getSourceVertexId(SimpleEdge line) throws IOException {
			return new IntWritable(line.src);
		}

		@Override
		protected IntWritable getValue(SimpleEdge line) throws IOException {
			return new IntWritable(line.weight);
		}

	
	
	}

	

}
