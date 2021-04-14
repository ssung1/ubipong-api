package com.eatsleeppong.ubipong.tournamentmanager;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This is only here to supply a ContextConfiguration.
 * 
 * The actual configuration is in AdapterTestConfiguration.  Usually we
 * can just keep all the configuration here, but when we are running from
 * VSCode, VSCode would mix classpaths of all the subprojects together,
 * causing bean conflict.  The solution, therefore, is to annotate the
 * configuration as @TestConfiguration in AdapterTestConfiguration and
 * import it explicitly.
 * 
 * To reiterate, this is only necessary for running from VSCode.  Gradle
 * and IntelliJ both work fine without having to do the above.
 */
@Configuration
public class AdapterContextConfiguration {
}
