package org.example.databench.common.domain.query.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SqlResult implements Serializable {
    private static final long serialVersionUID = -2061289289393503703L;
    private ResultMeta resultMeta;
    private List<List<Object>> data = new ArrayList<>();

    public SqlResult(ResultMeta resultMeta, List<List<Object>> data) {
        this.resultMeta = resultMeta;
        this.data = data;
    }

    public SqlResult() {
    }

    public ResultMeta getResultMeta() {
        return resultMeta;
    }

    public void setResultMeta(ResultMeta resultMeta) {
        this.resultMeta = resultMeta;
    }

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SqlResult{" +
                "resultMeta=" + resultMeta +
                ", data=" + data +
                '}';
    }
}
