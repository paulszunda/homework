package io.fourfinanceit.util.db;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Timestamp> {
	@Override
	public Timestamp convertToDatabaseColumn(LocalDate ld) {
		return Timestamp.valueOf(ld.atStartOfDay());
	}

	@Override
	public LocalDate convertToEntityAttribute(Timestamp ts) {
		return ts.toLocalDateTime().toLocalDate();
	}
}