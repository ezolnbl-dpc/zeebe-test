package org.work;

import io.zeebe.client.ZeebeClient;
import org.apache.camel.CamelContext;
import org.apache.camel.spi.RestConfiguration;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

@SpringBootApplication
public class WorkerApplication {

    @Bean
    public ZeebeClient setup() {
        return ZeebeClient.newClientBuilder()
                .brokerContactPoint("localhost:26500")
                .usePlaintext()
                .numJobWorkerExecutionThreads(100)
                .build();
    }

    @Bean
    public CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                camelContext.setTracing(false);
                camelContext.setMessageHistory(false);
                camelContext.setStreamCaching(true);

                RestConfiguration rest = new RestConfiguration();
                camelContext.setRestConfiguration(rest);
                rest.setComponent("jetty");
                rest.setProducerComponent("undertow");
                rest.setPort(5000);
                rest.setDataFormatProperties(new HashMap<>());
                rest.getDataFormatProperties().put("prettyPrint", "true");
                rest.setScheme("http");
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                // empty
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }
}
