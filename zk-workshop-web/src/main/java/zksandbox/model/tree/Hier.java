package zksandbox.model.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** Obecna hierarchie objektu. Hierarchie je stromova struktura, ktera muze mit vice korenu, tzn.
 * na sobe nezavislych stromu, ktere maji urceno poradi.
 *
 * @param <T_ELM> typ, jehoz hierarchie se reprezentuje.
 */
public class Hier<T_ELM extends TreeElement> implements Serializable {

	/** Stupen rozdielu uzlov v hierarchii pre CompFunc.diffHier*/
	public enum HierNodeDiff {
		NONE, // uzly zhodne (povodne false)
		PARENT, /* rozdiel je v rodicovi (a pripadne aj v poradi)
		           to ma dopad na pripadne propagacie v ramci hierarchie */
		MOVED_ORDER, /* uzol nezmenil rodica, ale uzivatel ho presunul, cize sa mu meni poradie,
		                to nema propagacny dopad, ale moze to mat dopad v [pripade editacie neuplnej
		                hierarchie */
		ORDER // rodic zostal rovnaky, meni sa len poradie, ale len preto, ze doslo k nejakemu vlozeniu,
		     // alebo ubraniu nodu s nizsim poradim a rovnakym rodicom, cize fakticky sa node sam "nepohol"

		// zatial nevidim potrebu rozlisovat moznost PARENT_ORDER
	}
	/** Funkce k pouziti v ramci mapovani na hierarchii. Metoda map vraci true/flase ve smyslu
	 * lze/nelze ukoncit pruchod stromem (napr. pro pocet prvku nelze nikdy, pro nalezeni
	 * konkretniho prvku po jeho nalezeni apod.).
	 * @param <T_ELM>
	 */
	public interface MapFunc<T_ELM> {
		boolean map(HierNode<T_ELM> hn);
	}

	/** Funkce slouzici k porovnani uzlu hierarchie na shodu. Dale vraci UNIKATNI klic
	 * hodnoty, ktery je v ramci porovnavani potreba kvuli efektivite pouziteho algoritmu.
	 * @param <T_ELM>
	 */
	public interface CompFunc<T_ELM> extends Serializable {
		Object getKey(T_ELM elm);
		HierNodeDiff diffHier(HierNode<T_ELM> hn1, HierNode<T_ELM> hn2);
		boolean diffContent(T_ELM el1, T_ELM el2);
	}

	/** Reakce na udalosti ve strukture stromu.
	 * @param <T_ELM>
	 */
	public interface StructChngCallback<T_ELM> extends Serializable {
		void ordShift(T_ELM elm, int shift);
	}

	/** Umoznuje ovlivnit chovani metody toString. Urceno pro vyvoj, v produkcnim nasazeni se
	 * typicky pouzije default (minimum vypisu).
	 * @param <T_ELM>
	 */
	public interface ToStringPrinter<T_ELM extends TreeElement> extends Serializable {
		String printToString(Hier<T_ELM> hier);
	}

	/** Porovnava uzly dle hloubky vnoreni ve stromu (level), hlubsi znamena mensi.
	 */
	public static final Comparator<HierNode<?>> COMP_LVL = new LevelComparator();



	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(Hier.class);

	private CompFunc<T_ELM> compFunc;

	private StructChngCallback<T_ELM> structChngCallback;

	private ToStringPrinter<T_ELM> toStringPrinter;

	private List<HierNode<T_ELM>> roots;



	public CompFunc<T_ELM> getCompFunc() {
		return compFunc;
	}

	public void setCompFunc(CompFunc<T_ELM> compFunc) {
		this.compFunc = compFunc;
	}

	public StructChngCallback<T_ELM> getStructChngCallback() {
		return structChngCallback;
	}

	public void setStructChngCallback(StructChngCallback<T_ELM> structChngCallback) {
		this.structChngCallback = structChngCallback;
	}


	public ToStringPrinter<T_ELM> getToStringPrinter() {
		return toStringPrinter;
	}

