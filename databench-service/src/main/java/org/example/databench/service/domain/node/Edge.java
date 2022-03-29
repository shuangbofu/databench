package org.example.databench.service.domain.node;

import lombok.Data;

import java.util.Objects;

@Data
public class Edge {
    private Long source;
    private Long target;

    public Edge() {
    }

    public Edge(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return Objects.equals(source, edge.source) && Objects.equals(target, edge.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
