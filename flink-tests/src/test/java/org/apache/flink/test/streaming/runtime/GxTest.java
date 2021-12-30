/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.test.streaming.runtime;

import org.apache.flink.test.util.AbstractTestBase;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/** For my own test. */
public class GxTest extends AbstractTestBase {

    public static void main(String[] args) {
        //        Optional<Integer> res =
        //                Stream.of("f", "ab", "abdcd")
        //                        .map(s -> s.length())
        //                        .filter(l -> l <= 3)
        //                        .max(Comparator.comparingInt(o -> o));
        //        System.out.println(res);

        // 守护线程
        Thread t = new GxThread("my_thread");
        t.setDaemon(true);
        t.start();

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main over");
    }

    @Test
    public void testCompletableFuture() {
        // CompletableFuture.supplyAsync & thenApply & thenAccept & whenComplete
        int i = 2;
        CompletableFuture<Void> res =
                CompletableFuture.supplyAsync(
                                () -> {
                                    if (i < 10) {
                                        System.out.println("supplyAsync: " + i);
                                        return Math.sqrt(i);
                                    } else {
                                        return 0.0;
                                    }
                                })
                        .thenApply(
                                a -> {
                                    System.out.println("apply: " + a);
                                    return a + 1;
                                })
                        .thenAccept(
                                aDouble -> {
                                    System.out.println("accept: " + aDouble);
                                    throw new RuntimeException("Excep throwed");
                                })
                        .whenComplete(
                                (unused, throwable) -> {
                                    if (throwable != null) {
                                        System.out.println("Throwable: " + throwable.toString());
                                    }
                                });

        // complete()
        CompletableFuture<Integer> cf = new CompletableFuture<>();
        cf.thenApply(
                        a -> {
                            System.out.println("apply: " + a);
                            return a + 1;
                        })
                .thenAccept(a -> System.out.println("consume: " + a));
        cf.complete(1);
    }

    @Test
    public void testThreadLocal() throws InterruptedException {
        ThreadLocal<String> tl = new ThreadLocal<>();
        tl.set("tlVal");

        Thread t1 =
                new Thread(
                        () -> {
                            tl.set("t1Val");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("t1 tlVal: " + tl.get());
                            tl.remove();
                            System.out.println("t1 tlVal after remove: " + tl.get());
                        });

        Thread t2 =
                new Thread(
                        () -> {
                            tl.set("t2Val");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("t2 tlVal: " + tl.get());

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("t2 tlVal after t1 remove tl: " + tl.get());
                        });

        t1.run();
        t2.run();

        t1.join();
        t2.join();
    }

    /** 用这个方法测试，setDaemon设置为true和false的结果是一样的。猜测是@Test注解的影响. 放在main方法中测试，可以得到期望的结果. */
    @Test
    public void testDaemon() {

        Thread t = new GxThread("my_thread");
        //        t.setDaemon(true);
        t.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main over");
    }

    static class GxThread extends Thread {
        public GxThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println("hello, daemon or not: " + Thread.currentThread().isDaemon());
            int i = 0;
            while (true) {
                System.out.println("world " + i++);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