	public void setToStringPrinter(ToStringPrinter<T_ELM> toStringPrinter) {
		this.toStringPrinter = toStringPrinter;
	}

	public List<HierNode<T_ELM>> getRoots() {
		return roots;
	}

	public void setRoots(List<HierNode<T_ELM>> roots) {
		this.roots = roots;
	}


	/** Kopie s naplnenou strukturou, coz jsou pomocne callbacky (napr. compFunc). Neobsahuje
	 * zadne uzly, roots ma hodnotu null.
	 *
	 * @return
	 */
	public Hier<T_ELM> structCopy() {
		Hier<T_ELM> ret = new Hier<T_ELM>();
		ret.setCompFunc(compFunc);
		ret.setStructChngCallback(structChngCallback);
		ret.setToStringPrinter(toStringPrinter);
		return ret;
	}


	/** Plochy pohled na hierarchii. Uzly jsou na vystup ukladany v ramci pruchodu do hloubky, k
	 * zapisu dojde pri prvni navsteve uzlu.
	 *
	 * @return seznam prvku hierarchie (poradi viz popis), nedochazi ke kopirovani uzlu
	 */
	public List<HierNode<T_ELM>> flatView() {
		if (roots == null) {
			return Collections.emptyList();
		}
		final List<HierNode<T_ELM>> ret = new ArrayList<HierNode<T_ELM>>();
		map(new MapFunc<T_ELM>() {
			@Override
			public boolean map(HierNode<T_ELM> elm) {
				ret.add(elm);
				return false;
			}
		});
		return ret;
	}


	/** Polozky hierarchie v poradi dle hierarchie.
	 *
	 * @return seznam prvku hierarchie (poradi viz popis), nedochazi ke kopirovani prvku
	 */
	public List<T_ELM> extract() {
		List<HierNode<T_ELM>> flat = flatView();
		List<T_ELM> ret = new ArrayList<T_ELM>(flat.size());
		for (HierNode<T_ELM> hn: flat) {
			ret.add(hn.getValue());
		}
		return ret;
	}


	/**
	 * @return pocet prvku v hierarchii
	 */
	public int elemCount() {
		final int count[] = {0};
		map(new MapFunc<T_ELM>() {
			@Override
			public boolean map(HierNode<T_ELM> elm) {
				count[0]++;
				return false;
			}
		});
		return count[0];
	}


	/**
	 * @return max. hloubka hierarchie, 0 pro prazdnou hierarchii
	 */
	public int depth() {
		final int depth[] = {0};
		map(new MapFunc<T_ELM>() {
			@Override
			public boolean map(HierNode<T_ELM> elm) {
				int lvl = elm.getLevel();
				if (lvl > depth[0]) {
					depth[0] = lvl;
				}
				return false;
			}
		});
		return depth[0];
	}


	/** Spusteni mapovane funkce postupne dle pruchodu do hloubky. Ve chvili, kdy funkce vrati
	 * true, je pruchod ukoncen bez ohledu na to, zda jiz bylo mapovani provedeno na vsech
	 * uzlech (napr. vyhledani konkretniho prvku).
	 *
	 * @param fnc mapovana funkce
	 */
	public void map(MapFunc<T_ELM> fnc) {
		if (roots == null || roots.isEmpty()) {
			return;
		}
		for (HierNode<T_ELM> hn: roots) {
			if (map(fnc, hn, false)) {
				break;
			}
		}
	}

	/** Spusteni mapovane funkce postupne dle pruchodu do hloubky. Ve chvili, kdy funkce vrati
	 * true, je pruchod ukoncen bez ohledu na to, zda jiz bylo mapovani provedeno na vsech
	 * uzlech (napr. vyhledani konkretniho prvku).
	 *
	 * @param fnc mapovana funkce
	 */
	public void mapOnReturn(MapFunc<T_ELM> fnc) {
		if (roots == null || roots.isEmpty()) {
			return;
		}
		for (HierNode<T_ELM> hn: roots) {
			if (map(fnc, hn, true)) {
				break;
			}
		}
	}


