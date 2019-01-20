package threadpool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimplePoolSizeCaculator extends PoolSizeCalculator {

    @Override
    protected Runnable createTask() {
        return new AsyncIOTask();
    }

    @Override
    protected BlockingQueue<Runnable> createWorkQueue(int capacity) {
        return new LinkedBlockingQueue<Runnable>(capacity);
    }

    @Override
    protected long getCurrentThreadCPUTime() {
        //the total CPU time for the current thread in nanoseconds
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    public static void main(String[] args) {
        PoolSizeCalculator poolSizeCalculator = new SimplePoolSizeCaculator();
        poolSizeCalculator.calculateBoundaries(new BigDecimal(1.0), new BigDecimal(100000));
    }

}

/**
 * 自定义的异步IO任务
 * @author Will
 *
 */
class AsyncIOTask implements Runnable {

    @Override
    public void run() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://baidu.com");

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            String line;
            StringBuilder stringBuilder;
            while ((line = reader.readLine()) != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(line);
            }
        }

        catch (IOException e) {

        } finally {
            if(reader != null) {
                try {
                    reader.close();
                }
                catch(Exception e) {

                }
            }
            if (connection != null)
            connection.disconnect();
        }

    }

}