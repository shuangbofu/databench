package org.example.databench.persistence.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.example.databench.common.domain.file.FileCfg;
import org.example.databench.common.domain.node.NodeCfg;

@MappedJdbcTypes({JdbcType.VARCHAR})
@MappedTypes({FileCfg.class, NodeCfg.class, FileCfg.EmptyFiLeCfg.class})
public class FileCfgJsonHandler extends JsonTypeHandler<FileCfg> {

    public FileCfgJsonHandler() {
        super(FileCfg.class);
    }
}
