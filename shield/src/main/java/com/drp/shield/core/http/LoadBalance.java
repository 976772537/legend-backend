package com.drp.shield.core.http;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month16day  09:47:04
 */
public class LoadBalance {
    private static AtomicInteger count= new AtomicInteger(0);

    public static String random(List<String> destinations) {
        final Random random = new Random();
        return destinations.get(random.nextInt(destinations.size()));
    }

    public static String average(List<String> destinations) {
        if(count.get() > destinations.size()) {
            count.set(0);
        }
        return destinations.get(count.getAndAdd(1));
    }
}
