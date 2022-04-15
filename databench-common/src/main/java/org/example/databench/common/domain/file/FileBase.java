package org.example.databench.common.domain.file;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.example.databench.common.domain.node.NodeContent;
import org.example.databench.common.domain.resource.FunctionContent;
import org.example.databench.common.domain.resource.ResourceContent;
import org.example.databench.common.enums.FileCategory;
import org.example.databench.common.enums.FileType;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY;

/**
 * Created by shuangbofu on 2021/9/11 2:46 下午
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = EXTERNAL_PROPERTY, property = "contentType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NodeContent.class, name = "node"),
        @JsonSubTypes.Type(value = FileContent.class, name = "default"),
        @JsonSubTypes.Type(value = ResourceContent.class, name = "resource"),
        @JsonSubTypes.Type(value = FunctionContent.class, name = "function")
})
@Data
public abstract class FileBase {
    private String name;
    private Long createTime;
    private Long updateTime;
    private String createBy;
    private String modifiedBy;
    private Integer version;
    private String fileType;
    private String description;

    public FileCategory getCategory() {
        // FIXME
        return FileType.valueOf(fileType).getCategory();
    }
}
