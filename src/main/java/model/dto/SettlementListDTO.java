package model.dto;
import java.math.BigDecimal;

public class SettlementListDTO {
    private int listId;
    private int groupId;
    private String month;
    private String itemName;
    private int payerId;
    private BigDecimal listPrice;
    
    public SettlementListDTO() {}
    
    public SettlementListDTO(int listId, int groupId, String month, String itemName, int payerId, BigDecimal listPrice) {
        super();
        this.listId = listId;
        this.groupId = groupId;
        this.month = month;
        this.itemName = itemName;
        this.payerId = payerId;
        this.listPrice = listPrice;
    }
    
    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }
    
    @Override
    public String toString() {
        return "SettlementListDTO [listId=" + listId + ", groupId=" + groupId + ", month=" + month + ", itemName="
                + itemName + ", payerId=" + payerId + ", listPrice=" + listPrice + "]";
    }
}
