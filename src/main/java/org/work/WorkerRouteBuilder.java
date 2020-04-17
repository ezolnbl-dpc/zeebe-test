package org.work;

import io.zeebe.client.ZeebeClient;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WorkerRouteBuilder extends RouteBuilder {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ZeebeClient zeebeClient;

    @Override
    public void configure() {
        from("rest:GET:/start")
                .id("worker")
                .log(LoggingLevel.INFO, "## working started ##")
                .process(exchange -> {
                            for (int i = 0; i < 200; i++) {
                                Map<String, Object> variables = new HashMap<>();
                                variables.put("thing1", "thing1");
                                variables.put("thing2", 100);

                                zeebeClient.newCreateInstanceCommand()
                                        .bpmnProcessId("Process_0k7bx2t")
                                        .latestVersion()
                                        .variables(variables)
                                        .send()
                                        .join();

                                logger.info("zeebee workflow instance started");
                            }
                        }
                );
    }
}