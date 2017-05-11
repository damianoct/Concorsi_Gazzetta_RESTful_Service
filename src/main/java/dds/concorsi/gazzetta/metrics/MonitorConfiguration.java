package dds.concorsi.gazzetta.metrics;


import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

//@Configuration
//@EnableMetrics
public class MonitorConfiguration extends MetricsConfigurerAdapter
{

    private static final String GRAPHITE_PREFIX =
            "collectd/graphite-monitoring-example/test";

    @PostConstruct()
    public void init()
    {
        configureReporters(GRAPHITE_PREFIX);
    }

    @Value("${graphite.host}")
    private String graphiteHost;

    @Value("${graphite.port}")
    private int graphitePort;

    @Value("${graphite.amount.of.time.between.polls}")
    private long graphiteAmountOfTimeBetweenPolls;

    @Inject
    private MetricRegistry registry;

    private String graphitePrefix;

    protected void configureReporters(String graphitePrefix) {
        this.graphitePrefix = graphitePrefix;
        configureReporters(registry);
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        registerReporter(JmxReporter.forRegistry(metricRegistry)
                .build()).start();
        GraphiteReporter graphiteReporter =
                getGraphiteReporterBuilder(metricRegistry)
                        .build(getGraphite());
        registerReporter(graphiteReporter);
        graphiteReporter.start(graphiteAmountOfTimeBetweenPolls,
                TimeUnit.MILLISECONDS);
    }

    private GraphiteReporter.Builder getGraphiteReporterBuilder(MetricRegistry
                                                       metricRegistry) {
        metricRegistry.register("gc", new GarbageCollectorMetricSet());
        metricRegistry.register("memory", new MemoryUsageGaugeSet());
        metricRegistry.register("threads", new ThreadStatesGaugeSet());
        return GraphiteReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .prefixedWith(graphitePrefix);
    }

    private Graphite getGraphite() {
        return new Graphite(new InetSocketAddress(graphiteHost,
                graphitePort));
    }
}
