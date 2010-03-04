package com.calclab.emite.im.client.roster;

import java.util.Comparator;

public class RosterItemsOrder {

    public static final Comparator<RosterItem> byName = new Comparator<RosterItem>() {
	@Override
	public int compare(final RosterItem item1, final RosterItem item2) {
	    if (item1.name != null && item2.name != null) {
		return item1.name.compareTo(item2.name);
	    } else {
		return item1.jid.toString().compareTo(item2.jid.toString());
	    }

	}
    };
    public static final Comparator<RosterItem> byAvailability = new Comparator<RosterItem>() {
	@Override
	public int compare(final RosterItem item1, final RosterItem item2) {
	    if (item1.isAvailable() && !item2.isAvailable()) {
		return -1;
	    } else if (!item1.isAvailable() && item2.isAvailable()) {
		return 1;
	    } else {
		return 0;
	    }
	}
    };
    public static Comparator<RosterItem> groupedFirst = new Comparator<RosterItem>() {
	@Override
	public int compare(final RosterItem item1, final RosterItem item2) {
	    final Integer item1Size = item1.groups.size();
	    final Integer item2Size = item2.groups.size();
	    System.out.print("GF: " + item1 + item1Size + "-" + item2 + item2Size + ": "
		    + item2Size.compareTo(item2Size));
	    return item2Size.compareTo(item1Size);
	}
    };

    public static Comparator<RosterItem> order(final Comparator<RosterItem>... comparators) {
	return new Comparator<RosterItem>() {
	    @Override
	    public int compare(final RosterItem o1, final RosterItem o2) {
		int result;
		for (final Comparator<RosterItem> order : comparators) {
		    result = order.compare(o1, o2);
		    if (result != 0) {
			return result;
		    }
		}
		return 0;
	    }
	};
    }

}
