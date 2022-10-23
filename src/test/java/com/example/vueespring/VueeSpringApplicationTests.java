package com.example.vueespring;

import cn.hutool.crypto.SecureUtil;
import com.vueespring.Scheduler.ThingScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VueeSpringApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void md5test(){
    }
    @Test
    void scheduler() throws Exception {
        ThingScheduler th = new ThingScheduler();
        th.init("cyk");
    }

}
