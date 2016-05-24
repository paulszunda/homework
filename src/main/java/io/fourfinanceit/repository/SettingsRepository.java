package io.fourfinanceit.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import io.fourfinanceit.domain.Settings;

public interface SettingsRepository extends CrudRepository<Settings, Long> {

	@Query("from Settings s where s.defaultSettings = true")
	public Settings findDefaultSettings();
}
