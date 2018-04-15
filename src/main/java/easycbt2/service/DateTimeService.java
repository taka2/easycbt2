package easycbt2.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

@Service
public class DateTimeService {
	public Instant getCurrentDateTime() {
		return Instant.now();
	}
}
