package husacct.analyse.task.reconstruct.mojo;

import java.util.*;

/* Vertex in a bipartite graph */
class Vertex {

    boolean matched = false;

    boolean isLeft = false;

    int outdegree = 0;

    int indegree = 0;
}

class BipartiteGraph {

    /*
     * we use a vector to represent a edge list in a directed graph for example,
     * adjacentList[1] has 2 means there is a edge from point 1 to point 2
     */

    Vector<Vector<Integer>> adjacentList;

    /* vertex list */
    Vertex vertex[];

    /* this list is used to store the augmenting Path we got from matching */
    Vector<Integer> augmentPath;

    /* total number of all points,points in left side and right side */
    int points, leftpoints, rightpoints;

    /* create the graph,points means the toal number of points */
    BipartiteGraph(int points, int leftpoints, int rightpoints) {
        this.leftpoints = leftpoints;
        this.rightpoints = rightpoints;
        this.points = points;

        adjacentList = new Vector<Vector<Integer>>(points);
        vertex = new Vertex[points];
        augmentPath = new Vector<Integer>();

        for (int i = 0; i < points; i++)
        {
            vertex[i] = new Vertex();
            if (i < leftpoints) vertex[i].isLeft = true;
            adjacentList.add(i, new Vector<Integer>());
        }

    }

    /* add edge, add an edge to the graph */
    public void addedge(int startPoint, int endPoint) {
        /* insert the edge to the adjacentList of startPoint */
        adjacentList.elementAt(startPoint).add(new Integer(endPoint));
        /* increase the outdegree of startPoint, indegree of endPoint */
        vertex[startPoint].outdegree += 1;
        vertex[endPoint].indegree += 1;
        /*
         * if the edge is from right to left side, mark both start and end as
         * mached point
         */
        if (isRight(startPoint) && isLeft(endPoint))
        {
            vertex[startPoint].matched = true;
            vertex[endPoint].matched = true;

        }
    }

    public void removeEdge(int startPoint, int endPoint) {
        /* find the index of edge in the adjacentList of startPoint */
        int index = adjacentList.elementAt(startPoint).indexOf(new Integer(endPoint));
        /* remove the edge from adjacentList of startPoint */
        if (index > -1) adjacentList.elementAt(startPoint).removeElementAt(index);
        /* decrease the outdegree of startPoint and indegree of endPoint */
        vertex[startPoint].outdegree -= 1;
        vertex[endPoint].indegree -= 1;

        /*
         * if the startPoint is on the right, and its outdegree become zero,
         * mark the startPoint as unmached
         */
        if (isRight(startPoint) && vertex[startPoint].outdegree == 0) vertex[startPoint].matched = false;

        /*
         * if the endPoint is on the left, and its indegree become zero, mark
         * the endPoint as unmached
         */
        if (isLeft(endPoint) && vertex[endPoint].indegree == 0) vertex[endPoint].matched = false;

    }

    /* Change the direction of an edge, e.g. change i -> j to j -> i */
    public void reverseEdge(int startPoint, int endPoint) {
        removeEdge(startPoint, endPoint);
        addedge(endPoint, startPoint);
    }

    /* Reverse all the edges in the augmenting path */
    public String XOR() {
        int start, end;
        String str = "";
        /* the first point of augmenting path */
        start = augmentPath.elementAt(0).intValue();

        for (int i = 1; i < augmentPath.size(); i++)
        {
            end = augmentPath.elementAt(i).intValue();
            reverseEdge(start, end);
            start = end;
        }
        return str;
    }

    /* do the maximum bipartiture matching */
    public String matching() {

        String str = "";
        while (findAugmentPath())
        {
            str += XOR();
        }
        return str;
    }

    public boolean findAugmentPath() {
        augmentPath.removeAllElements(); /* init the path */
        /*
         * use all the unmatched left points as start, see if we can find a
         * augmenting path
         */
        for (int i = 0; i < leftpoints; i++)
        {
            if (vertex[i].matched == false)
            {
                if (findPath(i)) return true;
                else augmentPath.removeAllElements(); /* re init the path */
            }
        }
        return false;
    }

    /* recursive find a path using DFS */
    public boolean findPath(int start) {
        int nextPt, index;
        /* if the current vertex has no out edge, return false */
        if (vertex[start].outdegree == 0) return false;
        /* insert the current point to the path */
        augmentPath.addElement(new Integer(start));

        /*
         * use the pts that the current point is linked to as next point,
         * recursively call findPath function
         */
        for (int i = 0; i < adjacentList.elementAt(start).size(); i++)
        {
            nextPt = adjacentList.elementAt(start).elementAt(i).intValue();
            /* if the next point was already in the path, discard it */
            if (augmentPath.indexOf(new Integer(nextPt)) > -1) continue;
            /* find a terminal, add it to the path and return true */
            if (vertex[nextPt].matched == false)
            {
                augmentPath.addElement(new Integer(nextPt));
                return true;
            }
            /* otherwise recursive call using depth first search */
            else if (findPath(nextPt)) return true;

        }
        /* if failed, delete the current pt from path and return false */
        index = augmentPath.indexOf(new Integer(start));
        augmentPath.removeElementAt(index);
        return false;

    }

    /* indicate whether the current point is in right side */
    public boolean isLeft(int pt) {
        if (pt < leftpoints) return true;
        else return false;
    }

    /* indicate whether the current point is in right side */
    public boolean isRight(int pt) {
        if (pt > leftpoints - 1) return true;
        else return false;
    }

    /* print out the current status of the graph */
    public String toString() {
        String str = "";
        for (int i = 0; i < points; i++)
        {
            str += "Point ";
            str += isLeft(i) == true ? "A" + (i + 1) : "G" + (i - leftpoints + 1);
            str += " is ";
            str += vertex[i].matched == true ? "MATCHED\n" : "UNMATCHED\n";
            for (int j = 0; j < adjacentList.elementAt(i).size(); j++)
            {
                int to = adjacentList.elementAt(i).elementAt(j).intValue();
                str += " and is connected to points ";
                str += isLeft(to) ? "A" + (to + 1) : "G" + (to - leftpoints + 1);
                str += "\n";
            }
        }
        return str;
    }

}
