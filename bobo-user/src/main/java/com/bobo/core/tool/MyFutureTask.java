package com.bobo.core.tool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyFutureTask extends FutureTask<String> { 
	private static final Logger logger = LoggerFactory.getLogger(MyFutureTask.class);

	public MyFutureTask(Callable<String> callable) {		
		super(callable);	
		} 	
	@Override	
	protected void done() {		
		try {	
			Integer c=0;
			c=c+1;
			logger.info(get() + " 线程执行完毕！~");		
			} catch (InterruptedException | ExecutionException e) {			
				// TODO Auto-generated catch block			
				//e.printStackTrace();	
				logger.error(e.getMessage());
		}	}		
	}



