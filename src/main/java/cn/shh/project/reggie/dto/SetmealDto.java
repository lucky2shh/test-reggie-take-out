package cn.shh.project.reggie.dto;

import cn.shh.project.reggie.pojo.Setmeal;
import cn.shh.project.reggie.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
