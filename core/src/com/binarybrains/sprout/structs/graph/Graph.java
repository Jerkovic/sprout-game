package com.binarybrains.sprout.structs.graph;

import java.util.*;

public class Graph {
    private Map<Vertex, List<Vertex>> adjVertices;

    public Graph() {
        this.adjVertices = new HashMap<>();
    }

    public void addEdge(String label1, String label2) {
        Vertex v1 = new Vertex(label1, this);
        Vertex v2 = new Vertex(label2, this);
        adjVertices.get(v1).add(v2);
        adjVertices.get(v2).add(v1);
    }

    public void removeEdge(String label1, String label2) {
        Vertex v1 = new Vertex(label1, this);
        Vertex v2 = new Vertex(label2, this);
        List<Vertex> eV1 = adjVertices.get(v1);
        List<Vertex> eV2 = adjVertices.get(v2);
        if (eV1 != null)
            eV1.remove(v2);
        if (eV2 != null)
            eV2.remove(v1);
    }

    public void removeVertex(String label) {
        Vertex v = new Vertex(label, this);
        adjVertices.values().stream().forEach(e -> e.remove(v));
        adjVertices.remove(new Vertex(label, this));
    }

    public void addVertex(String label) {
        adjVertices.putIfAbsent(new Vertex(label, this), new ArrayList<>());
    }

    public List<Vertex> getAdjVertices(String label) {
        return adjVertices.get(new Vertex(label, this));
    }

    public String printGraph() {
        StringBuffer sb = new StringBuffer();
        for(Vertex v : adjVertices.keySet()) {
            sb.append(v);
            sb.append(adjVertices.get(v));
        }
        return sb.toString();
    }

}
