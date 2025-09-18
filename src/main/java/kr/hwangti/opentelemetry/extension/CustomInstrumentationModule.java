package kr.hwangti.opentelemetry.extension;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import kr.hwangti.opentelemetry.extension.metrics.logger.LoggerMetricsInstrumentation;
import kr.hwangti.opentelemetry.extension.metrics.system.ProcessStartTimeSecondsMetricsInstrumentation;
import kr.hwangti.opentelemetry.extension.trace.SpringExtendedInstrumentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoService(InstrumentationModule.class)
public class CustomInstrumentationModule extends InstrumentationModule {

    public CustomInstrumentationModule() {
        super("hwangti-custom");
    }

    @Override
    public List<String> getAdditionalHelperClassNames() {
        List<String> list = new ArrayList<>();

        list.add(LoggerMetricsInstrumentation.LoggerEventAdvice.class.getName());
        list.add(SpringExtendedInstrumentation.SpringBeanAdvice.class.getName());

        return Collections.unmodifiableList(list);
    }

    @Override
    public List<TypeInstrumentation> typeInstrumentations() {
        List<TypeInstrumentation> list = new ArrayList<>();

        list.add(new ProcessStartTimeSecondsMetricsInstrumentation());
        list.add(new LoggerMetricsInstrumentation());
        list.add(new SpringExtendedInstrumentation());

        return Collections.unmodifiableList(list);
    }

}
