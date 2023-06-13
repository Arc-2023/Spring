package com.vueespring.utils;

import cn.hutool.core.io.IoUtil;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static cn.hutool.core.lang.Console.log;

@Component
@Slf4j
public class CurrencyLimiter {
    @Autowired
    MinioClient minioClient;
    private static final Semaphore semaphore = new Semaphore(5, true);
    static ReentrantLock lock = new ReentrantLock(true);
    static AtomicInteger limit = new AtomicInteger(0);
    static ExecutorService es = Executors.newFixedThreadPool(3);

    public Future<?> take(GetObjectArgs args, HttpServletResponse res) {
        return es.submit(() -> {
            try {
                log.info(String.valueOf(limit.incrementAndGet()));
                args.getHeaders();
                doDownLoad(args, res);
                log.info(String.valueOf(limit.decrementAndGet()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void doDownLoad(GetObjectArgs args, HttpServletResponse res) throws IOException, ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException {
        minioClient.getObject(args).transferTo(res.getOutputStream());
//        try (GetObjectResponse object = minioClient.getObject(args)) {
//            IoUtil.copy(object, res.getOutputStream());
//        }
    }
}
