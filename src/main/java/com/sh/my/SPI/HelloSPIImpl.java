package com.sh.my.SPI;

/**
 * @author sh
 * @since 2025/05/29
 */
public class HelloSPIImpl implements IHelloSPI {
    @Override
    public void sayHello() {
        System.out.println("hello SPI");
    }
}
