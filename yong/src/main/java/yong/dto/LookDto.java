package yong.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LookDto {

    private String slotName;
    private String itemId;
    private String itemName;
    private String unitPrice;
    private String averagePrice;
    private boolean SoldItem;
}
