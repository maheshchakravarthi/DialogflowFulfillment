package com.dialogflowfulfillment;

import io.prometheus.client.hotspot.DefaultExports;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(scanBasePackages = {"java.lang","org.springframework.data.redis.connection"})
//@EnableAutoConfiguration
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
public class DialogflowFulfillmentApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(DialogflowFulfillmentApplication.class, args);
		DefaultExports.initialize();
	}
}
