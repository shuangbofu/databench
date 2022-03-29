package org.example.databench.common.domain.query.result;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ResultMeta implements Serializable {
    private static final long serialVersionUID = -3164193267778417877L;
    private final List<MetaColumn> metaColumns = new LinkedList<>();
    private String schemeName;
    private String catalogName;

    public ResultMeta() {
    }

    public ResultMeta(String schemeName, String catalogName) {
        this.schemeName = schemeName;
        this.catalogName = catalogName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public List<MetaColumn> getMetaColumns() {
        return metaColumns;
    }

    @Override
    public String toString() {
        return "ResultMeta{" +
                "metaColumns=" + metaColumns +
                ", schemeName='" + schemeName + '\'' +
                ", catalogName='" + catalogName + '\'' +
                '}';
    }
}