	/**
	 * Pridanie dalsieho prvku pre xbrl Transformaciu nepouzivat pre ine pridanie ako pri transformacii
	 * @param childElm
	 * @param parentElm
	 */
	public void addAfterXbrl(T_ELM childElm, T_ELM parentElm) {
		if (parentElm == null) {
			if (roots == null) {
				roots = new ArrayList<HierNode<T_ELM>>();
			}
			HierNode<T_ELM> childNode = find(childElm);
			if (childNode == null) {
				int ord = roots.size();
				HierNode<T_ELM> hn = new HierNode<T_ELM>(1, ord+1, childElm, null);
				roots.add(hn);
			}
		}
		else {
			int ord = roots.size();
			HierNode<T_ELM> afterNode = find(parentElm);
			if (afterNode == null) {
				HierNode<T_ELM> childNode = find(childElm);
				if (childNode == null) {
					HierNode<T_ELM> rHn = new HierNode<T_ELM>(1, ord+1, parentElm, null);
					roots.add(rHn);
					HierNode<T_ELM> hn = new HierNode<T_ELM>(2, 1, childElm, rHn);
					rHn.addAfterXbrl(hn);
				} else {
					roots.remove(childNode);

					HierNode<T_ELM> rHn = new HierNode<T_ELM>(1, ord-1, parentElm, null);
					roots.add(rHn);

					childNode.setParent(rHn);
					childNode.moveNodeXbrl();
				}
			} else {
				HierNode<T_ELM> childNode = find(childElm);
				if (childNode == null) {
					HierNode<T_ELM> hn = new HierNode<T_ELM>(afterNode.getLevel()+1, 1, childElm, afterNode);
					afterNode.addAfterXbrl(hn);
				} else {
					List<HierNode<T_ELM>> afterChilds = new ArrayList<>();
					roots.remove(childNode);

					if(afterNode.getChildren() != null){
					afterChilds = afterNode.getChildren();
					}
					afterChilds.add(childNode);
					afterNode.setChildren(afterChilds);
					childNode.setParent(afterNode);
					childNode.moveNodeXbrl();
				}
			}
		}
	}

	public void cleanOrd() {
		for (int i=0; i<roots.size(); i++) {
			roots.get(i).cleanOrd(i+1);
		}
	}


