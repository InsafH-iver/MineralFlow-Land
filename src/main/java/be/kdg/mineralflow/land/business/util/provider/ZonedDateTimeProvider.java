package be.kdg.mineralflow.land.business.util.provider;

import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Component
public class ZonedDateTimeProvider {
    public ZonedDateTime now(ZoneOffset offset) {
        return ZonedDateTime.now(offset);
    }
    public ZonedDateTime now() {
        return ZonedDateTime.now();
    }
}
