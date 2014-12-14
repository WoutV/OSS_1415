package plugin;

import org.sonar.api.web.AbstractRubyTemplate;
import org.sonar.api.web.Description;
import org.sonar.api.web.RubyRailsWidget;
import org.sonar.api.web.UserRole;
import org.sonar.api.web.WidgetScope;

/**
 * The widget class needed to create a Sonar widget.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 */
@UserRole(UserRole.USER)
@Description("The Polymorphic Views plugin allows users to create charts based on various metrics collected by Sonar.")
@WidgetScope(WidgetScope.PROJECT)
public class PolymorphicViewsWidget extends AbstractRubyTemplate implements RubyRailsWidget {

	@Override
	public String getId() {
		return "polymorphicviews";
	}

	@Override
	public String getTitle() {
		return "Polymorphic Views";
	}
	
	@Override
	protected String getTemplatePath() {
        return "/be/kuleuven/cs/oss/polymorphicviews/polymorphicviews_widget.html.erb";
	}

}