	/** Porovnani dvou hierarchii. Porovnani je zalozeno na reprezentovanych prvcich. Pokud v h1 je
	 * prvek, ktery v h2 neni, je jeho uzel povazovan za novy. Pokud v h2 je prvek, ktery v h1 neni
	 * je jeho uzel povazovan za zruseny. Pro prvky, ktere jsou v h1 i h2 se zjsti, zda jsou shodne
	 * umisteny v hierarchii a pokud ne, je uzel z h1 povazovan za modifikovany, pricemz se rozlisuje
	 * modifikace pouze poradi, nebo modifikace rodice. Co je shoda prvku
	 * a shoda v hierarchii je dane porovnavaci funkci fnc.
	 * Vraci se sestiprvkovy seznam seznamu uzlu takovy, ze prvni jsou nove uzly, druhy zrusene uzly, treti
	 * uzly, kterym se zmenil rodic, ctvrty uzly s nezmenenym rodicem, ale zmenou poradi a to takove, ktere
	 * manualne presunul uzivatel, paty jsou uzly bez zmeny rodice se zmenou poradi - kde zmena poradi byla
	 * vynucena z duvodu modifikace jineho noduu stromu (nektereho ze 4 seznamu) a sesty
	 * modifikovane uzly vecne (obsahem).
	 * Seznamy mohou byt prazdne, ale nikdy null.
	 *
	 * @param h1
	 * @param h2
	 * @param fnc
	 * @return zmeny mezi hierarchiemi, poradi v seznamech neni zaruceno
	 */
	public List<List<HierNode<T_ELM>>> compare(Hier<T_ELM> h2) {
		if (compFunc == null) {
			throw new IllegalStateException("compFunc not set but needed by compare");
		}
		List<HierNode<T_ELM>> newNodes = new ArrayList<HierNode<T_ELM>>();
		List<HierNode<T_ELM>> delNodes = new ArrayList<HierNode<T_ELM>>();
		List<HierNode<T_ELM>> updNodesHi = new ArrayList<HierNode<T_ELM>>();
		List<HierNode<T_ELM>> updNodesMovedOrd = new ArrayList<HierNode<T_ELM>>();
		List<HierNode<T_ELM>> updNodesStayingOrd = new ArrayList<HierNode<T_ELM>>();
		List<HierNode<T_ELM>> updNodesCo = new ArrayList<HierNode<T_ELM>>();

		SearchStructFunc<T_ELM> sfnc1 = new SearchStructFunc<T_ELM>(compFunc);
		SearchStructFunc<T_ELM> sfnc2 = new SearchStructFunc<T_ELM>(compFunc);
		map(sfnc1);
		h2.map(sfnc2);

		for (Map.Entry<Object, HierNode<T_ELM>> en: sfnc1.getVal().entrySet() ) {
			HierNode<T_ELM> oth = sfnc2.getVal().get(en.getKey());
			if (oth == null) {
				logger.debug("compare: newNodes + {}", en.getValue());
				newNodes.add(en.getValue());
			} else {
				switch (compFunc.diffHier(en.getValue(), oth)) {
					case PARENT:
						updNodesHi.add(en.getValue());
						break;
					case MOVED_ORDER:
						updNodesMovedOrd.add(en.getValue());
						break;
					case ORDER:
						updNodesStayingOrd.add(en.getValue());
						break;
					case NONE:
						break;
					default:
						throw new IllegalStateException("diff type not covered");
				}
				if (compFunc.diffContent(en.getValue().getValue(), oth.getValue())) {
					updNodesCo.add(en.getValue());
				}
			}
		}
		for (Map.Entry<Object, HierNode<T_ELM>> en: sfnc2.getVal().entrySet() ) {
			HierNode<T_ELM> oth = sfnc1.getVal().get(en.getKey());
			if (oth == null) {
				logger.debug("compare: delNodes + {}", en.getValue());
				delNodes.add(en.getValue());
			}
		}

		@SuppressWarnings("unchecked")
		List<List<HierNode<T_ELM>>> ret = Arrays.asList(newNodes, delNodes, updNodesHi, updNodesMovedOrd, updNodesStayingOrd, updNodesCo);
		return ret;
	}


	/** Nalezeni uzlu odpovidajiciho zadanemu prvku. Null pokud neexistuje.
	 *
	 * @param elm
	 * @param comp
	 * @return
	 */
	public HierNode<T_ELM> find(T_ELM elm) {
		if (compFunc == null) {
			throw new IllegalStateException("compFunc not set but needed by find");
		}
		FinderFunc<T_ELM> finder = new FinderFunc<T_ELM>(elm, compFunc);
		map(finder);
		return finder.getVal();
	}


	/** Nalezeni vsech vyskytu elementu z elms v hierarchii. Poradi uzlu je shodne s elms.
	 *
	 * @param elms
	 * @return
	 */
	public List<HierNode<T_ELM>> findAll(List<T_ELM> elms) {
		if (elms == null || elms.isEmpty()) {
			return Collections.emptyList();
		}
		final List<HierNode<T_ELM>> ret = new ArrayList<HierNode<T_ELM>>(elms.size());
		SearchStructFunc<T_ELM> sfnc = new SearchStructFunc<T_ELM>(compFunc);
		map(sfnc);
		Map<Object, HierNode<T_ELM>> search = sfnc.getVal();
		for (T_ELM elm: elms) {
			HierNode<T_ELM> hn = search.get(compFunc.getKey(elm));
			if (hn != null) {
				ret.add(hn);
			}
		}
		return ret;
	}


