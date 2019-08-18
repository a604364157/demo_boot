package com.jjx.boot.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author jiangjx
 */
@Component("timeLimit")
public class TimeLimit {

    private Logger log = LoggerFactory.getLogger(TimeLimit.class);
    private WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

    private ThreadPoolTaskScheduler scheduler = ThreadPoolSingle.getSingle().getScheduler();
    private ExecutorService executorService = ThreadPoolSingle.getSingle().getThreadPool();
    private ScheduledExecutorService executor = ThreadPoolSingle.getSingle().getService();

    /**
     * 本函数会将传入函数作为一个独立的线程运行
     * 并指定运行时间，如果函数不能在指定时间内
     * 返回正常运行的结果，则返回null
     * 如果在指定时间内返回结果则正常返回
     *
     * @param beanName   函数所定义的组件名称
     * @param mthName    函数名称
     * @param paramTypes 函数所需要的参数类型
     * @param params     函数所需要的参数
     * @param time       指定运行时间 单位 秒
     * @return 函数返回结果
     */
    public Object callMth4LT(final String beanName, final String mthName, final Class<?> paramTypes, final Object params, int time) {
        //创建任务
        FutureTask<Object> future = new FutureTask<>(() -> {
            Object obj = wac.getBean(beanName);
            Method method = obj.getClass().getDeclaredMethod(mthName, paramTypes);
            return method.invoke(obj, params);
        });
        //将任务交给线程池
        executorService.execute(future);
        //从任务中获取执行结果
        Object result = null;
        try {
            //这里的时间是最大运行时间，如果任务在最大运行时间
            //前就完成了，这里也会提前返回
            //如果超时，则会抛出TimeoutException异常
            result = future.get(time, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            future.cancel(true);
            log.error("[" + mthName + "]函数执行过程中异常中断");
        } catch (ExecutionException e) {
            future.cancel(true);
            log.error("[" + mthName + "]函数执行过程中出现程序异常");
            e.printStackTrace();
        } catch (TimeoutException e) {
            future.cancel(true);
            log.error("[" + mthName + "]函数执行时间超过设置时间");
        }
        return result;
    }

    /**
     * 本函数会将传入函数按照指定时间模式定时运行
     * 最好能够使用注解Scheduled(cron="0/5 * *  * * ? ")代替本函数
     * 使用定时任务注解必须先在配置中启用，目前项目配置未启用
     *
     * @param beanName   函数所定义的组件名称
     * @param mthName    函数名称
     * @param paramTypes 函数的参数类型
     * @param params     函数的参数
     * @param timeSet    定时运行的时间定义（请自行查阅CronTrigger的用法进行配置）
     */
    public void addJob(final String beanName, final String mthName, final Class<?> paramTypes, final Object params, String timeSet) {
        scheduler.schedule(() -> {
            try {
                //            Object obj = wac.getBean(beanName);
                Object obj = Class.forName(beanName);
                Method method = obj.getClass().getDeclaredMethod(mthName, paramTypes);
                method.invoke(obj, params);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("需要定时执行的函数不存在");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("需要定时执行的函数在调用或运行中出现异常");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("指定的类或组件不存在");
            }
        }, new CronTrigger(timeSet));
    }

    /**
     * 指定运行时间测试函数
     *
     * @param a a
     * @return map
     * @throws InterruptedException error
     */
    public Map<String, Object> test1(String a) throws InterruptedException {
        Map<String, Object> param = new HashMap<>(16);
        param.put("A", "1");
        param.put("B", "2");
        param.put("C", "3");
        Thread.sleep(1000);
        return param;
    }

    /**
     * Java自带的ScheduledExecutorService
     * 定时执行测试函数
     */
    public void test2() {
        executor.scheduleAtFixedRate(() -> System.out.println(System.currentTimeMillis()), 0, 2, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(() -> System.out.println(new Date().toString()), 0, 2, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(() -> System.out.println(new Timestamp(System.currentTimeMillis()).toString()), 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Spring提供的定时任务测试函数
     */
    private void test3() {
        scheduler.schedule(() -> System.out.println(System.currentTimeMillis()), new CronTrigger("0/2 * *  * * ? "));
        scheduler.schedule(() -> System.out.println(new Date().toString()), new CronTrigger("0/2 * *  * * ? "));
    }

    public void test4() {
        System.out.println(new Date().toString());
    }

    private int a = 10000;

    private synchronized int sub() {
        return a--;
    }

    private void test5() {
        executorService.execute(() -> test6("A"));
        executorService.execute(() -> test6("B"));
        executorService.execute(() -> test6("C"));
        executorService.execute(() -> test6("D"));
        executorService.execute(() -> test6("E"));
        executorService.execute(() -> test6("F"));
        executorService.execute(() -> test6("G"));
    }

    private void test6(String flag) {
        while (a != 0) {
            System.out.println(flag + ":" + sub());
        }
    }

    private void test7() {
        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        String[] king = {"K", "k"};
        List<String> all = new ArrayList<>();
        all.addAll(test8(arr, "A"));
        all.addAll(test8(arr, "B"));
        all.addAll(test8(arr, "C"));
        all.addAll(test8(arr, "D"));
        all.addAll(Arrays.asList(king));
        Collections.shuffle(all);
        List<String> a = all.subList(0, 17);
        test9(a);
        List<String> b = all.subList(17, 34);
        test9(b);
        List<String> c = all.subList(34, 54);
        test9(c);
        System.out.println(a.toString());
        System.out.println(b.toString());
        System.out.println(c.toString());
    }

    private List<String> test8(String[] arr, String flag) {
        List<String> list = new ArrayList<>();
        for (String s : arr) {
            list.add(flag + s);
        }
        return list;
    }

    private void test9(List<String> list) {
        list.sort((o1, o2) -> {
            if ("k".equalsIgnoreCase(o1)) {
                return 1;
            }
            if ("k".equalsIgnoreCase(o2)) {
                return -1;
            }
            String ot1 = o1.substring(1);
            String ot2 = o2.substring(1);
            return Integer.valueOf(ot1) - Integer.valueOf(ot2);
        });
    }

    public static void main(String[] args) {
        TimeLimit t = new TimeLimit();
        t.test7();
    }

}

