package com.smk.funnymessager.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.List;

public abstract class DatabaseManager<E>{
	protected Context mContext;
	protected SQLiteDatabase connectSQLiteDatabase = null;
	protected String DATABASE_FILE_PATH = "/data/data/";
	protected final String DATABASE_NAME = "funny_messager_database.sqlite3";
	
	protected OnSave<E> save;
	protected OnUpdate<E> update;
	protected OnSelect<E> select;
	protected OnDelete delete;
	
	protected OnComplete complete;
	
	public DatabaseManager(Context ctx)
	{
		mContext=ctx;
		//To Create Database
		try {
			DATABASE_FILE_PATH += mContext.getApplicationContext().getPackageName()+ "/databases";
			connectSQLiteDatabase = mContext.openOrCreateDatabase(DATABASE_FILE_PATH + File.separator + DATABASE_NAME, Context.MODE_PRIVATE, null);
		} catch (Exception e) {
			Log.e("MYERROR","Cann't crate database!!!!!!!!!!");
		}
		finally
		{
			createTables();
			connectSQLiteDatabase.close();
		}
				
	}
	
	/*To Check Readable to Database*/
	protected SQLiteDatabase getReadableDatabase()
	{
	    connectSQLiteDatabase = mContext.openOrCreateDatabase(DATABASE_FILE_PATH + File.separator + DATABASE_NAME, Context.MODE_PRIVATE, null);
	    return connectSQLiteDatabase;
	}
	/*To Check Writeable to Database*/
	protected SQLiteDatabase getWritableDatabase()
	{
	    connectSQLiteDatabase = mContext.openOrCreateDatabase(DATABASE_FILE_PATH + File.separator + DATABASE_NAME, Context.MODE_PRIVATE, null);
	    return connectSQLiteDatabase;
	}
		
	protected abstract void createTables();
	
	public abstract String generateAutoId();
	
	/*Create to folder in external sd card*/
	public Boolean IfExistDatabase() {
		boolean ret = true;
		File file = new File("", DATABASE_FILE_PATH + File.separator + DATABASE_NAME);
		if (!file.exists()) {
			ret = false;
		}
		return ret;
	}
	
	/*Save Record*/
	public void save(E obj){
		save.saveRecord(obj);
	}
	
	/*Save All Record*/
	public void save(List<E> objList){
		save.saveRecord(objList);
	}
	
	/*Set Implemention on Save*/
	public void setOnSave(OnSave<E> save){
		this.save = save;
	}
	
	/*Update Record*/
	public void update(E obj){
		update.updateRecord(obj);
	}
	
	/*Set Implemention on Update*/
	public void setOnUpate(OnUpdate<E> update){
		this.update = update;
	}
	
	/*Select Records*/
	public List<E> select(){
		return select.selectRecords();
	}
	
	/*Select Records*/
	public E select(Integer arg0){
		return select.selectRecord(arg0);
	}
	
	/*Select Records by arg0*/
	public List<E> select(String arg0){
		return select.selectRecords(arg0);
	}
	
	/*Select Records by Object*/
	public List<E> select(E obj){
		return select.selectRecords(obj);
	}
	
	/*Set Implemention on Select*/
	public void setOnSelect(OnSelect<E> select){
		this.select = select;
	}
	
	/*Delete All Records*/
	public void delete(){
		delete.deleteRecord();
	}
	
	/*Delete Record*/
	public void delete(String arg0){
		delete.deleteRecord(arg0);
	}
	
	/*Set Implemention on Select*/
	public void setOnDelete(OnDelete delete){
		this.delete = delete;
	}
	
	/*Set Listener on Complete */
	public void setOnComplete(OnComplete complete){
		this.complete = complete;
	}
	
}
