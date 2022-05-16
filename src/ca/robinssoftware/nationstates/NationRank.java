package ca.robinssoftware.nationstates;

public enum NationRank {
    
    LEADER, COUNCIL, OFFICER, CITIZEN, RESIDENT;
    
    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
