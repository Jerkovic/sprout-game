package com.binarybrains.sprout.structs.graph;

public class Vertex {
    String label;
    Graph graph;

    public Vertex(String label, Graph graph) {
        this.label = label;
        this.graph = graph;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getOuterType().hashCode();
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (!getOuterType().equals(other.getOuterType()))
            return false;
        if (label == null) {
            return other.label == null;
        } else return label.equals(other.label);
    }

    private Graph getOuterType() {
        return this.graph;
    }

    @Override
    public String toString() {
        return label;
    }

}
