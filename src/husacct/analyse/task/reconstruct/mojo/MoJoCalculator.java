package husacct.analyse.task.reconstruct.mojo;

import java.io.*;
import java.util.*;

public class MoJoCalculator {

    /* File info */
    private String sourceFile, targetFile, relFile;

    private BufferedReader br_s, br_t, br_r;

    public MoJoCalculator(String sf, String tf, String rf) {
        sourceFile = sf;
        targetFile = tf;
        relFile = rf;
    }

    /* The mapping between objects and clusters in B */
    private Map<String, String> mapObjectClusterInB = new Hashtable<String, String>();

    /* The mappings of clusters to tags in both A and B */
    private Map<String, Integer> mapClusterTagA = new Hashtable<String, Integer>();

    private Map<String, Integer> mapClusterTagB = new Hashtable<String, Integer>();

    /* Mapping between edges and their edgecost */
    private Hashtable<String, Double> tableR = new Hashtable<String, Double>();

    /* use for store the name of each items */
    private Vector<String> clusterNamesInA = new Vector<String>();

    // Stores the number of objects in each cluster in partition B
    // Used in calculating the max distance from partition B
    private Vector<Integer> cardinalitiesInB = new Vector<Integer>();

    /* This vector contains a vector for each cluster in A */
    private Vector<Vector<String>> partitionA = new Vector<Vector<String>>();

    private int l = 0; /* number of clusters in A */

    private int m = 0; /* number of clusters in B */

    private long numberOfObjectsInA;

    private Cluster A[] = null;

    private boolean verbose = false;

    /*
     * record the capacity of each group, if the group is empty ,the count is
     * zero, otherwise >= 1
     */
    private int groupscount[] = null;

    /*
     * after join operations, each group will have only one cluster left, we use
     * grouptags[i] to indicate the remain cluster in group i
     */
    private Cluster grouptags[] = null; /*
                                         * every none empty group have a tag
                                         * point to a cluster in A
                                         */

    public long mojoplus() {

        commonPrep();

        /* tag assigment */
        tagAssignment("MoJoPlus");

        /* draw graph and matching */
        maxbipartiteMatching();

        /* Calculate total cost */
        return calculateCost();
    }

    public double mojofm() {
    	double returnValue = 0;

        commonPrep();

        /* tag assigment */
        tagAssignment("MoJo");

        /* draw graph and matching */
        maxbipartiteMatching();

        /* Calculate MoJoFM value */
        returnValue = mojofmValue(cardinalitiesInB, numberOfObjectsInA, calculateCost());
        return returnValue;
    }

    public double edgemojo() {

        /* In EdgeMoJo mode, we read the relationship file first */
        if (relFile != null) readRelationRSFfile();

        commonPrep();

        /* tag assigment */
        tagAssignment("MoJo");

        /* draw graph and matching */
        maxbipartiteMatching();

        /* Calculate total cost */
        double result = calculateCost();

        /* perform the edgeMoJo */
        result = result + edgeCost();
        return result;
    }

    public void setVerbose(boolean v) {
        verbose = v;
    }

    public long mojo() {

        commonPrep();

        /* tag assigment */
        tagAssignment("MoJo");

        /* draw graph and matching */
        maxbipartiteMatching();

        /* Calculate total cost */
        return calculateCost();
    }

    private void commonPrep() {

        numberOfObjectsInA = 0;

        /* Read target file first to update mapObjectClusterInB */
        if (isBunch(targetFile)) readTargetBunchFile();
        else readTargetRSFFile();

        /* Read source file */
        if (isBunch(sourceFile)) readSourceBunchFile();
        else readSourceRSFfile();

        l = mapClusterTagA.size(); /* number of clusters in A */
        m = mapClusterTagB.size(); /* number of clusters in B */

        A = new Cluster[l]; /* create A */
        groupscount = new int[m]; /* the count of each group, 0 if empty */
        grouptags = new Cluster[m]; /*
                                     * the first cluster in each group, null if
                                     * empty
                                     */

        /* init group tags */
        for (int j = 0; j < m; j++)
        {
            grouptags[j] = null;
        }

        /* create each cluster in A */
        for (int i = 0; i < l; i++)
        {
            A[i] = new Cluster(i, l, m);
        }
    }

    private double edgeCost() {
        /* Perform join operation first */
        for (int j = 0; j < m; j++)
        {
            if (groupscount[j] > 1)
            {
                for (int i = 0; i < l; i++)
                {
                    if (A[i].getGroup() == j)
                    {
                        if (grouptags[j].getNo() != i)
                        {
                            grouptags[j].merge(A[i]);
                        };
                    }
                }
            }
        }
        /* Calculate the additional edge cost */
        double result = 0;
        for (int j = 0; j < m; j++)
        {
            if (grouptags[j] != null) result += grouptags[j].edgeCost(tableR, grouptags, null);
        }
        return result;
    }

