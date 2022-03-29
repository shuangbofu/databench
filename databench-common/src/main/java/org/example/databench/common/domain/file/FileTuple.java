package org.example.databench.common.domain.file;

import lombok.Data;

/**
 * Created by shuangbofu on 2021/9/11 2:47 下午
 */
@Data
public class FileTuple {
    private FileBase content;
    private FileCfg cfg;

    public FileTuple(FileBase content, FileCfg cfg) {
        this.content = content;
        this.cfg = cfg;
    }

    public FileTuple() {
    }
}
