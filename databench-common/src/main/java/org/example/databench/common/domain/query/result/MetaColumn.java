package org.example.databench.common.domain.query.result;

import java.io.Serializable;

public class MetaColumn implements Serializable {
    private static final long serialVersionUID = -8082573427320547935L;
    private String name;
    private String className;
    private String typeName;
    private Integer type;
    private String label;
    private Integer displaySize;
    private Integer precision;
    private Integer scale;

    public MetaColumn() {
    }

    public MetaColumn(String name, String className, String typeName, Integer type, String label, Integer displaySize, Integer precision, Integer scale) {
        this.name = name;
        this.className = className;
        this.typeName = typeName;
        this.type = type;
        this.label = label;
        this.displaySize = displaySize;
        this.precision = precision;
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getTypeName() {
        return typeName;
    }

    public Integer getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public Integer getDisplaySize() {
        return displaySize;
    }

    public Integer getPrecision() {
        return precision;
    }

    public Integer getScale() {
        return scale;
    }

    @Override
    public String toString() {
        return "MetaColumn{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", typeName='" + typeName + '\'' +
                ", type=" + type +
                ", label='" + label + '\'' +
                ", displaySize=" + displaySize +
                ", precision=" + precision +
                ", scale=" + scale +
                '}';
    }
}
