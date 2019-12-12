package com.drp.shield.view;

import cn.hutool.core.io.IoUtil;
import com.drp.shield.config.SecurityConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month06day  16:27:27
 */
@Component
public class AssetLoader {

    private final SecurityConfig env;

    @Getter
    private byte[] faviconBytes;

    @Autowired
    public AssetLoader(SecurityConfig env) {
        this.env = env;
    }

    @PostConstruct
    public void init() throws IOException {
        final InputStream ins = this.getImageFile(env.getFaviconPath());
        faviconBytes = IoUtil.readBytes(ins);
    }

    private InputStream getImageFile(String path) throws IOException {
        return new ClassPathResource(path).getInputStream();
    }

}
