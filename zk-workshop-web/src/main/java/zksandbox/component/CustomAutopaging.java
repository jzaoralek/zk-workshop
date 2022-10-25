package zksandbox.component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.AfterSizeEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;

/**
 * Vlastní implementace autopagingu (tzn. dynamický počet záznamů na jednu
 * stránku datové tabulky). ZK autopaging nefunguje moc dobře (SDAT-2930).
 *
 */
public class CustomAutopaging {

	/**
	 * Výchozí výška řádku (v pixelech) datové tabulky
	 */
	public static final int DEFAULT_ROW_HEIGHT = 31;

	/**
	 * Běžná výška (v pixelech) horizontálního posuvníku (v prohlížeči)
	 */
	private static final int SCROLLBAR_HEIGHT = 18;

	private static final String PARENT = "PARENT";
	private static final String LISTHEAD = "LISTHEAD";

	/**
	 * Výška řádku (v pixelech) datové tabulky
	 */
	private int rowHeight = 0;

	/**
	 * Výška v pixelech rodičovské komponenty listboxu. Samotná výška listboxu
	 * v ZK nelze zjistit, odvodíme tedy od výšky nejbližšího wrapperu.
	 */
	private int parentHeight = 0;

	/**
	 * parentWidth a listboxWidth slouží k určení, jestli se v listboxu vyskytuje
	 * horizontální posuvník - v tom případě je nutné zmenšit pageSize
	 */
	private int parentWidth = 0;
	private int listboxWidth = 0;

	/**
	 * součet výšek všech sledovaných komponent v listboxu (hlavičky, footer)
	 */
	private int listboxCompsHeight = 0;

	/**
	 * všechyn výšky sledovaných komponent uvnitř listboxu byly nastaveny
	 */
	private boolean listboxCompsCollected = false;

	/**
	 * proběhla první fáze nastavení výšek u všech sledovaných komponent (parent + listbox comps)
	 */
	private boolean initCompHeightFinished = false;

	/**
	 * Pokud TRUE - v listboxu se vyskytuje horizontální scrollbar
	 */
	private boolean scrollbar = false;

	/**
	 * Klíčem je uuid komponenty, která je uvnitř listboxu
	 * a sledujeme její výšku - tzn. hlavičky s filtry, pomocné hlavičky
	 * a footer s ovládáním stránkování. Hodnota v mapě vyjadřuje pixelovou 
	 * výšku komponenty 
	 */
	private Map<String, Integer> listboxComponents;
	private Listbox listbox;
	private boolean debugEnabled = false;

	/**
	 * 
	 */
	private CustomAutopaging() {
	}

	/**
	 * Inicializace a registrace autopagingu na datovou tabulku
	 * 
	 * @param listbox
	 * @param rowHeight výška řádku (v pixelech) datové tabulky. Pokud null, nastaví se DEFAULT_ROW_HEIGHT
	 * @return
	 */
	public static CustomAutopaging createInstance(Listbox listbox, Integer rowHeight) {
		CustomAutopaging atp = new CustomAutopaging();
		atp.initAutopaging(listbox, rowHeight);
		return atp;
	}

	/**
	 * Inicializace
	 * 
	 * @param listbox
	 * @param rowHeight výška řádku (v pixelech) datové tabulky
	 */
	private void initAutopaging(Listbox listbox, Integer rowHeight) {
		this.listbox = listbox;

		if (rowHeight != null) {
			this.rowHeight = rowHeight;
		} else {
			this.rowHeight = DEFAULT_ROW_HEIGHT;
		}

		parentHeight = 0;
		listboxCompsHeight = 0;
		parentWidth = 0;
		listboxWidth = 0;
		listboxCompsCollected = false;
		initCompHeightFinished = false;
		scrollbar = false;
		listboxComponents = new HashMap<>(6);

		Component parent = listbox.getParent();
		listbox.setAutopaging(false);
		listbox.setPageSize(20);
		SandboxUtils.addSclass(listbox, "x-autopaging-hide-data");
		registerOnAfterSizeEvent(parent, PARENT);

		for (Component comp : listbox.getChildren()) {
			String uuid = comp.getUuid();
			if (comp instanceof Listhead) {
				registerOnAfterSizeEvent(comp, LISTHEAD);
				listboxComponents.put(LISTHEAD, null);
			} else {
				registerOnAfterSizeEvent(comp, uuid);
				listboxComponents.put(uuid, null);
			}
		}
	}

