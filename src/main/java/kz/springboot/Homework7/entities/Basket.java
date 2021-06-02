package kz.springboot.Homework7.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class Basket implements Comparable<Basket>{
    private Items items;
    private int quantity = 0;

    @Override
    public int compareTo(Basket basket) {
        if (basket.getItems().getId().equals(this.items.getId())){
            return 1;
        }
        return 0;
    }
}