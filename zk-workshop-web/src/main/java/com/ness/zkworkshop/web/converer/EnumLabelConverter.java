package com.ness.zkworkshop.web.converer;

import com.ness.zkworkshop.web.util.WebUtils;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;

public class EnumLabelConverter implements Converter<String, Enum<?>, Component> {

	@Override
	public Enum<?> coerceToBean(String compAttr, Component component, BindContext ctx) {
		return null;
	}

	@Override
	public String coerceToUi(Enum<?> value, Component component, BindContext ctx) {
		return (value == null ? null : WebUtils.getMessageItemFromEnum(value));
	}
}
