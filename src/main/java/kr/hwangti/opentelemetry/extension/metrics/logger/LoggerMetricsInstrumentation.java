package kr.hwangti.opentelemetry.extension.metrics.logger;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class LoggerMetricsInstrumentation implements TypeInstrumentation {

    @Override
    public ElementMatcher<TypeDescription> typeMatcher() {
        return hasSuperType(named("org.slf4j.Logger")).and(not(isInterface()));
    }

    @Override
    public void transform(TypeTransformer transformer) {
        transformer.applyAdviceToMethod(
                isPublic().and(nameMatches("trace|debug|info|warn|error")),
                LoggerEventAdvice.class.getName()
        );
    }


    @SuppressWarnings("unused")
    public static class LoggerEventAdvice {

        public static LongCounter counter;

        static {
            counter = GlobalOpenTelemetry
                    .getMeter("io.opentelemetry.sdk.metrics")
                    .counterBuilder("logger.events")
                    .setUnit("total")
                    .setDescription("Number of log events that were enabled by the effective log level")
                    .build();

            counter.add(0, Attributes.builder().put("level", "TRACE").build());
            counter.add(0, Attributes.builder().put("level", "DEBUG").build());
            counter.add(0, Attributes.builder().put("level", "INFO").build());
            counter.add(0, Attributes.builder().put("level", "WARN").build());
            counter.add(0, Attributes.builder().put("level", "ERROR").build());
        }

        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.Origin("#m") String methodName) {
            counter.add(1, Attributes.builder()
                    .put("level", methodName.toUpperCase())
                    .build());
        }

    }

}
