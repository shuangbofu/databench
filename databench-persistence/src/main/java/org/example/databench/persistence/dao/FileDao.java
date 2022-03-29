package org.example.databench.persistence.dao;

import io.github.shuangbofu.helper.dao.BaseDao;
import org.example.databench.persistence.dao.mapper.FileMapper;
import org.example.databench.persistence.entity.File;

/**
 * Created by shuangbofu on 2021/9/9 3:25 下午
 */
public interface FileDao extends BaseDao<File, FileMapper> {
}
