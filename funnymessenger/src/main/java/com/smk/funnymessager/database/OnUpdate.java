package com.smk.funnymessager.database;

import java.util.List;

public interface OnUpdate<E> {
	public void updateRecord(E obj);
	public void updateReourd(List<E> obj);
}
