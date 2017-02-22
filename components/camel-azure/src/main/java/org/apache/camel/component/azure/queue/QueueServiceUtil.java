/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.azure.queue;

import java.net.URI;

import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.queue.CloudQueue;

public final class QueueServiceUtil {
    private QueueServiceUtil() { 
    }
    
    public static URI prepareStorageQueueUri(QueueServiceConfiguration cfg) {
        return prepareStorageQueueUri(cfg, true);
    }

    public static URI prepareStorageQueueUri(QueueServiceConfiguration cfg, boolean isForMessages) {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append("https://")
            .append(cfg.getAccountName())
            .append(QueueServiceConstants.SERVICE_URI_SEGMENT)
            .append("/")
            .append(cfg.getQueueName());
        if (isForMessages) {
            uriBuilder.append("/messages");
        }
        return URI.create(uriBuilder.toString());
    }
    
    public static CloudQueue createQueueClient(QueueServiceConfiguration cfg)
        throws Exception {
        CloudQueue client = (CloudQueue) getConfiguredClient(cfg);
        if (client == null) {
            URI uri = prepareStorageQueueUri(cfg);
            StorageCredentials creds = getAccountCredentials(cfg);
            client = new CloudQueue(uri, creds);
        }
        return client;
    }

    public static CloudQueue getConfiguredClient(QueueServiceConfiguration cfg) {
        CloudQueue client = cfg.getAzureQueueClient();
        if (client != null && !client.getUri().equals(prepareStorageQueueUri(cfg))) {
            throw new IllegalArgumentException("Invalid Client URI");
        }
        return client;
    }
    
    public static StorageCredentials getAccountCredentials(QueueServiceConfiguration cfg) {
        return cfg.getCredentials();
    }
}