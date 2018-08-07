package easycbt2.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

@Service
public class DateTimeService {

	public Instant getCurrentDateTime() {
		return LocalDateTime.now().toInstant(ZoneOffset.UTC);
	}
}
