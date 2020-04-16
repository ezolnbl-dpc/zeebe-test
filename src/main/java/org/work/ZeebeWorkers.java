package org.work;

import io.zeebe.client.ZeebeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ZeebeWorkers {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ZeebeClient zeebeClient;

    @PostConstruct
    public void setupWorkers() {
        zeebeClient.newWorker()
                .jobType("work123")
                .handler((client, job) -> {
                    logger.info("Started work123");
                    client.newCompleteCommand(job.getKey())
                            .send();
                })
                .maxJobsActive(50)
                .name("w1")
                .open();

        zeebeClient.newWorker()
                .jobType("work1234")
                .handler((client, job) -> {
                    logger.info("Started work1234");
                    client.newCompleteCommand(job.getKey())
                            .send();
                })
                .maxJobsActive(50)
                .name("w2")
                .open();
    }
}