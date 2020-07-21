package com.jjx.boot.common.aop;

import com.jjx.boot.common.pool.ThreadPoolSingle;
import com.jjx.boot.common.util.DateConst;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.zip.GZIPOutputStream;

/**
 * @author Administrator
 */
@Aspect
@Configuration
@EnableScheduling
@SuppressWarnings("unused")
public class TimerAspect {

    private static final String FILE_PATH = "logs";

    private static final String FILE_NAME = "run.log";

    private Logger log = LoggerFactory.getLogger(TimerAspect.class);

    /**
     * 实例日志输出队列
     */
    private BlockingQueue<String> queue = new LinkedBlockingDeque<>(500);

    /**
     * 获取单例线程实例
     */
    private ExecutorService threadPool = ThreadPoolSingle.getSingle().getThreadPool();

    /**
     * 切入全部业务前后，进行性能计算
     *
     * @param joinPoint 代理链
     * @return 函数签名
     */
    @Around("bean(*Service)")
    public Object timer(ProceedingJoinPoint joinPoint) throws Throwable {
        long t1 = System.currentTimeMillis();
        //调用业务函数
        Object obj = joinPoint.proceed();
        long t2 = System.currentTimeMillis();
        Signature s = joinPoint.getSignature();
        String now = DateConst.getDateStr(new Date(), DateConst.DATE_MODEL_2);
        String logStr = now + "：" + s.toString() + "运行耗时：" + (t2 - t1);
        log.info(logStr);
        queue.offer(logStr);
        return obj;
    }

    /**
     * 该函数在本类被spring加载后自动执行
     * 启动该线程，用于输出日志。
     * <p>
     * PostConstruct
     * 本来用于自动执行死循环进行异步的性能检测
     * 但是发现特别占用CPU
     * 故不在死循环调用输出
     * 改用定时任务器
     */
    @Scheduled(cron = "0/30 * *  * * ? ")
    public void start() {
        File dir = new File(FILE_PATH);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                log.error("重新生成日志文件目录");
            }
        }
        File file = new File(FILE_PATH + File.separator + FILE_NAME);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    log.error("重新生成日志文件");
                }
            } catch (IOException e) {
                throw new RuntimeException("创建日志文件异常");
            }
        }
        while (!queue.isEmpty()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, true), true)) {
                String s = queue.take();
                writer.println(s);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(cron = "0 50 23  * * ? ")
    public void stop() {
        File file = new File(FILE_PATH + File.separator + FILE_NAME);
        if (!file.exists()) {
            return;
        }
        String gzFileName = file.getPath() + DateConst.getDateStr(new Date(), DateConst.DATE_MODEL_11) + ".gz";
        try (GZIPOutputStream gz = new GZIPOutputStream(new FileOutputStream(gzFileName));
             FileInputStream fis = new FileInputStream(file)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                gz.write(buf, 0, len);
            }
            //手动关闭文件流
            fis.close();
            log.info("日志备份：" + file.delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
