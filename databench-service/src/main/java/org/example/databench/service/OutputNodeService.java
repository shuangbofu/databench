package org.example.databench.service;

import org.example.databench.persistence.entity.OutputNode;
import org.example.databench.service.base.BaseService;
import org.example.databench.service.domain.vo.OutputNodeVO;

import java.util.List;

/**
 * Created by shuangbofu on 2021/9/20 5:43 下午
 */
public interface OutputNodeService extends BaseService<OutputNode> {
    Long getFileIdByOutputName(String name);

    List<OutputNodeVO> getOutputNodes();
}
