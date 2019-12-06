package com.drp.shield.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author dongruipeng
 * @Descrpition The currently running code is in the same Spring context as the started Spring code
 * @date 2019year 12month05day  23:18:50
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContextUtils.context = context;
    }

    // 传入线程中
    public static <T> T getBean(Class beanClass) {
        return (T) context.getBean(beanClass);
    }

    // 国际化使用
    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

    /// 获取当前环境
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

    public static boolean isDev() {
        String env = context.getEnvironment().getActiveProfiles()[0];
        return env.equals("dev");
    }
}
