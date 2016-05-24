package io.fourfinanceit;

import java.math.BigDecimal;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import io.fourfinanceit.domain.Settings;
import io.fourfinanceit.repository.SettingsRepository;

@Component
public class ApplicationStartedListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private SettingsRepository settingsRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		addDefaultSettings();
	}
	
	//Variables taken from SMSCredit.lv
	private void addDefaultSettings() {
		Settings settings = new Settings();
		settings.setDefaultSettings(true);
		settings.setInitialTermMin(10);
		settings.setInitialTermMax(30);
		settings.setInterestFactor(new BigDecimal("1.5"));
		settings.setMaxApplications(3);
		settings.setMinAmount(new BigDecimal(50));
		settings.setMaxAmount(new BigDecimal(300));
		settings.setRiskHoursStart(LocalTime.of(00, 00));
		settings.setRiskHoursEnd(LocalTime.of(06, 00));
		settings.setReturnBaseFactor(new BigDecimal("0.021"));
		settings.setReturnPerDayFactor(new BigDecimal("0.0025"));
		settingsRepository.save(settings);
	}

}