    /* public void showSequence() {

        commonPrep();

        System.out.println("Join operations");
        for (int j = 0; j < m; j++)
        {
            if (groupscount[j] > 1)
            {
                for (int i = 0; i < l; i++)
                {
                    if (A[i].getGroup() == j)
                    {
                        if (grouptags[j].getNo() != i)
                        {
                            grouptags[j].merge(A[i]);
                            System.out.println("Join clusters " + clusterNamesInA.elementAt(grouptags[j].getNo())
                                               + " and " + clusterNamesInA.elementAt(i));
                        }
                    }
                }
            }
        }

        System.out.println("Move operations");
        int newClusterIndex = l;
        for (int j = 0; j < m; j++)
        {
            if (grouptags[j] != null)
            {
                for (int i = 0; i < m; i++)
                {
                    if (i != j && grouptags[j].objectList.elementAt(i).size() > 0)
                    {
                        System.out.print("Move " + grouptags[j].objectList.elementAt(i) + " from A" + (grouptags[j].getNo() + 1));

                        if (grouptags[i] != null) // the group is not empty
                        {
                            System.out.println(" to cluster A" + (grouptags[i].getNo() + 1));
                        }
                        else
                        {
                            grouptags[i] = new Cluster(newClusterIndex++, l, m); // create a new group
                            System.out.println(" to newly created cluster A" + (grouptags[i].getNo() + 1) + "(G" + (i + 1) + ")");
                        }
                        grouptags[j].move(i, grouptags[i]);
                    }
                }
            }
        }

    }

*/

    private void maxbipartiteMatching() {
        
        /* Create the graph and add all the edges */
        BipartiteGraph bgraph = new BipartiteGraph(l + m, l, m);

        for (int i = 0; i < l; i++)
        {
            for (int j = 0; j < A[i].groupList.size(); j++)
            {
                bgraph.addedge(i, l + A[i].groupList.elementAt(j).intValue());
            }
        }

        /* Use maximum bipartite matching to calculate the groups */
        bgraph.matching();
        
        /*
         * Assign group after matching, for each Ai in matching, assign the
         * corresponding group, for other cluster in A, just leave them alone
         */
        for (int i = l; i < l + m; i++)
        {
            if (bgraph.vertex[i].matched)
            {
                int index = bgraph.adjacentList.elementAt(i).elementAt(0).intValue();
                A[index].setGroup(i - l);
            }
        }

    }

    /*
     * Calculates the MoJoFM value, using the formula MoJoFM(M) = 1 - mno(A,B)/
     * max(mno(any_A,B)) * 100%
     */
    private double mojofmValue(@SuppressWarnings("rawtypes") Vector number_of_B, long obj_number, long totalCost) {
    	/*
    	 * This method is edited specifically for HUSACCT to prevent that incompatible intended architecture, 
    	 * with one overlapping module (e.g. ExternalSystems, or xLibraries) receive a result of 100%. Due to the 
    	 * following characteristic of MoJo (see ReadME in mojo.jar): "If the two decompositions do not refer to the 
    	 * same set of clustered objects, only the intersection of the two sets will be considered."
    	 */
    	double mojofmValue = 0;
    	long maxDis = maxDistanceTo(number_of_B, obj_number);
        if (totalCost == 0) {
        	if (obj_number > 1) {
            	mojofmValue = Math.rint((1 - (double) totalCost / (double) maxDis) * 10000) / 100;
        	}
        } else {
        	mojofmValue = Math.rint((1 - (double) totalCost / (double) maxDis) * 10000) / 100;
        }
        return mojofmValue;
    }

    /* calculate the max(mno(B, any_A)), which is also the max(mno(any_A, B)) */
    private long maxDistanceTo(@SuppressWarnings("rawtypes") Vector number_of_B, long obj_number) {
        int group_number = 0;
        int[] B = new int[number_of_B.size()];

        for (int i = 0; i < B.length; i++)
        {
            B[i] = ((Integer) number_of_B.elementAt(i)).intValue();
        }
        /* sort the array in ascending order */
        java.util.Arrays.sort(B);

        for (int i = 0; i < B.length; i++)
        {
            /* calculate the minimum maximum possible groups for partition B */
            /*
             * after sort the B_i in ascending order B_i: 1, 2, 3, 4, 5, 6, 7,
             * 8, 10, 10, 10, 15 we can calculate g in this way g: 1, 2, 3, 4,
             * 5, 6, 7, 8, 9, 10, 10, 11
             */
            if (group_number < B[i]) group_number++;
        }
        /* return n - l + l - g = n - g */
        return obj_number - group_number;

    }

