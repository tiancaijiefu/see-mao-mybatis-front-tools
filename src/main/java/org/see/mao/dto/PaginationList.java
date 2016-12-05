package org.see.mao.dto;

import java.util.Collection;
import java.util.List;

import org.see.mao.dto.datatables.PagingCriteria;

/**
 * @author Joshua Wang
 * @param <E>
 * @date 2016年7月20日
 */
public class PaginationList<E> extends PageArrayList<E> {
	
	private static final long serialVersionUID = 1L;
	
	/** default page number. */
	private static final int DEFAULT_NUM = 1;
	
    /** default page size. */
    private static final int DEFAULT_SIZE = 2;
	
	/**
	 * 分页参数
	 */
	public int pageNo = DEFAULT_NUM; //页码
	
	public int pageSize = DEFAULT_SIZE; //每页显示条数
	
	public long pageCount; // 页面总数
	
	public long rowCount; // 总记录数

	public int displayStart; // 开始数

	/**
	 * @return the pageNo
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * @param pageNo
	 *            the pageNo to set
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the pageCount
	 */
	public long getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount
	 *            the pageCount to set
	 */
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * @return the rowCount
	 */
	public long getRowCount() {
		return rowCount;
	}

	/**
	 * @param rowCount
	 *            the rowCount to set
	 */
	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
		if (rowCount % pageSize == 0) {
			pageCount = rowCount / pageSize;
		} else {
			pageCount = rowCount / pageSize + 1;
		}
		this.setPageCount(pageCount);
	}

	/**
	 * @return the displaySize
	 */
	public int getDisplayStart() {
		displayStart = pageNo * pageSize - pageSize;
		return displayStart;
	}

	/**
	 * @param displaySize
	 *            the displaySize to set
	 */
	public void setDisplayStart(int displayStart) {
		this.displayStart = displayStart;
	}
	
	public PagingCriteria getPagingCriteria() {
		PagingCriteria pageCriteria = PagingCriteria.createCriteria(getPageSize(),getDisplayStart(), getPageNo());
		return pageCriteria;
	}
	
	/**
     * Instantiates a new Page my batis.
     * @param content  the content
     * @param pageable the pageable
     * @param total    the total
	 */
	public PaginationList(Collection<? extends E> content, PagingCriteria pageable, long total) {
		super(content, pageable, total);
		this.pageNo = pageable.getPageNumber();
		this.pageSize = pageable.getDisplaySize();
		setRowCount(total);
	}
	
	/**
     * Instantiates a new Page my batis.
     * @param content the content
	 */
	public PaginationList(List<E> content) {
		super(content);
	}
	
	/**
	 * Instantiates a new Page my batis no Parameteres
	 */
	public PaginationList(){
		super();
	}
	
	/**
	 * Instantiates a new Page my batis no Parameteres
	 */
	public PaginationList(int pageSize){
		super();
		this.pageSize = pageSize;
	}
	
}
