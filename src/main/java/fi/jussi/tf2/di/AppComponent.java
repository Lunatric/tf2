package fi.jussi.tf2.di;

import java.util.Scanner;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import fi.jussi.tf2.parsing.Parser;
import fi.jussi.tf2.ui.Ui;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
	
	@Named("userinput")
	Scanner provideScanner();
	
	Parser provideParser();	
	
	Ui provideUi();
	
}
