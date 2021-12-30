/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.test.streaming.runtime;

import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.runtime.entrypoint.ClusterEntrypoint;
import org.apache.flink.runtime.entrypoint.StandaloneSessionClusterEntrypoint;
import org.apache.flink.test.util.AbstractTestBase;

import org.junit.jupiter.api.Test;

/** Test Cluster. */
public class GxFlinkClusterTest extends AbstractTestBase {

    @Test
    public void testStandaloneSessionCluster() {
        Configuration configuration = new Configuration();

        Configuration config = new Configuration();
        config.setInteger(ConfigConstants.DEFAULT_PARALLELISM_KEY, 1);
        config.setString(JobManagerOptions.ADDRESS, "localhost");
        config.setInteger(JobManagerOptions.PORT, 6123);

        config.set(JobManagerOptions.TOTAL_PROCESS_MEMORY, MemorySize.parse("1728m"));
        config.setString(JobManagerOptions.EXECUTION_FAILOVER_STRATEGY, "region");

        config.set(JobManagerOptions.OFF_HEAP_MEMORY, MemorySize.parse("134217728b"));
        config.set(JobManagerOptions.JVM_OVERHEAD_MIN, MemorySize.parse("201326592b"));
        config.set(JobManagerOptions.JVM_METASPACE, MemorySize.parse("268435456b"));
        config.set(JobManagerOptions.JVM_HEAP_MEMORY, MemorySize.parse("1073741824b"));
        config.set(JobManagerOptions.JVM_OVERHEAD_MAX, MemorySize.parse("201326592b"));

        config.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 1);
        config.set(TaskManagerOptions.TOTAL_PROCESS_MEMORY, MemorySize.parse("1728m"));

        StandaloneSessionClusterEntrypoint entrypoint =
                new StandaloneSessionClusterEntrypoint(configuration);

        ClusterEntrypoint.runClusterEntrypoint(entrypoint);

        System.out.println("hello");
    }
}
