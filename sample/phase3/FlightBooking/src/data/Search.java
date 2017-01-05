package data;

import java.util.*;
import managers.MainSystem;

/**
 * A class representing a searching.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public abstract class Search<T> {

    /** The search result. */
    protected List<T> searchResult;

    /** The system of this flight booking application. */
    protected MainSystem system;

    /**
     * Creates a Search with the given MainSystem.
     * @param system the system of this flight booking application
     */
    public Search(MainSystem system) {
        this.system = system;
    }

    /**
     * Returns the search result.
     * @return the search result
     */
    public String getSearchResult() {
        String res = "";
        for (T element : searchResult) {
            res += element.toString() + "\n";
        }
        return res;
    }
}