    private long calculateCost() {
        int moves = 0; /* total number of move operations */
        int no_of_nonempty_group = 0; /* number of total noneempty groups */
        long totalCost = 0; /* total cost of MoJo */

        /* find none empty groups and find total number of moves */
        for (int i = 0; i < l; i++)
        {
            /* caculate the count of nonempty groups */
            /*
             * when we found that a group was set to empty but in fact is not
             * empty, we increase the number of noneempty group by 1
             */
            if (groupscount[A[i].getGroup()] == 0)
            {
                no_of_nonempty_group += 1;
            }
            /* assign group tags */
            /* if this group has no tag, then we assign A[i] to its tag */
            if (grouptags[A[i].getGroup()] == null)
            {
                grouptags[A[i].getGroup()] = A[i];
            }
            /* assign the group count */
            groupscount[A[i].getGroup()] += 1;
            /* calculate the number of move opts for each cluster */
            moves += A[i].gettotalTags() - A[i].getMaxtag();
        }
        totalCost = moves + l - no_of_nonempty_group;
        return totalCost;
    }

    private void tagAssignment(String mode) {
        for (int i = 0; i < l; i++)
        {
            int tag = -1;
            String clusterName = "";
            for (int j = 0; j < partitionA.elementAt(i).size(); j++)
            {
                String objName = partitionA.elementAt(i).elementAt(j);
                clusterName = mapObjectClusterInB.get(objName);
                tag = mapClusterTagB.get(clusterName).intValue();
                A[i].addobject(tag, objName, mode);
            }
        }
    }

