package zksandbox.model.tree;


/** Interface, ktere musi splnovat elementy zabalene do nodu naseho stromu  */
public interface TreeElement {
	/** Nastaveni priznaku, ze s elementem se hybalo - doslo k presunu v stromu a
	 * to uzivatelem, tj. nikoli tim, ze se pred nej presunul, nebo zrusil jiny nod
	 * @param moved
	 */
	void setMoved(boolean moved);
	boolean isMoved();
	/* BasicEntity */
	long getId();
	void setId(long id);
}
