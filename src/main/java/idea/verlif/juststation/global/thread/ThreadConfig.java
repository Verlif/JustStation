package idea.verlif.juststation.global.thread;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.annotation.NonNull;

import java.util.concurrent.*;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/21 11:12
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "station.thread")
public class ThreadConfig {

    /**
     * 核心线程数
     */
    private int coreSize = 4;

    /**
     * 最大线程数
     */
    private int maxSize = 20;

    /**
     * 线程存活时间（单位：秒）
     */
    private long alive = 60000;

    /**
     * 允许线程超时
     */
    private boolean allowTimeout = true;

    @Bean
    @ConditionalOnMissingBean(ThreadPoolExecutor.class)
    public ThreadPoolExecutor threadPoolExecutor(
            @Autowired ThreadFactory factory) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                coreSize, maxSize,
                alive, TimeUnit.SECONDS,
                blockingQueue(), factory);
        executor.allowCoreThreadTimeOut(allowTimeout);
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(ThreadFactory.class)
    public ThreadFactory threadFactory() {
        return new DefaultThreadFactory();
    }

    public BlockingQueue<Runnable> blockingQueue() {
        return new LinkedBlockingQueue<>(maxSize / 2);
    }

    private static final class DefaultThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r);
        }
    }
}
