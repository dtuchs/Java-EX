package xdean.jex.extra.rx2;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ThreadFactory;

import lombok.extern.slf4j.Slf4j;
import xdean.jex.util.lang.FinalizeSupport;

@Slf4j
public class RxSchedulers {
  public static Scheduler fixedSize(int size) {
    return autoClose(Executors.newFixedThreadPool(size, new ThreadFactory() {
      int i = 0;

      @Override
      public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName("FixedSizeScheduler(size=" + size + ")-" + (++i));
        t.setPriority(Thread.NORM_PRIORITY);
        t.setDaemon(true);
        return t;
      }
    }));
  }

  public static Scheduler forkJoin(int size) {
    class FJWT extends ForkJoinWorkerThread {
      protected FJWT(ForkJoinPool pool) {
        super(pool);
      }
    }
    return autoClose(new ForkJoinPool(size, new ForkJoinWorkerThreadFactory() {
      int i = 0;

      @Override
      public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        ForkJoinWorkerThread t = new FJWT(pool);
        t.setName("ForkJoinScheduler(parallelism=" + size + ")-" + (++i));
        t.setPriority(Thread.NORM_PRIORITY);
        t.setDaemon(true);
        return t;
      }
    }, null, true));
  }

  public static Scheduler autoClose(ExecutorService pool) {
    Scheduler scheduler = Schedulers.from(pool);
    FinalizeSupport.finalize(scheduler, () -> {
      log.debug("Shutdown the scheduler from: " + pool);
      pool.shutdown();
    });
    return scheduler;
  }
}