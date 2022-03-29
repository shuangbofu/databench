package org.example.databench.service.domain.node;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shuangbofu on 2021/9/21 10:12 上午
 */
@Data
public class Graph<T extends Graph.INode> {
    private Set<T> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();

    public Graph() {
    }

    public Graph(Set<T> nodes, Set<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public interface INode {
        Long getId();
    }
}
