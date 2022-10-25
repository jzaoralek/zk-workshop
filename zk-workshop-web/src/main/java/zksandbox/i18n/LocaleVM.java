package zksandbox.i18n;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * VM pro reseni prepinani lokalizace.
 */
public class LocaleVM {

    private LocaleConfigManager localeCfgManager = new LocaleConfigManager();
    private LocaleConfigManager.LocaleConfig localeSelected;

    @Init
    public void init() {
        initLocale();
    }

    /**
     * Inicializace lokalizace.
     * Pokud je v session, zkontroluje zda-li je v seznamu povolenych, pokud ano nastavi, pokud ne nastavi default.
     * Pokud neni v session nastavi default.
     */
    private void initLocale() {
        Session session = Executions.getCurrent().getSession();
        Locale locale = getLocaleSession(session);
        if (locale == null) {
            localeSelected = localeCfgManager.getLocaleDefault();
        } else {
            // kontrola zda-li jde o nadefinovany Locale, pokud ne, vraci default
            localeSelected = localeCfgManager.getLocaleCfgByLocale(locale);
        }
        setLocaleToSession(session, localeSelected.getLocale());
    }

    @Command
    public void changeLocaleCmd(@BindingParam("locale") LocaleConfigManager.LocaleConfig locale) {
        // Objects.requireNonNull(locale, "locale is null");
        setLocaleToSession(Executions.getCurrent().getSession(), localeCfgManager.checkLocale(localeSelected.getLocale()));
        Executions.sendRedirect("");
    }

    /**
     * Seznam povolenych locales.
     * @return
     */
    public List<LocaleConfigManager.LocaleConfig> getLocales() {
        return localeCfgManager.getLocales();
    }

    /**
     * Vraci Locale ze session.
     * @param session
     * @return
     */
    private Locale getLocaleSession(Session session) {
        return (Locale)session.getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE);
    }

    /**
     * Nastavi Locale do session.
     * @param session
     * @param locale
     */
    private void setLocaleToSession(Session session, Locale locale) {
        session.setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, locale);
    }

    public LocaleConfigManager.LocaleConfig getLocaleSelected() {
        return localeSelected;
    }
    public void setLocaleSelected(LocaleConfigManager.LocaleConfig localeSelected) {
        this.localeSelected = localeSelected;
    }
}
