package com.calclab.emite.im.client.roster;

import java.util.Comparator;

public class RosterItemsOrder {

    public static final Comparator<RosterItem> byName = new Comparator<RosterItem>() {
        @Override
        public int compare(final RosterItem item1, final RosterItem item2) {
            final String item1Name = item1.getName() == null ? item1.getJID().toString() : item1.getName();
            final String item2Name = item2.getName() == null ? item2.getJID().toString() : item2.getName();

            return item1Name.compareToIgnoreCase(item2Name);
        }
    };

    /**
     * Available first. Inside availables, items with getShow() == Show.dnd last
     */
    public static final Comparator<RosterItem> byAvailability = new Comparator<RosterItem>() {
        @Override
        public int compare(final RosterItem item1, final RosterItem item2) {
            final boolean av1 = item1.isAvailable();
            final boolean av2 = item2.isAvailable();

            if (av1 && !av2) {
                return -1;
            } else if (!av1 && av2) {
                return 1;
            } else if (av1 && av2) {
                return getShowLevel(item2) - getShowLevel(item1);
            } else {
                return 0;
            }

        }

        private int getShowLevel(final RosterItem item) {
            switch (item.getShow()) {
            case dnd:
                return 1;
            case away:
            case xa:
                return 2;
            default:
                return 3;
            }
        }
    };

    public static Comparator<RosterItem> groupedFirst = new Comparator<RosterItem>() {
        @Override
        public int compare(final RosterItem item1, final RosterItem item2) {
            final Integer item1Size = item1.getGroups().size();
            final Integer item2Size = item2.getGroups().size();

            if (((item1Size == 0) && (item2Size == 0)) || ((item1Size > 0) && (item2Size > 0))) {
                return 0;
            } else if (item1Size == 0) {
                return 1;
            } else {
                return -1;
            }
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
