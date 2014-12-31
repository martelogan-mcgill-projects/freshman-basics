import java.util.*;
import java.io.*;

// This class implements a google-like search engine
public class searchEngine {

	public HashMap<String, LinkedList<String>> wordIndex; // this will contain a
															// set of pairs
															// (String,
															// LinkedList of
															// Strings)
	public directedGraph internet; // this is our internet graph

	// Constructor initializes everything to empty data structures
	// It also sets the location of the internet files
	searchEngine() {
		// Below is the directory that contains all the internet files
		htmlParsing.internetFilesLocation = "internetFiles";
		wordIndex = new HashMap<String, LinkedList<String>>();
		internet = new directedGraph();
	} // end of constructor2014

	// Returns a String description of a searchEngine
	public String toString() {
		return "wordIndex:\n" + wordIndex + "\ninternet:\n" + internet;
	}

	// This does a graph traversal of the internet, starting at the given url.
	// For each new vertex seen, it updates the wordIndex, the internet graph,
	// and the set of visited vertices.

	void traverseInternet(String url) throws Exception {
		/* WRITE SOME CODE HERE */

		/*
		 * Hints 0) This should take about 50-70 lines of code (or less) 1) To
		 * parse the content of the url, call htmlParsing.getContent(url), which
		 * returns a LinkedList of Strings containing all the words at the given
		 * url. Also call htmlParsing.getLinks(url). and assign their results to
		 * a LinkedList of Strings. 2) To iterate over all elements of a
		 * LinkedList, use an Iterator, as described in the text of the
		 * assignment 3) Refer to the description of the LinkedList methods at
		 * http://docs.oracle.com/javase/6/docs/api/ . You will most likely need
		 * to use the methods contains(String s), addLast(String s), iterator()
		 * 4) Refer to the description of the HashMap methods at
		 * http://docs.oracle.com/javase/6/docs/api/ . You will most likely need
		 * to use the methods containsKey(String s), get(String s), put(String
		 * s, LinkedList l).
		 */

		// add new vertex for url and set url to visited
		internet.addVertex(url);
		internet.setVisited(url, true);

		// store words in url address to Linked List "content"
		LinkedList<String> content = htmlParsing.getContent(url);

		// store links at url to Linked List "neighbours"
		LinkedList<String> neighbours = htmlParsing.getLinks(url);

		// declare iterator
		Iterator<String> i;

		// set iterator to iterate over content
		i = content.iterator();

		// iterate through words in url address
		while (i.hasNext()) {
			String s = i.next();

			// if this is a new word
			if (!wordIndex.containsKey(s)) {
				// create linked list for word and add url to list
				LinkedList<String> l = new LinkedList<String>();
				l.addLast(url);
				// add linked list to wordIndex
				wordIndex.put(s, l);
			} else { // word is indexed in hashMap

				// if url is not found in word's list
				if (!(wordIndex.get(s)).contains(url)) {

					// add url to list
					(wordIndex.get(s)).addLast(url);
				}
			}
		}

		// set iterator to iterate over neighbours
		i = neighbours.iterator();

		// iterate through links at url
		while (i.hasNext()) {
			String s = i.next();

			// add edge to each neighbour
			internet.addEdge(url, s);

			// if neighbour is unvisited
			if (!internet.getVisited(s)) { // traverse internet from s
				traverseInternet(s);
			}
		}

	} // end of traverseInternet

	/*
	 * This computes the pageRanks for every vertex in the internet graph. It
	 * will only be called after the internet graph has been constructed using
	 * traverseInternet. Use the iterative procedure described in the text of
	 * the assignment to compute the pageRanks for every vertices in the graph.
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	void computePageRanks() {
		/* WRITE YOUR CODE HERE */

		// initialize PR(v) = 1 for all vertices v
		LinkedList<String> v = internet.getVertices();
		Iterator<String> i = v.iterator();
		while (i.hasNext()) {
			internet.setPageRank(i.next(), 1);
		}

		// run pageRank computation until convergence
		for (int test = 0; test < 100; test++) {

			// compute pageRank for every site on the internet
			i = v.iterator();
			while (i.hasNext()) {

				String s = i.next();

				// initialize site's pageRank to the constant 0.5
				double cur_pr = 0.5;

				// iterate over sites linking to current site
				Iterator<String> references = internet.getEdgesInto(s)
						.iterator();
				while (references.hasNext()) {
					String t = references.next();
					// compute the page rank of each reference
					double temp_pr = (internet.getPageRank(t) / internet
							.getOutDegree(t));
					// increment site's page rank by 0.5 * reference's page rank
					cur_pr += 0.5 * temp_pr;
				}
				// set pageRank of current site
				internet.setPageRank(s, cur_pr);

			}
		}
	}// end of computePageRanks

	/*
	 * Returns the URL of the page with the high page-rank containing the query
	 * word Returns the String "" if no web site contains the query. This method
	 * can only be called after the computePageRanks method has been executed.
	 * Start by obtaining the list of URLs containing the query word. Then
	 * return the URL with the highest pageRank. This method should take about
	 * 25 lines of code.
	 */
	String getBestURL(String query) {
		/* WRITE YOUR CODE HERE */

		// declare linked list for search results
		LinkedList<String> results;

		// find results as sites containing our query in url
		query = query.toLowerCase();
		if (wordIndex.containsKey(query))
			results = wordIndex.get(query);
		else {
			String s = "";
			return s;
		}

		// prepare to iterate over search results
		Iterator<String> i = results.iterator();
		String best = "";
		double bestPR = -1;

		// use linear search to find max pageRank
		while (i.hasNext()) {
			String s = i.next();

			// get pageRank of each site
			double pr = internet.getPageRank(s);

			// get max pageRank so far
			if (pr > bestPR) {
				bestPR = pr;
				best = s;
			}
		}
		return best;
	} // end of getBestURL

	public static void main(String args[]) throws Exception {
		searchEngine mySearchEngine = new searchEngine();
		// to debug your program, start with.
		// mySearchEngine.traverseInternet("http://www.cs.mcgill.ca/~blanchem/250/a.html");

		// When your program is working on the small example, move on to
		mySearchEngine.traverseInternet("http://www.cs.mcgill.ca");

		// this is just for debugging purposes. REMOVE THIS BEFORE SUBMITTING
		// System.out.println(mySearchEngine);

		mySearchEngine.computePageRanks();

		BufferedReader stndin = new BufferedReader(new InputStreamReader(
				System.in));
		String query;
		do {
			System.out.print("Enter query: ");
			query = stndin.readLine();
			if (query != null && query.length() > 0) {
				System.out.println("Best site = "
						+ mySearchEngine.getBestURL(query));
			}
		} while (query != null && query.length() > 0);
	} // end of main
}