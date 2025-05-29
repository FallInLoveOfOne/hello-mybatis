package com.sh.my.SPI;

import java.util.ServiceLoader;

/**
 * @author sh
 * @since 2025/05/29
 */
public class SPITest {
    public static void main(String[] args) {
        ServiceLoader<IHelloSPI> loader = ServiceLoader.load(IHelloSPI.class);
        for (IHelloSPI service : loader) {
            service.sayHello();
        }
    }
}
