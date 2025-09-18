package kr.hwangti.opentelemetry.extension.metrics.system;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class ProcessStartTimeSecondsMetricsInstrumentation implements TypeInstrumentation {

    static {
        GlobalOpenTelemetry
                .getMeter("io.opentelemetry.sdk.metrics")
                .gaugeBuilder("process.start.time.seconds")
                .setDescription("Start time of the process since unix epoch")
                .build()
                .set(System.currentTimeMillis() / 1000.0);
    }

    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        return ElementMatchers.none();
    }

    @Override
    public void transform(TypeTransformer transformer) {
    }

}
