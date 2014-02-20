#Distributed Approximate High Betweenness Set Extraction

Why High Betweenness Set Extraction
==========================================================

(Betweenness Centraility)[http://en.wikipedia.org/wiki/Betweenness_centrality] is a useful property when searching for crital nodes in a network.  The exact calculation of betweenness is however computationally expensive, leave us with the need for approximate methods.

If you want to discover nodes that are most import for connectivity in a large network this project is for you.


Algorithm
-----------

1. Shortest paths are calculated from a set of pivots (source nodes) to all other nodes in the graph.
2. The dependency of each vertex for each pivot is caculated and added to a running total approximated betweenness.
3. The highest betweenness set is extracted.
4. Set stability conditions are checked.  If met algorithm exits, otherwise a new pivot batch is chose and step 1 starts again.


For background information on the approximation method see:
 "W. Chong, Efficent Extraction of High-Betweenness Vertices"
 
 For background information on the method of accumulation of pair dependencies and shortest path data see:
  "U. Brandes, A Faster Algorithm for Betweenness Centrality"


Implementation
---------------------

Our implementation is parallized using the BSP method on Appache giraph and built with Gradle.  Giraph requires a Hadoop cluster (map reduce and hdfs)



I/O
----

The graph is read from an edge list file in HDFS, each line in the file represents a single edge and is coded as a "key,target,weight"  (edge weith is optional and defaults to 1)

Graph vertices must be coded to use integer ids ranging from 0,1,...N where N is number of vertices -1.  So an example graph file could be

>0,1
1,3
2,3
3,4


Output is a directory in HDFS with both the extracted high betweenness set and the approximate betweenness value for all nodes.



Build
-----

This project is built with gradle. (http://www.gradle.org/)

First build and install giraph-1.1.0 into your local maven reposity
(TODO, link to general giraph build instructions for all our giraph projects)


Next edit the build.gradle file to ensure the build uses your version of giraph and hadoop.

You can now build the project by running
 
> gradle clean fatjar
   
Verify that build/libs/HighBetweennessSetExtraction-1.0.jar exists.



Example Run
-----------

A small example is included to verify installation and the general concept in the 'example' directory.

Move the job jar to your current directory for use and create a run configuration for your cluster.

> cp build/libs/HighBetweennessSetExtraction-1.0.jar .
> cp run-template.sh run-example.sh


Edit run-example.sh for your clusrer configuration.  You must set the options for:

1. HDFS_HOST
4. ZK_LIST   (zoo keeper nodes)

Now you'll need to put the example data set int hdfs at the location you specified for EDGE_INPUT_PATH  (you can change this or use the default), also see the value for OUTPUT_DIR.

> hadoop fs -put example/example.csv /tmp/hbse_example/input/example.csv

Finally execute the example and view the results in HDFS

> ./run-example.sh


For me details on the example and expected results see the project wiki




Running on your own data
-----------

After running the example above you are ready to try out your own data.  

> cp run-example.sh run-mydata.sh

Edit run-mydata.sh

1.  VERTEX_COUNT must be set to number of nodes in your data
2.  OUPUT_DIR  - point to your output locaiton in hdfs
3.  EDGE_INPUT_DIR - point to you edge list in hdfs

You may also want to adjust various paramters in the VARIABLE JOB SETTINGS section, such as the pivot batch size, result set size, stability cutoff, or stability counter to taylor the job for your specific application/data.

See below for a description of each parameter.

Other Information
-----------------

The graph must be stored as a directed weighted edge list, stored as a csv file in HDFS.  
The columns required are src, target, edge weight.  (edge weight default to 1 if omitted)

WARNING:  id values for all vertices must be in 0,1,2,...N where N=vertex.count-1


The job outputs a mapping of each vertex id to its approximated betweenness value, and a separate file containing the high betweenness set.


For custom configuration options pass them into giraph runner as -ca arguments (see run.sh script provided for easy running).  
The following is a list of custom arguments and their defaults.


> fs.defaultFS OR fs.default.name
>>the default hdfs file system, normally this will not need to be set as it will correctly be read from the environment.

> betweenness.output.dir
>>Directory in HDFS used to write the high betweenness set.

> betweenness.set.stability
>>Integer value, algorithm completes with the high betweenness set changes by less than this value, checked after each cycle.

> betweenness.set.stability.counter
>>Integer value, number of times the stability threshold must be reached.
  
> betweenness.set.maxSize
>>Size of the result set desired
   
> pivot.batch.size
>>Number of pivots to use in each batch.

> pivot.batch.size.initial
>> Number of pivots to use in the initial batch. defaults to pivot.batch.size    
> vertex.count  
>>Number of vertcies in the graph    
