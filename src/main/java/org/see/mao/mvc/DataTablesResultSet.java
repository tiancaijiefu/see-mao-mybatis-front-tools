package org.see.mao.mvc;

import java.util.Collections;
import java.util.List;

import org.see.mao.dto.PageArrayList;

/**
 * @author mumu@yfyang
 * @author Joshua Wang
 * @since JDK 1.8
 */
public final class DataTablesResultSet<T> {
    private final int sEcho;
    private final long iTotalRecords;
    private final long iTotalDisplayRecords;
    private final List<T> aaData;

    /**
     * Instantiates a new Data tables result set.
     *
     * @param sEcho the pc
     * @param rs the rs
     */
    public DataTablesResultSet(int sEcho, PageArrayList<T> rs) {
        this.sEcho = sEcho;
        this.aaData = rs;
        this.iTotalRecords = rs.getTotal();
        this.iTotalDisplayRecords = rs.getTotal();
    }

    /**
     * Gets echo.
     *
     * @return the echo
     */
    public int getsEcho() {
        return sEcho;
    }

    /**
     * Gets total records.
     *
     * @return the total records
     */
    public long getiTotalRecords() {
        return iTotalRecords;
    }

    /**
     * Gets total display records.
     *
     * @return the total display records
     */
    public long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    /**
     * Gets aa data.
     *
     * @return the aa data
     */
    public List<T> getAaData() {
        return Collections.unmodifiableList(aaData);
    }
}