	/** Najde a presunie zadany element bezprostredne ako suseda dalsieho
	 *  vstupneho elementu, pricom v zavislsti na after to bude bud
	 *  bezprostredne za nim, alebo bezprostredne pred nim
	 *  Ak sa lubovolny z elementov v strome nenachadza, tak vynimka
	 * @return
	 */
	public void moveNodeToNeibour(T_ELM elm, T_ELM neiElm, boolean after) {
		if (neiElm == null) {
			throw new IllegalStateException("neiElm is null");
		}
		if (elm == null) {
			throw new IllegalStateException("elm is null");
		}
		HierNode<T_ELM> neiNode = find(neiElm);
		if (neiNode == null) {
			throw new IllegalStateException("neiNode not found: " + neiElm);
		}
		HierNode<T_ELM> elmNode = find(elm);
		if (elmNode == null) {
			throw new IllegalStateException("elmNode not found: " + elm);
		}
		//Vyradime element spod svojho stareho rodica
/*		if (elmNode.getLevel() > 1) {
			HierNode<T_ELM> oldParent = elmNode.getParent();
			oldParent.getChildren().remove(elmNode.getOrd()-1);
			for (int idx = elmNode.getOrd()-1; idx < oldParent.getChildren().size(); idx++) {
				oldParent.getChildren().get(idx).setOrd(idx + 1);
				if (structChngCallback != null) {
					structChngCallback.ordShift(oldParent.getChildren().get(idx).getValue(), 1);
				}
			}
		} else {
			roots.remove(elmNode.getOrd()-1);
			for (int idx = elmNode.getOrd()-1; idx < roots.size(); idx++) {
				roots.get(idx).setOrd(idx + 1);
				if (structChngCallback != null) {
					structChngCallback.ordShift(roots.get(idx).getValue(), 1);
				}
			}
		}
*/
		// delete(elmNode.getValue(), true);
		elmNode.getValue().setMoved(true);
		//nastavime nove poradie
		if (after) {
			elmNode.setOrd(neiNode.getOrd() + 1);
		} else {
			elmNode.setOrd(neiNode.getOrd());
		}

		// nahradena podmienka na level podmienkou na rodica. Level nemusi mat spravnu hodnotu
		// ak sa s uzlom hybalo!
		if (neiNode.getParent() != null) {
			//Zaradime element pod noveho rodica
			HierNode<T_ELM> parent = neiNode.getParent();
			elmNode.setParent(parent);
			parent.getChildren().add(elmNode.getOrd()-1, elmNode);
			for (int idx = elmNode.getOrd(); idx < parent.getChildren().size(); idx++) {
				parent.getChildren().get(idx).setOrd(idx + 1);
				if (structChngCallback != null) {
					structChngCallback.ordShift(parent.getChildren().get(idx).getValue(), 1);
				}
			}
		} else {
			elmNode.setParent(null);

			roots.add(elmNode.getOrd()-1, elmNode);
			for (int idx = elmNode.getOrd(); idx < roots.size(); idx++) {
				roots.get(idx).setOrd(idx + 1);
				if (structChngCallback != null) {
					structChngCallback.ordShift(roots.get(idx).getValue(), 1);
				}
			}
		}
	}

	/** Najde a presunie zadany element ako posledny element pod zadaneho rodica
	 *  Ak je rodic null, tak ako posledny element korenov
	 *  Ak sa elm v strome nenachadza, tak vynimka
	 * @return
	 */
	public void moveNodeUnder(T_ELM elm, T_ELM parentElm) {
		if (elm == null) {
			throw new IllegalStateException("elm is null");
		}
		HierNode<T_ELM> parentNode = null;
		if (parentElm != null) {
			parentNode = find(parentElm);
			if (parentNode == null) {
				throw new IllegalStateException("parentNode not found: " + parentElm);
			}
		}
		HierNode<T_ELM> elmNode = find(elm);

		if (elmNode == null) {
			throw new IllegalStateException("elmNode not found: " + elm);
		}

		//vyradenie z inej pozicie stromu
		// delete(elm, true);
		elmNode.getValue().setMoved(true);

		if (parentNode != null) {
			elmNode.setParent(parentNode);
			if (parentNode.isLeaf()) {
				parentNode.setChildren(new ArrayList<HierNode<T_ELM>>());
			}
			parentNode.getChildren().add(elmNode);
			elmNode.setOrd(parentNode.getChildren().size());
		} else {
			elmNode.setParent(null);

			roots.add(elmNode);
			elmNode.setOrd(roots.size());
		}
	}

