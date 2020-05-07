// Dakota Brown

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class cmsc401 {

	private Graph graph;
	private Stack<Node> shortestPath;
	private int[][][] matrix;

	private static Scanner scanner;

	private cmsc401() {}

	public static void main(String... args) {
		cmsc401 i = new cmsc401();

		scanner = new Scanner(System.in);
		int nodes = scanner.nextInt();
		if (scanner.hasNextLine()) {
			scanner.nextLine();
		}

		int edges = scanner.nextInt();
		if (scanner.hasNextLine()) {
			scanner.nextLine();
		}

		i.constructListGraph(nodes, edges);

		System.out.println(i.dijkstra());

		// Begin test code
//		for (String arg : args) {
//			scanner = new Scanner(new File(arg));
//			int nodes = scanner.nextInt();
//			if (scanner.hasNextLine()) {
//				scanner.nextLine();
//			}
//
//			int edges = scanner.nextInt();
//			if (scanner.hasNextLine()) {
//				scanner.nextLine();
//			}
//
//			i.constructListGraph(nodes, edges);
//
//			System.out.println(i.dijkstra());
//
//
//			while (!i.shortestPath.isEmpty()) {
//				Node node = i.shortestPath.pop();
//				System.out.printf("\tVertex: %d\n", node.vertex.label);
//			}
//
//			i.constructMatrixGraph(nodes, edges, arg);
//			i.floydWarshall();
//		}
		// End test code
	}

	private int dijkstra() {
		// no need to initialize a single source graph; we did that already
		PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
		shortestPath = new Stack<>();

		// initialize PriorityQueue with source node
		queue.offer(new Node(null, 0, graph.getVertex(1)));

		int pathCost = 0;
		boolean done = false;
		while (!done && !queue.isEmpty()) {
			Node front = queue.remove();

			if (front.vertex.label == 2) {
				pathCost = front.distance;

				while (front.parent != null) {
					shortestPath.push(front);
					front = front.parent;
				}

				done = true;
			} else {
				for (Graph.Edge edge : front.vertex.edges) {
					Node newNode = new Node(null, Integer.MAX_VALUE, edge.endVertex);

					newNode = relax(front, newNode, edge);
					if (newNode != front) {
						queue.offer(newNode);
					}
				}
			}
		}


		return pathCost;
	}

	private Node relax(Node start, Node end, Graph.Edge edge) {
		if (end.distance > start.distance + end.vertex.weight + edge.weight) {
			end.distance = start.distance + end.vertex.weight + edge.weight;
			end.parent = start;

			return end;
		}

		return start;
	}

	private void constructListGraph(int nodes, int edges) {
		graph = new Graph();

		graph.addVertex(1, 0);
		graph.addVertex(2, 0);

		for (int i = 2; i < nodes; i++) {
			graph.addVertex(scanner.nextInt(), scanner.nextInt());
		}
		if (scanner.hasNextLine()) {
			scanner.nextLine();
		}

		String[] nums = new String[edges];
		for (int i = 0; i < edges; i++) {
			nums[i] = scanner.nextLine();
		}

		Scanner lineScanner;
		for (int i = 0; i < edges; i++) {
			lineScanner = new Scanner(nums[i]);
			int start = lineScanner.nextInt();
			int end = lineScanner.nextInt();
			int weight = lineScanner.nextInt();
			graph.addEdge(start, end, weight);
			graph.addEdge(end, start, weight);
		}
	}

	// Begin test methods
	private void floydWarshall() {
		int dist[][][] = new int[matrix.length][2][];
		for (int m = 0; m < matrix.length; m++) {
			dist[m][0] = matrix[m][0];
			dist[m][1] = matrix[m][1];
		}
		for (int m = 0; m < matrix.length; m++) {
			for (int n = 0; n < matrix.length; n++) {
				if (dist[m][1][n] == 0) {
					dist[m][1][n] = Integer.MAX_VALUE;
				}
			}
		}

		for (int k = 0; k < matrix.length; k++) {
			// Pick all vertices as source one by one
			for (int i = 0; i < matrix.length; i++)	{
				// Pick all vertices as destination for the
				// above picked source
				for (int j = 0; j < matrix.length; j++) {
					// If vertex k is on the shortest path from
					// i to j, then update the value of dist[i][j]
					if (!(dist[i][1][k] == Integer.MAX_VALUE || dist[k][1][j] == Integer.MAX_VALUE)) {
						if (dist[i][1][k] + dist[k][0][0] + dist[k][1][j] + dist[j][0][0] < dist[i][1][j] + dist[j][0][0])
							dist[i][1][j] = dist[i][1][k] + dist[k][0][0] + dist[k][1][j] + dist[j][0][0];
					}
				}
			}
		}

			// Print the shortest distance matrix
			displaySolution(dist);
	}

	private void displaySolution(int D[][][]) {
		for (int i = 0; i < D.length; ++i) {
			for (int j = 0; j < D.length; ++j) {
				if (D[i][1][j] == Integer.MAX_VALUE) {
					System.out.printf("INF\t");
				} else {
					System.out.printf("%d\t", D[i][1][j]);
				}
			}
			System.out.println();
		}
	}

	private void constructMatrixGraph(int nodes, int edges, String fileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		scanner.nextInt();
		if (scanner.hasNextLine()) {
			scanner.nextLine();
		}
		scanner.nextInt();
		if (scanner.hasNextLine()) {
			scanner.nextLine();
		}

		matrix = new int[nodes][2][];
		matrix[0][0] = new int[] {0};
		matrix[0][1] = new int[nodes];
		matrix[1][0] = new int[] {0};
		matrix[1][1] = new int[nodes];

		for (int i = 2; i < nodes; i++) {
			matrix[scanner.nextInt() - 1][0] = new int[] {scanner.nextInt()};
			matrix[i][1] = new int[nodes];
		}
		if (scanner.hasNextLine()) {
			scanner.nextLine();
		}

		String[] nums = new String[edges];
		for (int i = 0; i < edges; i++) {
			nums[i] = scanner.nextLine();
		}

		Scanner lineScanner;
		for (int i = 0; i < edges; i++) {
			lineScanner = new Scanner(nums[i]);
			int start = lineScanner.nextInt();
			int end = lineScanner.nextInt();
			int weight = lineScanner.nextInt();
			matrix[--start][1][--end] = weight;
			matrix[end][1][start] = weight;
		}
	}
	// End test methods

	// Convenience methods/classes for representing our undirected graph
	class Graph {

		private Map<Integer, Vertex> vertices;


		Graph() {
			vertices = new HashMap<>();
		}

		Vertex addVertex(int vertexLabel, int weight) {
			return vertices.put(vertexLabel, new Vertex(vertexLabel, weight));
		}

		Vertex getVertex(int vertexLabel) {
			return vertices.get(vertexLabel);
		}

		Edge addEdge(int startVertexLabel, int endVertexLabel, int weight) {
			Edge edge = new Edge(endVertexLabel, weight);
			return  getVertex(startVertexLabel).addEdge(edge);
		}


		class Vertex {

			private int weight;
			private int label;
			private List<Edge> edges;


			Vertex(int vertexLabel, int vertexWeight) {
				this.label = vertexLabel;
				weight = vertexWeight;
				edges = new ArrayList<>();
			}

			private Edge addEdge(Edge edge) {
				return edges.add(edge) ? edge : null;
			}

			@Override
			public String toString() {
				return String.format("Vertex: %d\n\tWeight: %d", label, weight);
			}
		}

		class Edge {

			private int weight;
			private Vertex endVertex;


			private Edge(int endVertexLabel, int edgeWeight) {
				weight = edgeWeight;
				endVertex = getVertex(endVertexLabel);
			}

			@Override
			public String toString() {
				return String.format("Edge:\n\tWeight: %d\n\tEnd Vertex: %s", weight, endVertex.label);
			}
		}
	}

	// Convenience class for representing shortest-path tree nodes
	class Node {

		private Node parent;
		private int distance;
		private Graph.Vertex vertex;


		Node(Node parent, int distance, Graph.Vertex vertex) {
			this.parent = parent;
			this.distance = distance;
			this.vertex = vertex;
		}
	}
}
