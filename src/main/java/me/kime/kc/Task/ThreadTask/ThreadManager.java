package me.Kime.KC.Task.ThreadTask;

import java.util.LinkedList;
import me.kime.Threadpool.ThreadPool;

public class ThreadManager extends Thread{

	private ThreadPool pool;
	private LinkedList<TTask> taskList;

	public ThreadManager(ThreadPool pool){
		this.pool = pool;
		taskList = new LinkedList<TTask>();
	}
	
	public void registerTask(TTask task){
		taskList.add(task);
	}
	
	@Override
	public void run() {
		boolean hasTask = false;
		while(true){
			synchronized(taskList){
				hasTask = false;
				for(TTask task: taskList){
					int taskSize = task.queueSize();
					for(int i = 0; i < taskSize; i++){
						//hasTask = true;
						pool.addTask(task);
					}
				}
			}
			if(!hasTask){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}			
		}
	}
	
	public boolean isDone(){
		synchronized(taskList){
			for(TTask task: taskList){
				if(task.queueSize() > 0){
					return false;
				}
			}
			return true;
		}
	}
	
}
