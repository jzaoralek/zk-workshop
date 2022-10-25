package zksandbox.component;

import org.zkoss.zhtml.I;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

public class IconLabelComponent extends HtmlMacroComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6651759083668249879L;

	@Wire
	private Label textLabel;

	@Wire
	private I iconified;

	private String iconSclass;
	private String label;

	public IconLabelComponent() {
		compose();
	}

	@Override
	public void afterCompose() {
		super.afterCompose();
		render();
	}

	/**
	 * 
	 */
	private void render() {
		if (textLabel != null) {
			textLabel.setValue(label);
		}
		if (iconified != null) {
			iconified.setSclass(iconSclass);
		}
	}

	public String getIconSclass() {
		return iconSclass;
	}

	public void setIconSclass(String iconSclass) {
		this.iconSclass = iconSclass;
		render();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		render();
	}
}
