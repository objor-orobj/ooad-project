package cn.edu.xmu.goods.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-08 17:37
 */
@Data
public class CommentConfirmVo {
    @NotNull
    private Boolean conclusion;
}
