package com.soteradefense.betweenness.giraph.io;

import java.io.IOException;

import org.apache.giraph.graph.Vertex;
import org.apache.giraph.io.formats.TextVertexOutputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.soteradefense.betweenness.giraph.compute.VertexData;

/**
 * Writes the approximated betweenness value for each vertex
 * 
 * @author Eric Kimbrel - Sotera Defense, eric.kimbrel@soteradefense.com
 *
 */
public class HBSEOutputFormat extends TextVertexOutputFormat<IntWritable,VertexData,IntWritable>{

	@Override
	public TextVertexWriter createVertexWriter(
			TaskAttemptContext context) throws IOException,
			InterruptedException {
		return new SBVertexWriter();
	}

	public class SBVertexWriter extends TextVertexWriter{

		public void writeVertex(
				Vertex<IntWritable, VertexData, IntWritable> vertex)
				throws IOException, InterruptedException {
			
			double approxBC = vertex.getValue().getApproxBetweenness();
			getRecordWriter().write(new Text(Integer.toString(vertex.getId().get())), new Text(Double.toString(approxBC)));
		}
		
	}

}
