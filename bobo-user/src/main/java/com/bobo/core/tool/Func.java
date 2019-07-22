package com.bobo.core.tool;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.google.common.collect.Lists;
//@Component
public class Func  {
	@Resource(name = "myTaskExecutor")
	public ThreadPoolTaskExecutor executor;
	
	private static final Logger logger = LoggerFactory.getLogger(Func.class);

	public static int batchSum = 25000;

	public static int sleepTime = 100;

	public static int nThreads = 16;
	

	
	/**
	 * 异步
	 */
	public  <T, R> T asynData(Function<T, R> function, T t) {
	//	final ExecutorService executor = Executors.newCachedThreadPool();
		List<R> list = Lists.newArrayList();
		Callable<String> callable = new Callable<String>() {
			@Override
			public String call() {
				try {
					R r = function.apply(t);
					list.add(r);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;

			}
		};
		MyFutureTask ft = new MyFutureTask(callable);

//		executor.submit(ft);
		executor.submit(ft);


		//executor.shutdown();
		
		return null;
	}

	/**
	 * 多线程for List
	 * 
	 * @param map
	 * @param func
	 */
	public  <T, R> List<R> asynDataForList(Function<T, R> function, List<T> list, int isWait) {
		Long start = System.currentTimeMillis();
//		final ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		int size=list.size();
		final CountDownLatch countDownLatch=new CountDownLatch(size);
		logger.info("List<T> list长度:{}",size);

		List<R> listR = Collections.synchronizedList(Lists.newArrayList());
		for (T t : list) {
			Callable<String> callable = new Callable<String>() {
				@Override
				public String call() {
					try {
						R r = function.apply(t);
						if(r!=null) {
							listR.add(r);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
			            countDownLatch.countDown();
			         }
					return null;

				}
			};
			MyFutureTask ft = new MyFutureTask(callable);
			executor.submit(ft);
		}
		/*executor.shutdown();
		if (isWait == 1) {
			waitResult(executor);
		}*/
		
		if(isWait==1) {
			waitResult( countDownLatch);
		}
		Long end = System.currentTimeMillis();
		logger.info("==================多线程共耗时" + (end - start) + "=================={}");
		return listR;
	}

	/**
	 * 多线程for subList
	 * 
	 * @param map
	 * @param func
	 */
	public static <T, R> List<R> asynDataForSubList(Function<List<T>, R> function, List<T> list, int isWait) {
		Long start = System.currentTimeMillis();
		List<R> listR = Collections.synchronizedList(Lists.newArrayList());
		int sizeList = list.size();
		int num = sizeList / batchSum + 1;
		if (num > nThreads) {
			num = nThreads;
		}
		final ExecutorService executor = Executors.newFixedThreadPool(num);
		for (int i = 0; i < num; i++) {
			int size = (i + 1) * batchSum;
			if (size > sizeList)
				size = sizeList;
			List<T> sublist = list.subList(i * batchSum, size);
			executor.submit(new Callable<Object>() {
				@Override
				public String call() {
					try {
						R r = function.apply(sublist);
						listR.add(r);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			});

		}
		executor.shutdown();
		if (isWait == 1) {
			waitResult(executor);
		}

		Long end = System.currentTimeMillis();
		logger.info("==================多线程共耗时" + (end - start) + "=================={}");
		return listR;
	}

	/**
	 * 多线程for Map
	 * 
	 * @param map
	 * @param func
	 */
	public  <T, U, R> List<R> asynDataForMap(BiFunction<T, U, R> function, Map<T, U> map, int isWait) {
		Long start = System.currentTimeMillis();
		List<R> listR = Collections.synchronizedList(Lists.newArrayList());
		if (ObjectUtils.isEmpty(map)) {
			return listR;
		}
		int size=map.size();
		final CountDownLatch countDownLatch=new CountDownLatch(size);
		logger.info("Map<T, U> map list长度:{}",size);

		//final ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		map.forEach((key, value) -> {
			Callable<String> callable = new Callable<String>() {
				@Override
				public String call() {
					try {
						R r = function.apply(key, value);
						listR.add(r);
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
			            countDownLatch.countDown();
			         }
					return null;

				}
			};
			MyFutureTask ft = new MyFutureTask(callable);
			//executor.submit(ft);
			executor.submit(ft);

		});
		/*executor.shutdown();
		if (isWait == 1) {
			waitResult(executor);
		}*/
		if(isWait==1) {
			waitResult( countDownLatch);
		}
		Long end = System.currentTimeMillis();
		logger.info("==================多线程共耗时" + (end - start) + "=================={}");
		return listR;
	}

	public static void waitResult(ExecutorService executor) {
		
		while (true) {
			if (executor.isTerminated()) {
				System.out.println("都结束了！");
				break;
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	public  void waitResult(CountDownLatch countDownLatch) {
		
		//等待子线程全部结束
		   try {
			 logger.info("countDownLatch长度:{}",countDownLatch.getCount());
//			countDownLatch.await();
			countDownLatch.await(4, TimeUnit.MINUTES);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
