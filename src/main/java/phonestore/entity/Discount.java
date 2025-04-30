package phonestore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discount extends BaseEntity {
    private Integer id;
    private String name;
    private Integer quantity;
    private Date expiryDate;
    private Integer mode; // 0: Fixed amount, 1: Percentage
    private Long minAmount;
    private Long discountAmount;
    private Integer discountPercentage;
}