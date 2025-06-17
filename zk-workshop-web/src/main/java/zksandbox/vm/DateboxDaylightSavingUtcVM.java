package zksandbox.vm;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

public class DateboxDaylightSavingUtcVM {

//	private static final ZoneId ZONEID_DEFAULT = ZoneId.systemDefault();
	private static final ZoneId ZONEID_DEFAULT = ZoneId.of("UTC");
	private static final String DF_CZ_PATT = "dd.MM.yyyy";
	private Date dateA;
	private Date dateB;

	@Init
	private void init() {
		LocalDate localDateFromBackend = LocalDate.now(ZONEID_DEFAULT);
		setDateA(transDate(localDateFromBackend));
	}

	@Command
	@NotifyChange("dateB")
	public void dateAChangedCmd() {
		LocalDate localDateForBackend = transLocalDate(getDateA());
		setDateB(transDate(localDateForBackend));
	}

	/** Prevod na Date, null na null.
	 */
	public static Date transDate(LocalDate dd) {
		if (dd == null) {
			return null;
		}
		return Date.from(dd.atStartOfDay(ZONEID_DEFAULT).toInstant());
	}

	/** Prevod na LocalDate, null na null.
	 */
	public static LocalDate transLocalDate(Date dd) {
		if (dd == null) {
			return null;
		}
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(dd.getTime()), ZONEID_DEFAULT).toLocalDate();
	}

	public String getDefaultDateFormatMask() {
    	return DF_CZ_PATT;
    }

	public Date getDateA() {
		System.out.println("getDateA: " + dateA);
		return dateA;
	}
	public void setDateA(Date dateA) {
		System.out.println("setDateA: " + dateA);
		this.dateA = dateA;
	}
	public Date getDateB() {
		System.out.println("getDateB: " + dateB);
		return dateB;
	}
	public void setDateB(Date dateB) {
		System.out.println("setDateB: " + dateB);
		this.dateB = dateB;
	}

}
