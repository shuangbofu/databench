package org.example.databench.persistence.dao;

import io.github.shuangbofu.helper.dao.BaseDao;
import org.example.databench.persistence.dao.mapper.FileVersionMapper;
import org.example.databench.persistence.entity.FileVersion;

/**
 * Created by shuangbofu on 2021/9/11 2:33 下午
 */
public interface FileVersionDao extends BaseDao<FileVersion, FileVersionMapper> {
}
