package zksandbox.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

public class LocaleConfigManagerTest {

    LocaleConfigManager localeConfigManager = new LocaleConfigManager();

    @Test
    public void testGetLocales() {
        List<LocaleConfigManager.LocaleConfig> locales = localeConfigManager.getLocales();
        Assertions.assertNotNull(locales);
    }

    @Test
    public void testGetLocaleDefault() {
        LocaleConfigManager.LocaleConfig locale = localeConfigManager.getLocaleDefault();
        Assertions.assertNotNull(locale);
        Assertions.assertEquals(locale, LocaleConfigManager.LocaleConfig.CZ);
    }

    @Test
    public void testIsLocaleDef() {
        boolean defaultLocale = localeConfigManager.isLocaleDef(LocaleConfigManager.LocaleConfig.CZ.getLocale());
        Assertions.assertTrue(defaultLocale);
        boolean nonDefaultLocale = localeConfigManager.isLocaleDef(LocaleConfigManager.LocaleConfig.EN.getLocale());
        Assertions.assertFalse(nonDefaultLocale);
    }

    @Test
    public void testCheckLocale() {
        // nenadefinovany locale, vraci default
        Locale localeChecked = localeConfigManager.checkLocale(new Locale("cs"));
        Assertions.assertEquals(localeChecked, localeConfigManager.getLocaleDefault().getLocale());

        // nadefinovany locale, vraci stejny
        localeChecked = localeConfigManager.checkLocale(new Locale("en"));
        Assertions.assertEquals(localeChecked, LocaleConfigManager.LocaleConfig.EN.getLocale());
    }

    @Test
    public void testCheckLocaleLanguage() {
        // nenadefinovany locale, vraci default
        LocaleConfigManager.LocaleConfig localeChecked = localeConfigManager.getLocaleCfgByLocale(new Locale("cs"));
        Assertions.assertEquals(localeChecked, localeConfigManager.getLocaleDefault());

        // nadefinovany locale, vraci stejny
        localeChecked = localeConfigManager.getLocaleCfgByLocale(new Locale("en"));
        Assertions.assertEquals(localeChecked, LocaleConfigManager.LocaleConfig.EN);
    }
}
