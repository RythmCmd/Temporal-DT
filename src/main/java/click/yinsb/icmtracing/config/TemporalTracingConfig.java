package click.yinsb.icmtracing.config;

import io.temporal.opentracing.OpenTracingOptions;
import io.temporal.spring.boot.TemporalOptionsCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentracing.Tracer;

@Configuration
public class TemporalTracingConfig {

    @Bean
    public OpenTracingOptions openTracingOptions() {
        return OpenTracingOptions.getDefaultInstance();
    }

    @Bean
    public TemporalOptionsCustomizer<io.temporal.client.WorkflowClientOptions.Builder> customWorkerClientOptions(
            Tracer tracer) {
        return new TemporalOptionsCustomizer<io.temporal.client.WorkflowClientOptions.Builder>() {
            @Override
            public io.temporal.client.WorkflowClientOptions.Builder customize(
                    io.temporal.client.WorkflowClientOptions.Builder builder) {
                return builder.setInterceptors(
                        new io.temporal.opentracing.OpenTracingClientInterceptor(OpenTracingOptions.newBuilder()
                                .setTracer(tracer)
                                .build()));
            }
        };
    }

    @Bean
    public TemporalOptionsCustomizer<io.temporal.worker.WorkerFactoryOptions.Builder> customWorkerFactoryOptions(
            Tracer tracer) {
        return new TemporalOptionsCustomizer<io.temporal.worker.WorkerFactoryOptions.Builder>() {
            @Override
            public io.temporal.worker.WorkerFactoryOptions.Builder customize(
                    io.temporal.worker.WorkerFactoryOptions.Builder builder) {
                return builder.setWorkerInterceptors(
                        new io.temporal.opentracing.OpenTracingWorkerInterceptor(OpenTracingOptions.newBuilder()
                                .setTracer(tracer)
                                .build()));
            }
        };
    }
}
