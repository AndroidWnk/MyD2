package com.etrans.myd2.biz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseBiz
{
  protected static final ExecutorService dataEs = Executors.newCachedThreadPool();
}
