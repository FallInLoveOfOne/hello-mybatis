package com.sh.my.datastructure;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sh
 * @since 2025/05/09
 */
@Slf4j
public class HelloArray {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        for (int i = 0; i < arr.length; i++) {
            log.info("arr[{}] = {}", i, arr[i]);
        }
    }
}
