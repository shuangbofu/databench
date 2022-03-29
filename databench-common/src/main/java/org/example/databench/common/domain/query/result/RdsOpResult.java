package org.example.databench.common.domain.query.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RdsOpResult{
    private List<List<Object>> data;
    private List<String> fields;
}