    private void readSourceBunchFile() {
        try
        {
            br_s = new BufferedReader(new FileReader(sourceFile));
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("Could not open " + sourceFile);
        }
        try
        {
            for (String line = br_s.readLine(); line != null; line = br_s.readLine())
            {
                int equalMark = line.indexOf("=");
                String strClusterA = line.substring(0, equalMark).trim();
                String objList = line.substring(equalMark + 1).trim();
                StringTokenizer st = new StringTokenizer(objList, ",");
                int objNumber = st.countTokens();
                numberOfObjectsInA += objNumber;
                int index = mapClusterTagA.size();
                clusterNamesInA.addElement(strClusterA);
                mapClusterTagA.put(strClusterA, new Integer(index));
                partitionA.addElement(new Vector<String>());
                for (int i = 0; i < objNumber; i++)
                {
                    String obj = st.nextToken().trim();
                    partitionA.elementAt(index).addElement(obj);
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read from " + sourceFile);
        }
        try
        {
            br_s.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not close " + sourceFile);
        }
    }

    private void readSourceRSFfile() {
        try
        {
            br_s = new BufferedReader(new FileReader(sourceFile));
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("Could not open " + sourceFile);
        }
        long extraInA = 0;
        try
        {
            for (String line = br_s.readLine(); line != null; line = br_s.readLine())
            {
                StringTokenizer st = new StringTokenizer(line);
                if (st.countTokens() != 3)
                {
                    String message = "Incorrect RSF format in " + sourceFile + " in the following line:\n" + line;
                    throw new RuntimeException(message);
                }
                // Ignore lines that do not start with contain
                if (!st.nextToken().toLowerCase().equals("contain")) continue;

                int index = -1;
                String clusterName = st.nextToken();
                String objectName = st.nextToken();

                if (mapObjectClusterInB.keySet().contains(objectName))
                {
                    numberOfObjectsInA++;
                    Integer objectIndex = mapClusterTagA.get(clusterName);
                    if (objectIndex == null)
                    {
                        index = mapClusterTagA.size();
                        clusterNamesInA.addElement(clusterName);
                        mapClusterTagA.put(clusterName, new Integer(index));
                        partitionA.addElement(new Vector<String>());
                    }
                    else
                    {
                        index = objectIndex.intValue();
                    }
                    partitionA.elementAt(index).addElement(objectName);
                }
                else extraInA++;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read from " + sourceFile);
        }
        try
        {
            br_s.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not close " + sourceFile);
        }
        if (extraInA > 0) put("Warning: " + extraInA + " objects in " + sourceFile + " were not found in " + targetFile + ". They will be ignored.");
        long extraInB = mapObjectClusterInB.keySet().size() - numberOfObjectsInA;
        if (extraInB > 0) put("Warning: " + extraInB + " objects in " + targetFile + " were not found in " + sourceFile + ". They will be ignored.");
    }

    private void readRelationRSFfile() {
        try
        {
            br_r = new BufferedReader(new FileReader(relFile));
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("Could not open " + relFile);
        }
        try
        {
            for (String line = br_r.readLine(); line != null; line = br_r.readLine())
            {
                StringTokenizer st = new StringTokenizer(line);
                if (st.countTokens() != 3)
                {
                    String message = "Incorrect RSF format in " + relFile + " in the following line:\n" + line;
                    throw new RuntimeException(message);
                }
                // The relation name is not used
                st.nextToken();
                String obj1 = st.nextToken();
                String obj2 = st.nextToken();
                /*
                 * we use obj1+"%@$"+obj2 as the key, store it into hash table,
                 * for all kinds of relationship we consider them with same
                 * connection strength
                 */
                /*
                 * for example, if this time we see a call obj1 obj2, we store
                 * obj1+"%@$"+obj2 with value 1, next time we see a ref obj1
                 * obj2, we store obj1+"%@$"+obj2 with value 2
                 */
                if (tableR.get(obj1 + "%@$" + obj2) == null) tableR.put(obj1 + "%@$" + obj2, new Double(1));
                else
                {
                    double previous_value = (tableR.get(obj1 + "%@$" + obj2)).doubleValue();
                    tableR.put(obj1 + "%@$" + obj2, new Double(previous_value + 1));
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read from " + relFile);
        }
        try
        {
            br_r.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not close " + relFile);
        }
    }

    private void readTargetBunchFile() {
        try
        {
            br_t = new BufferedReader(new FileReader(targetFile));
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("Could not open " + targetFile);
        }
        try
        {
            for (String line = br_t.readLine(); line != null; line = br_t.readLine())
            {
                int equalMark = line.indexOf("=");
                String strClusterB = line.substring(0, equalMark).trim();
                String objList = line.substring(equalMark + 1, line.length()).trim();
                StringTokenizer st = new StringTokenizer(objList, ",");
                int objNumber = st.countTokens();

                int index = mapClusterTagB.size();
                cardinalitiesInB.addElement(new Integer(objNumber));
                mapClusterTagB.put(strClusterB, new Integer(index));

                for (int i = 0; i < objNumber; i++)
                {
                    String obj = st.nextToken().trim();
                    mapObjectClusterInB.put(obj, strClusterB);
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read from " + targetFile);
        }
        try
        {
            br_t.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not close " + targetFile);
        }
    }

    private void readTargetRSFFile() {
        try
        {
            br_t = new BufferedReader(new FileReader(targetFile));
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException("Could not open " + targetFile);
        }
        try
        {
            for (String line = br_t.readLine(); line != null; line = br_t.readLine())
            {
                StringTokenizer st = new StringTokenizer(line);
                if (st.countTokens() != 3)
                {
                    String message = "Incorrect RSF format in " + targetFile + " in the following line:\n" + line;
                    throw new RuntimeException(message);
                }
                // Ignore lines that do not start with contain
                if (!st.nextToken().toLowerCase().equals("contain")) continue;

                String clusterName = st.nextToken();
                /* Remove quotes from the token */
                int first_quote_index = clusterName.indexOf("\"");
                if (first_quote_index == 0 && clusterName.indexOf("\"", first_quote_index + 1) == clusterName.length() - 1)
                    clusterName = clusterName.substring(first_quote_index + 1, clusterName.length() - 1);

                String objectName = st.nextToken();
                int index = -1;

                /* Search for the cluster name in mapClusterTagB */
                Integer objectIndex = mapClusterTagB.get(clusterName);

                if (objectIndex == null)
                {
                    // This cluster is not in mapClusterTagB yet
                    index = mapClusterTagB.size();
                    // Since it is a new cluster, it currently contains 1 object
                    cardinalitiesInB.addElement(new Integer(1));
                    mapClusterTagB.put(clusterName, new Integer(index));
                }
                else
                {
                    index = objectIndex.intValue();
                    // Increase the cluster's cardinality in vector
                    // cardinalitiesInB
                    int newCardinality = 1 + cardinalitiesInB.elementAt(index).intValue();
                    cardinalitiesInB.setElementAt(new Integer(newCardinality), index);
                }
                mapObjectClusterInB.put(objectName, clusterName);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read from " + targetFile);
        }
        try
        {
            br_t.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not close " + targetFile);
        }
    }

    private boolean isBunch(String file) {
        int dot = file.lastIndexOf(".");
        if (dot < 0) return false;
        String extend = file.substring(dot + 1).trim();
        if (extend.equalsIgnoreCase("bunch")) return true;
        return false;
    }

    private void put(String message) {
        if (verbose) System.out.println(message);
    }
}
