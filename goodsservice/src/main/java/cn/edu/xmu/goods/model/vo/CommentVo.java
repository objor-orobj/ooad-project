package cn.edu.xmu.goods.model.vo;


import cn.edu.xmu.goods.model.bo.Comment;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @pragram:oomall
 * @description:
 * @author:JMDZWT
 * @create:2020-12-03 22:59
 */
@Data
public class CommentVo {
    //@NotNull(message="type不得为空")
    private Comment.Type type;

    //@NotBlank(message="content不得为空")
    private String content;

    public Comment createComment(){
        Comment comment=new Comment();
        comment.setContent(this.content);
        comment.setType(this.type);
        comment.setGmtCreate(LocalDateTime.now());
        return comment;
    }

}
