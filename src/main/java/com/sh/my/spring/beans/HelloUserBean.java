package com.sh.my.spring.beans;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class HelloUserBean {

    private String name;
    private int age;

}