	/**
	 * Výpočet počtu řádků na jednu stránku
	 * 
	 * @param height
	 */
	private void recalculateAutopaging() {
		checkScrollbar();
		int headers = getListboxCompsHeight();
		int pageSize = 0;

		if (scrollbar) {
			//listbox obsahuje horizontální scrollbar
			pageSize = (parentHeight - headers - SCROLLBAR_HEIGHT) / rowHeight;
		} else {
			pageSize = (parentHeight - headers) / rowHeight;
		}

		int testSum = pageSize * rowHeight + headers;
		if (parentHeight - testSum <= 2) {
			pageSize--;
		}

		listbox.setAutopaging(false);
		listbox.setPageSize((int) pageSize);
		SandboxUtils.removeSclass(listbox, "x-autopaging-hide-data");

		debug(MessageFormat.format("ATP RECALCULATED: pageSize = {0}, parentHeight = {1}, testSum = {2}, headers = {3}",
				pageSize, parentHeight, testSum, headers));
	}

	/**
	 * Registrace události na změnu velikosti komponenty (událost se zavolá i při prvním vykreslení komponenty
	 * na stránce)
	 * 
	 * @param comp
	 */
	private void registerOnAfterSizeEvent(Component comp, String source) {
		comp.addEventListener(Events.ON_AFTER_SIZE, e -> onAfterSize((AfterSizeEvent) e, source));
	}

	/**
	 * Sběr velikostí (v pixelech) všech sledovaných komponent. Události dorazí od všech komponent 
	 * pokaždé v jiném pořadí - je na to ptřeba dát pozor. Přepočet pagingu probíhá pouze jednou
	 * v rámci změny velikosti (i když událostí během jedné změny velikosti příjde mnohem více)
	 * 
	 * @param e
	 * @param source
	 */
	private void onAfterSize(AfterSizeEvent e, String source) {
		debug(MessageFormat.format("onAfterSize - {0}: w = {1}, h = {2}", source, e.getWidth(), e.getHeight()));

		if (PARENT.equals(source)) {
			parentHeight = e.getHeight();
			parentWidth = e.getWidth();

			if (isAllCompsCollected()) {
				recalculateAutopaging();
			}
		} else {
			listboxComponents.put(source, new Integer(e.getHeight()));
			if (LISTHEAD.equals(source)) {
				listboxWidth = e.getWidth();

				if (initCompHeightFinished && checkScrollbar()) {
					recalculateAutopaging();
				}
			}
		}

		if (!initCompHeightFinished && isAllCompsCollected()) {
			initCompHeightFinished = true;
			recalculateAutopaging();
		}
	}

	/**
	 * Nastaví příznak pro posuvník (horizontální): true - zobrazen.
	 * 
	 * @return TRUE - došlo ke změně stavu zobrazení posuvníku
	 */
	private boolean checkScrollbar() {
		if (parentWidth == 0 || listboxWidth == 0) {
			return false;
		}
		if (parentWidth < listboxWidth) {
			// posuvník se zobrazuje
			if (!scrollbar) {
				scrollbar = true;
				return true;
			} else {
				scrollbar = true;
				return false;
			}
		} else {
			// posuvník se nezobrazuje
			if (!scrollbar) {
				scrollbar = false;
				return false;
			} else {
				scrollbar = false;
				return true;
			}
		}
	}

	/**
	 * Zkontroluje, jestli už byli zaregistrovány výšky všech sledovaných komponent
	 * (parent + uvnitř listboxu)
	 * 
	 * @return
	 */
	private boolean isAllCompsCollected() {
		if (parentHeight == 0) {
			return false;
		}
		if (listboxCompsCollected) {
			return true;
		}
		for (Integer compHeight : listboxComponents.values()) {
			if (compHeight == null) {
				return false;
			}
		}
		listboxCompsCollected = true;
		return true;
	}

	/**
	 * Vrátí součet výšek sledovaných komponent v listboxu
	 * 
	 * @return
	 */
	private int getListboxCompsHeight() {
		if (listboxCompsHeight > 0) {
			return listboxCompsHeight;
		}

		listboxCompsHeight = 0;
		for (Integer compHeight : listboxComponents.values()) {
			listboxCompsHeight += compHeight.intValue();
		}
		return listboxCompsHeight;
	}

	private void debug(String message) {
		if (debugEnabled) {
			System.out.println("[ATP] " + message);
		}
	}
}
