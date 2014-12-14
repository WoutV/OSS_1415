package plugin;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.Extension;
import org.sonar.api.SonarPlugin;

import facade.WebServiceSonarFacade;
import plugin.PolymorphicViewsWidget;

/**
 * The plugin class needed to create a Sonar plugin.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 */
public class PolymorphicViewsPlugin extends SonarPlugin {

	@Override
	public List<Class<? extends Extension>> getExtensions() {
        return Arrays.<Class<? extends Extension>>asList(
        		WebServiceSonarFacade.class,
        		PolymorphicViewsChart.class,
        		PolymorphicViewsWidget.class);
    }
	
}
