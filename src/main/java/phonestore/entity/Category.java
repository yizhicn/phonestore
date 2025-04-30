package phonestore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {
    private Integer id;
    private Long parentId;
    private String name;
    private Integer status;
    private Integer sortOrder;
    private Integer isParent;
}