
package com.cj.mobile.common.domain.executor;

import rx.Scheduler;


public interface PostExecutionThread {
  Scheduler getScheduler();
}
