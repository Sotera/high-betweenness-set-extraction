This project is now deprecated. (See: [Distributed Graph Analytics](https://github.com/Sotera/distributed-graph-analytics))
==========================================================




Why High Betweenness Set Extraction
==========================================================

[[Betweenness Centrality](http://en.wikipedia.org/wiki/Betweenness_centrality)] is a useful property when searching for critical nodes in a network.  The exact calculation of betweenness is however computationally expensive, which leaves us with the need for approximate methods.

If you want to discover nodes that are most import for connectivity in a large network this project is for you.


Algorithm
-----------

1. Shortest paths are calculated from a set of pivots (source nodes) to all other nodes in the graph.
2. The dependency of each vertex for each pivot is calculated and added to a running total approximated betweenness.
3. The highest betweenness set is extracted.
4. Set stability conditions are checked.  If met algorithm exits, otherwise a new pivot batch is chosen and step 1 starts again.


For background information on the approximation method see:
 "W. Chong, Efficient Extraction of High-Betweenness Vertices"
 
 For background information on the method of accumulation of pair dependencies and shortest path data see:
  "U. Brandes, A Faster Algorithm for Betweenness Centrality"


Implementation
---------------------

Our implementation is distributed using the BSP method on Apache giraph and built with Gradle.  Giraph requires a Hadoop cluster (map reduce and hdfs)



I/O
----

The graph is read from an edge list file in HDFS, each line in the file represents a single edge and is coded as a "key,target,weight"  (edge weight is optional and defaults to 1)

Graph vertices must be coded to use integer ids ranging from 0,1,...N where N is number of vertices -1.  So an example graph file could be..

[example.csv](https://github.com/Sotera/high-betweenness-set-extraction/blob/master/example/example.csv)


Output is a directory in HDFS with both the extracted high betweenness set and the approximate betweenness value for all nodes.



Build
-----

see [How to Build](https://github.com/Sotera/high-betweenness-set-extraction/wiki/How-to-Build)


How to Run
----------
see [How to Run](https://github.com/Sotera/high-betweenness-set-extraction/wiki/How-to-Run)


Configuration
-------------

[Configuration](https://github.com/Sotera/high-betweenness-set-extraction/wiki/Configuration-Options)


Benchmarks and Trade Offs
-------------------------

[Benchmarks and trade offs](https://github.com/Sotera/high-betweenness-set-extraction/wiki/Benchmarks-and-trade-offs)

