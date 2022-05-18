package ca.robinssoftware.nationstates;

public enum NationRank {

    LEADER, COUNCIL, OFFICER, CITIZEN, RESIDENT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public boolean inherits(NationRank rank) {
        switch (rank) {
        case CITIZEN:
            if (this == RESIDENT)
                return true;
        case OFFICER:
            if (this == OFFICER)
                return true;
        case COUNCIL:
            if (this == COUNCIL)
                return true;
        case LEADER:
            if (this == LEADER)
                return true;
        default:
            return false;
        }
    }

}