	/** Pramatr onReturn urci, zda se ma volat az na konci (false znamena na zacatku). Pokud se
	 * vratuilo true z podstromu, k volani na uzlu jiz pro onReturn=true nedojde.
	 */
	private static <T_ELM> boolean map(MapFunc<T_ELM> fnc, HierNode<T_ELM> hn, boolean onReturn) {
		boolean ret;
		if (!onReturn) {
			ret = fnc.map(hn);
		} else {
			ret = false;
		}
		if (!ret) {
			if (!hn.isLeaf()) {
				for (HierNode<T_ELM> chld: hn.getChildren()) {
					if (map(fnc, chld, onReturn)) {
						return true;
					}
				}
			}
		} else {
			return true;
		}
		if (onReturn) {
			ret = fnc.map(hn);
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		//sb.append("Hier[cnt=").append(elemCount()).append(",depth=").append(depth());
		sb.append("Hier[rootsCnt=").append(roots != null ? roots.size() : 0);
		if (toStringPrinter != null) {
			sb.append(", content=");
			sb.append(toStringPrinter.printToString(this));
		}
		sb.append("]");
		return sb.toString();
	}


	/** Vypis hierarchie odsazene dle urovni, v elementech je potlacen novy radek. Jen pro ladici
	 * ucely.
	 *
	 * @return text. reprezentace cele hierarchie
	 */
	public String toHierString() {
		if (roots == null || roots.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (HierNode<T_ELM> root: roots) {
			printNode(root, sb);
		}
		return sb.substring(0, sb.length() - 1);
	}


	private static <T_ELM> void printNode(HierNode<T_ELM> hn, StringBuilder sb) {
		for (int i = 2; i <= hn.getLevel(); i++) {
			sb.append("    ");
		}
		String es = hn.getValue().toString();
		sb.append(es.replace('\n', '~').replace('\r', '~')).append("\n");
		if (! hn.isLeaf()) {
			for (HierNode<T_ELM> ch: hn.getChildren()) {
				printNode(ch, sb);
			}
		}
	}


	private final static class FinderFunc<T_ELM> implements MapFunc<T_ELM> {
		private final Object elmKey;
		private final CompFunc<T_ELM> comp;
		private HierNode<T_ELM> ret;

		public FinderFunc(T_ELM elm, CompFunc<T_ELM> comp) {
			this.comp = comp;
			this.elmKey = comp.getKey(elm);
		}

		public HierNode<T_ELM> getVal() {
			return ret;
		}

		@Override
		public boolean map(HierNode<T_ELM> hn) {
			if (elmKey.equals(comp.getKey(hn.getValue()))) {
				ret = hn;
				return true;
			} else {
				return false;
			}
		}
	}

	private final static class SearchStructFunc<T_ELM> implements MapFunc<T_ELM> {
		private final CompFunc<T_ELM> cmp;
		private final HashMap<Object, HierNode<T_ELM>> ret = new HashMap<Object, HierNode<T_ELM>>();

		public SearchStructFunc(CompFunc<T_ELM> cmp) {
			this.cmp = cmp;
		}

		public Map<Object, HierNode<T_ELM>> getVal() {
			return ret;
		}

		@Override
		public boolean map(HierNode<T_ELM> hn) {
			ret.put(cmp.getKey(hn.getValue()), hn);
			return false;
		}
	}

	private final static class LevelComparator implements Comparator<HierNode<?>>, Serializable {
		private static final long serialVersionUID = 1L;
		@Override
		public int compare(HierNode<?> hn1, HierNode<?> hn2) {
			return (hn2.getLevel() - hn1.getLevel());
		}
	}

}
