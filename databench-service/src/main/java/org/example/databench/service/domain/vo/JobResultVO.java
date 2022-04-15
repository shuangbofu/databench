package org.example.databench.service.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.executor.api.domain.query.QueryResult;

/**
 * Created by shuangbofu on 2022/3/30 21:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JobResultVO {
    private QueryResult queryResult;
    private String jobId;
}
