package com.sh.my.spring.mvc.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class HelloVO {
    private String name;
    private Long age;
    private BigDecimal price;
    private Boolean flag;
    private Date date;
    private LocalDate localDate;
    private LocalDateTime dateTime;
    private List<String> list;
    private Integer[] arr;
}
