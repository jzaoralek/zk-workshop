package zksandbox.i18n;

import org.zkoss.zul.Image;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

// TODO: (JZ), změnit na service
public class LocaleConfigManager {

    /**
     * Konfigurace lokalizací, bude nahrazena konfigurací v DB.
     */
    public enum LocaleConfig {
        CZ(new Locale("cs"), new Image("/sources/imgs/czech-republic.png"), true),
        EN (new Locale("en"), new Image("/sources/imgs/united-kingdom.png"), false);

        private final Locale locale;
        private final Image img;
        private final boolean def;

        private LocaleConfig(Locale locale, Image img, boolean def) {
            this.locale = locale;
            this.img = img;
            this.def = def;
        }

        public Locale getLocale() {
            return locale;
        }

        public Image getImg() {
            return img;
        }
    }

    /**
     * Vraci seznam povolenych lokalizaci;
     * @return
     */
    public List<LocaleConfig> getLocales() {
        return Arrays.asList(LocaleConfig.values());
    }

    /**
     * Vraci default lokalizaci;
     * @return
     */
    public LocaleConfig getLocaleDefault() {
        for (LocaleConfig cfg : getLocales()) {
            if (cfg.def) {
                return cfg;
            }
        }
        throw new IllegalStateException("No default Locale congigured.");
    }


    /**
     * Zjistuje zda-li jde o default lokalizaci.
     * @param locale
     * @return
     */
    public boolean isLocaleDef(Locale locale) {
        Objects.requireNonNull(locale);
        return locale.getLanguage().equalsIgnoreCase(getLocaleDefault().locale.getLanguage());
    }

    /**
     * Kontrola zda-li je Locale v seznamu definovanych.
     * Pokud je v seznamu definovanych locale, vrati, jinak vrati default.
     * @param locale
     * @return
     */
    public Locale checkLocale(Locale locale) {
        Objects.requireNonNull(locale);

        for (LocaleConfig cfg : getLocales()) {
            if (locale.getLanguage().equalsIgnoreCase(cfg.locale.getLanguage())) {
                return cfg.locale;
            }
        }

        // Locale na vstupu neni v seznamu definovanych, vraceni default.
        return getLocaleDefault().locale;
    }

    public LocaleConfig getLocaleCfgByLocale(Locale locale) {
        Objects.requireNonNull(locale);

        for (LocaleConfig cfg : getLocales()) {
            if (locale.getLanguage().equalsIgnoreCase(cfg.locale.getLanguage())) {
                return cfg;
            }
        }

        return getLocaleDefault();
    }
}