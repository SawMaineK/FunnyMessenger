package com.smk.funnymessager.database;

import java.util.List;

public interface OnSelect<E> {
	public E selectRecord(Integer arg0);
	public List<E> selectRecords();
	public List<E> selectRecords(String arg0);
	public List<E> selectRecords(E value);
}
