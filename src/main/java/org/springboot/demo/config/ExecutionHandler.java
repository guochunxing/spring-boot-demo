package org.springboot.demo.config;

import java.util.concurrent.ThreadPoolExecutor;

public class ExecutionHandler extends ThreadPoolExecutor.CallerRunsPolicy {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        super.rejectedExecution(r, e);
        System.out.println(e.toString());
        System.out.println("多出线程");
    }
}
