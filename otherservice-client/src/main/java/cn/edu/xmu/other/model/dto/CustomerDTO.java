package cn.edu.xmu.other.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerDTO implements Serializable {
    private Long id;
    private String userName;
    private String name;
}
