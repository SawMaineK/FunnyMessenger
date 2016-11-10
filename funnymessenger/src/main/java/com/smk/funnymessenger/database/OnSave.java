package com.smk.funnymessenger.database;

import java.util.List;

public interface OnSave<E> {
	public void saveRecord(E value);
	public void saveRecord(List<E> value);
}
