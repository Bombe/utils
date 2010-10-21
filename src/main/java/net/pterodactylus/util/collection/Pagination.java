/*
 * utils - Pagination.java - Copyright © 2010 David Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.util.collection;

import java.util.List;

/**
 * Helper class for lists that need pagination. Setting the page or the page
 * size will automatically recalculate all other parameters, and the next call
 * to {@link #getItems()} retrieves all items on the current page.
 *
 * @param <T>
 *            The type of the list elements
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Pagination<T> {

	/** The list to paginate. */
	private final List<T> list;

	/** The page size. */
	private int pageSize;

	/** The current page, 0-based. */
	private int page;

	/** The total number of pages. */
	private int pageCount;

	/**
	 * Paginates the given list.
	 *
	 * @param list
	 *            The list to paginate
	 * @param pageSize
	 *            The page size
	 */
	public Pagination(List<T> list, int pageSize) {
		this.list = list;
		this.pageSize = pageSize;
		pageCount = (list.size() - 1) / pageSize + 1;
	}

	//
	// ACCESSORS
	//

	/**
	 * Returns the current page, 0-based.
	 *
	 * @return The current page, 0-based
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Returns the current page, 1-based.
	 *
	 * @return The current page, 1-based
	 */
	public int getPageNumber() {
		return page + 1;
	}

	/**
	 * Sets the new page. If the new page is out of range it is silently
	 * corrected.
	 *
	 * @param page
	 *            The new page number
	 * @return This pagination helper (for method chaining)
	 */
	public Pagination<T> setPage(int page) {
		if (page < 0) {
			this.page = 0;
		} else if (page >= pageCount) {
			this.page = pageCount - 1;
		} else {
			this.page = page;
		}
		return this;
	}

	/**
	 * Returns the total number of pages.
	 *
	 * @return The total number of pages
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * Returns the number of items per page.
	 *
	 * @return The number of items per page
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the page size. The page is adjusted so that the first item on the
	 * old page is still contained in the new page. A page size of less than 1
	 * is silently corrected to 1.
	 *
	 * @param pageSize
	 *            The new page size
	 * @return This pagination helper (for method chaining)
	 */
	public Pagination<T> setPageSize(int pageSize) {
		int newPageSize = (pageSize < 1) ? 1 : pageSize;
		int index = page * this.pageSize;
		this.pageSize = newPageSize;
		pageCount = (list.size() - 1) / newPageSize + 1;
		page = index / newPageSize;
		return this;
	}

	/**
	 * Returns the number of items on the current page. For all but the last
	 * page this will equal the page size.
	 *
	 * @return The number of items on the current page
	 */
	public int getItemCount() {
		return Math.min(pageSize, list.size() - page * pageSize);
	}

	/**
	 * Returns the items on the current page.
	 *
	 * @return The items on the current page
	 */
	public List<T> getItems() {
		return list.subList(page * pageSize, page * pageSize + getItemCount());
	}

	/**
	 * Returns whether the current page is the first page
	 *
	 * @return {@code true} if the current page is the first page, {@code false}
	 *         otherwise
	 */
	public boolean isFirst() {
		return page == 0;
	}

	/**
	 * Returns whether the current page is the last page.
	 *
	 * @return {@code true} if the current page is the last page, {@code false}
	 *         otherwise
	 */
	public boolean isLast() {
		return page == (pageCount - 1);
	}

	/**
	 * Returns whether pagination is actually necessary, i.e. if the number of
	 * pages is greater than 1.
	 *
	 * @return {@code true} if there are more than one page in this pagination,
	 *         {@code false} otherwise
	 */
	public boolean isNecessary() {
		return pageCount > 1;
	}

	/**
	 * Returns the index of the previous page. {@link #isFirst()} should be
	 * called first to determine whether there is a page before the current
	 * page.
	 *
	 * @return The index of the previous page
	 */
	public int getPreviousPage() {
		return page - 1;
	}

	/**
	 * Returns the index of the next page. {@link #isLast()} should be called
	 * first to determine whether there is a page after the current page.
	 *
	 * @return The index of the next page
	 */
	public int getNextPage() {
		return page + 1;
	}

	/**
	 * Returns the index of the last page.
	 *
	 * @return The index of the last page
	 */
	public int getLastPage() {
		return pageCount - 1;
	}

}
