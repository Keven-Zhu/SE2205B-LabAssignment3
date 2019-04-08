
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Arrays;

import static java.lang.Math.E;

public class Assignment3 {
    public Vertex<V> parArray[];

    public int breadthFirstPathSearch(Graph fN, int s, int d)
    {
        //Set the size of the parent array
        parArray = new Vertex<V>[fN.numVertices()];
        //Create a visited array based on the size the number of vertices in the graph
        //In java an int array's values are initialized to zero upon declaration
        int visitedNodes[] = new int[fN.numVertices()];
        //Create a linkedlist queue
        LinkedListQueue<Vertex<V>> LLQ = new LinkedListQueue<Vertex<V>>();
        //enqueue the starting vertex into the queue
        LLQ.enqueue(fN.getVertex(s));
        //Set the vertex as visited in the visted array
        visitedNodes[0] = 1;
        //Continue to add nodes to the queue until it's empty
        while(!LLQ.isEmpty())
        {
            //dequeue the vertex in the queue
            Vertex<V> dequeuedNode = LLQ.dequeue();
            //iterate through the collection of nodes adjacent to the dequeued vertex
            for(Edge<E> edge:fN.outgoingEdges(dequeuedNode))
            {
                //Get the vertex that is adjacent to the dequeued vertex
                Vertex<V> temp = fN.opposite(dequeuedNode,edge);
                //Check if the vertex has been visited, if not go to the next check
                if(visitedNodes[temp.getLabel()] == 0)
                {
                    //Check if flow can be added
                    if((edge.flowCapacity()-edge.flow())> 0)
                    {
                        //if so set the parent of the temp vertex to the dequeued vertex and add the temp vertex to the queue
                        //also set the vertex to visited in the visited array
                        parArray[temp.getLabel()] = dequeuedNode;
                        LLQ.enqueue(temp);
                        visitedNodes[temp.getLabel()] = 1;
                    }
                }
            }

        }

        //If the value of the array in the last index,d, is not equal to 0 then return 1. Otherwise return 0.
        if(visitedNodes[d] != 0)
        {
            return 1;
        }

        return 0;

    }

    public void maximizeFlowNetwork(Graph fN, int s, int d)
    {
        //Create arraylist to keep track of edges that form a path from s to d
        ArrayList<Edge<E>> edges = new ArrayList<Edge<E>>();
        //Create array to hold all the possible max flows
        ArrayList<Integer> listOfMaxFlows = new ArrayList<Integer>();
        while (breadthFirstPathSearch(fN,s,d)==1)
        {
            //Get reference to parent of last vertex in parArray
            //Get reference to the last vertex in the graph (d)
            Vertex<V> temp1 = parArray[d];
            Vertex<V> temp2 = fN.getVertex(d);
            //Check if temp1 is the same as the starting vertex
            while (temp1.getLabel() != fN.getVertex(s).getLabel()) {
                //Add the edge between the two vertices
                edges.add(fN.getEdge(temp1, temp2));
                temp2 = temp1;
                temp1 = parArray[temp1.getLabel()];
            }
            //Finally add the edge between the starting vertex to the next vertex
            edges.add(fN.getEdge(temp1, temp2));

            //Find the max flow out of all the edges in the path
            int max = edges.get(0).flowCapacity();
            for(Edge<E> edge: edges)
            {
                if(edge.flowCapacity()< max)
                {
                    max = edge.flowCapacity();
                }
            }
            //Set the flow of all the edges in the path
            for(Edge<E> edge: edges)
            {
                edge.flow = max;
            }

            listOfMaxFlows.add(max);
        }

        //Once the max flow is achieved print out the highest flow of the array of max flows
        System.out.println("The max flow is "+ Collections.max(listOfMaxFlows));
    }
}
