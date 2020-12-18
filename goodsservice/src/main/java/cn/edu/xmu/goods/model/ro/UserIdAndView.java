package cn.edu.xmu.goods.model.ro;

import lombok.Getter;

@Getter
public class UserIdAndView {
    private final Long id;
    private final String userName;

    public UserIdAndView(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
