package org.example.executor.api.domain.query;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2022/3/29 21:22
 */
@Data
@AllArgsConstructor
public class QueryResult {
    private List<List<Object>> data;
    private List<String> columns;

    public QueryResult(List<Map<String, Object>> result) {
        data = result.stream().map(i -> Lists.newArrayList(i.values())).collect(Collectors.toList());
        columns = result.size() == 0 ? Lists.newArrayList() :
                Lists.newArrayList(result.get(0).keySet());
    }

    public QueryResult() {
        data = new ArrayList<>();
        columns = new ArrayList<>();
    }
}
