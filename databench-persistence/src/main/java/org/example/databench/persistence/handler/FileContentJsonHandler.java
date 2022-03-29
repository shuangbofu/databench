package org.example.databench.persistence.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.example.databench.common.domain.file.FileBase;

@MappedJdbcTypes({JdbcType.VARCHAR})
@MappedTypes({FileBase.class})
public class FileContentJsonHandler extends JsonTypeHandler<FileBase> {

    public FileContentJsonHandler() {
        super(FileBase.class);
    }
}
