package com.smk.funnymessager.database.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smk.funnymessager.database.DatabaseManager;
import com.smk.funnymessager.database.OnDelete;
import com.smk.funnymessager.database.OnSave;
import com.smk.funnymessager.database.OnSelect;
import com.smk.funnymessager.database.OnUpdate;
import com.smk.funnymessager.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * DEVELOP BY SMK
 * YOU DON'T FORGET TO CHANGE DATABASE NAME IN THE DATABASE MANAGER CLASS;
 */

public class MessageController extends DatabaseManager<Message> {

	//To define table name;
	private static final String TABLE = "tbl_message";
	//To define field name array;
	private static final String[] FIELD = { "id", "from_user_id", "to_user_id", "message", "type", "seen", "delivered", "created_at", "updated_at", "is_self" };

	public MessageController(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
		// To define listener for save, select, update, delete;
		setOnSave(onSaveRecords);
		setOnSelect(onSelectRecords);
		setOnUpate(onUpdateRecords);
		setOnDelete(onDeleteRecords);
	}

	@Override
	protected void createTables() {
		// TODO Auto-generated method stub
		// To define SQL command for table create;
		connectSQLiteDatabase.execSQL("CREATE TABLE  IF NOT EXISTS  "
				+ TABLE 	+ " ("
				+ FIELD[0] + " INT PRIMARY KEY,"
				+ FIELD[1] + " INT NOT NULL,"
				+ FIELD[2] + " INT NOT NULL,"
				+ FIELD[3] + " TEXT NOT NULL,"
				+ FIELD[4] + " TEXT NOT NULL,"
				+ FIELD[5] + " TEXT NULL,"
				+ FIELD[6] + " TEXT NULL,"
				+ FIELD[7] + " TEXT NOT NULL,"
				+ FIELD[8] + " TEXT NOT NULL,"
				+ FIELD[9] + " TEXT NOT NULL)");
	}

	private OnSave<Message> onSaveRecords = new OnSave<Message>() {

		public void saveRecord(Message value) {
			// TODO Auto-generated method stub
			// To define here for save to Message;
			// ---------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.beginTransaction();
			try {
				/**
				 * YOU DON'T FORGET UNCOMMENT VALUE PUT METHODS "REMOVE //"
				 */
				ContentValues values = new ContentValues();
				values.put(FIELD[0], value.getId());
				values.put(FIELD[1], value.getFromUserId());
				values.put(FIELD[2], value.getToUserId());
				values.put(FIELD[3], value.getMessage());
				values.put(FIELD[4], value.getType());
				values.put(FIELD[5], value.getSeen());
				values.put(FIELD[6], value.getDelivered());
				values.put(FIELD[7], value.getCreatedAt());
				values.put(FIELD[8], value.getUpdatedAt());
				values.put(FIELD[9], value.isSelf());

				db.insert(TABLE, null, values);
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
				if (complete != null) {
					complete.onComplete();
				}
			}
		}

		public void saveRecord(List<Message> value) {
			// TODO Auto-generated method stub
			// To define here for save to Message list;
			// --------------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.beginTransaction();
			try {
				/**
				 * YOU DON'T FORGET UNCOMMENT VALUE PUT METHODS "REMOVE //";
				 */
				for (Message message : value) {

					ContentValues values = new ContentValues();
					values.put(FIELD[0], message.getId());
					values.put(FIELD[1], message.getFromUserId());
					values.put(FIELD[2], message.getToUserId());
					values.put(FIELD[3], message.getMessage());
					values.put(FIELD[4], message.getType());
					values.put(FIELD[5], message.getSeen());
					values.put(FIELD[6], message.getDelivered());
					values.put(FIELD[7], message.getCreatedAt());
					values.put(FIELD[8], message.getUpdatedAt());
					values.put(FIELD[9], message.isSelf());

					db.insert(TABLE, null, values);
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
				if (complete != null) {
					complete.onComplete();
				}
			}
		}
	};

	private OnSelect<Message> onSelectRecords = new OnSelect<Message>() {

		public Message selectRecord(Integer arg0) {
			// TODO Auto-generated method stub
			// To define here for select Message by String arg0;
			// Maybe as Message id;
			// --------------------------------------
			Message message = null;
			try {

				String[] VALUE = { arg0.toString() };
				String WHERE = FIELD[0] + "=? ";
				String ORDER_BY = FIELD[0] + " ASC";

				SQLiteDatabase db = getReadableDatabase();
				Cursor cursor = db.query(TABLE, FIELD, WHERE, VALUE, null,null, ORDER_BY);
				try {
					if (cursor.moveToFirst()) {
						do {
							message = new Message();
							message.setId(cursor.getInt(0));
							message.setFromUserId(cursor.getInt(1));
							message.setToUserId(cursor.getInt(2));
							message.setMessage(cursor.getString(3));
							message.setType(cursor.getString(4));
							message.setSeen(cursor.getString(5).equals("1") ? true : false);
							message.setDelivered(cursor.getString(6).equals("1") ? true : false);
							message.setCreatedAt(cursor.getString(7));
							message.setUpdatedAt(cursor.getString(8));
							message.setSelf(Boolean.valueOf(cursor.getString(9)));

						} while (cursor.moveToNext());
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					cursor.close();
					db.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				if (complete != null) {
					complete.onComplete();
				}
			}
			return message;
		}

		public List<Message> selectRecords() {
			// TODO Auto-generated method stub
			// To define here for select all Message;
			// --------------------------------------
			List<Message> list = new ArrayList<Message>();
			try {

				String ORDER_BY = FIELD[0] + " ASC";

				SQLiteDatabase db = getReadableDatabase();
				Cursor cursor = db.query(TABLE, FIELD, null, null, null,null, ORDER_BY);
				try {
					if (cursor.moveToFirst()) {
						do {
							Message message = new Message();
							message.setId(cursor.getInt(0));
							message.setFromUserId(cursor.getInt(1));
							message.setToUserId(cursor.getInt(2));
							message.setMessage(cursor.getString(3));
							message.setType(cursor.getString(4));
							message.setSeen(cursor.getString(5).equals("1") ? true : false);
							message.setDelivered(cursor.getString(6).equals("1") ? true : false);
							message.setCreatedAt(cursor.getString(7));
							message.setUpdatedAt(cursor.getString(8));
							message.setSelf(Boolean.valueOf(cursor.getString(9)));
							list.add(message);
						} while (cursor.moveToNext());
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					cursor.close();
					db.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				if (complete != null) {
					complete.onComplete();
				}
			}
			return list;
		}

		public List<Message> selectRecords(String arg0) {
			// TODO Auto-generated method stub
			// To define here for select Message list by String arg0;
			// Maybe as Message name;
			// --------------------------------------
			List<Message> list = new ArrayList<Message>();
			try {

				String[] VALUE = { arg0, arg0 };
				String WHERE = FIELD[1] + "=? or "+FIELD[2] + "=? ";
				String ORDER_BY = FIELD[0] + " ASC";

				SQLiteDatabase db = getReadableDatabase();
				Cursor cursor = db.query(TABLE, FIELD, WHERE, VALUE, null,null, ORDER_BY);
				try {
					if (cursor.moveToFirst()) {
						do {
							Message message = new Message();
							message.setId(cursor.getInt(0));
							message.setFromUserId(cursor.getInt(1));
							message.setToUserId(cursor.getInt(2));
							message.setMessage(cursor.getString(3));
							message.setType(cursor.getString(4));
							message.setSeen(cursor.getString(5).equals("1") ? true : false);
							message.setDelivered(cursor.getString(6).equals("1") ? true : false);
							message.setCreatedAt(cursor.getString(7));
							message.setUpdatedAt(cursor.getString(8));
							message.setSelf(cursor.getString(9).equals("1") ? true : false);
							list.add(message);
						} while (cursor.moveToNext());
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					cursor.close();
					db.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				if (complete != null) {
					complete.onComplete();
				}
			}
			return list;
		}

		public List<Message> selectRecords(Message value) {
			// TODO Auto-generated method stub
			// To define here for select Message list by String arg0;
			// Maybe as multiple Message name;
			// --------------------------------------
			List<Message> list = new ArrayList<Message>();
			try {

				String[] VALUE = { value.getFromUserId().toString(),value.getSeen().toString() };
				String WHERE = FIELD[1] + "=? and "+FIELD[5] + "=? ";
				String ORDER_BY = FIELD[0] + " ASC";

				SQLiteDatabase db = getReadableDatabase();
				Cursor cursor = db.query(TABLE, FIELD, WHERE, VALUE, null,null, ORDER_BY);
				try {
					if (cursor.moveToFirst()) {
						do {
							Message message = new Message();
							message.setId(cursor.getInt(0));
							message.setFromUserId(cursor.getInt(1));
							message.setToUserId(cursor.getInt(2));
							message.setMessage(cursor.getString(3));
							message.setType(cursor.getString(4));
							message.setSeen(cursor.getString(5).equals("1") ? true : false);
							message.setDelivered(cursor.getString(6).equals("1") ? true : false);
							message.setCreatedAt(cursor.getString(7));
							message.setUpdatedAt(cursor.getString(8));
							message.setSelf(Boolean.valueOf(cursor.getString(9)));
							list.add(message);
						} while (cursor.moveToNext());
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					cursor.close();
					db.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				if (complete != null) {
					complete.onComplete();
				}
			}
			return list;
		}
	};

	private OnUpdate<Message> onUpdateRecords = new OnUpdate<Message>() {

		public void updateRecord(Message message) {
			// TODO Auto-generated method stub
			// To define here for update an Message;
			// --------------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.beginTransaction();
			try {
				/**
				 * YOU DON'T FORGET UNCOMMENT VALUE PUT METHODS "REMOVE //"
				 */
				ContentValues values = new ContentValues();
				values.put(FIELD[0], message.getId());
				values.put(FIELD[1], message.getFromUserId());
				values.put(FIELD[2], message.getToUserId());
				values.put(FIELD[3], message.getMessage());
				values.put(FIELD[4], message.getType());
				values.put(FIELD[5], message.getSeen());
				values.put(FIELD[6], message.getDelivered());
				values.put(FIELD[7], message.getCreatedAt());
				values.put(FIELD[8], message.getUpdatedAt());
				values.put(FIELD[9], message.isSelf());

				db.update(TABLE, values, FIELD[0]+"=? ", new String[]{message.getId().toString()});
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
				if (complete != null) {
					complete.onComplete();
				}
			}
		}

		public void updateReourd(List<Message> value) {
			// TODO Auto-generated method stub
			// To define here for update the Message list;
			// --------------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.beginTransaction();
			try {
				/**
				 * YOU DON'T FORGET UNCOMMENT VALUE PUT METHODS "REMOVE //";
				 */
				for (Message message : value) {

					ContentValues values = new ContentValues();
					values.put(FIELD[0], message.getId());
					values.put(FIELD[1], message.getFromUserId());
					values.put(FIELD[2], message.getToUserId());
					values.put(FIELD[3], message.getMessage());
					values.put(FIELD[4], message.getType());
					values.put(FIELD[5], message.getSeen());
					values.put(FIELD[6], message.getDelivered());
					values.put(FIELD[7], message.getCreatedAt());
					values.put(FIELD[8], message.getUpdatedAt());
					values.put(FIELD[9], message.isSelf());

					db.update(TABLE, values, FIELD[0]+"=? ", new String[]{message.getId().toString()});
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
				if (complete != null) {
					complete.onComplete();
				}
			}
		}
	};

	private OnDelete onDeleteRecords = new OnDelete() {

		public void deleteRecord(String arg0) {
			// TODO Auto-generated method stub
			// To define here for delete an Message by id or Message's field;
			// --------------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TABLE, FIELD[0]+"=? ", new String[]{ arg0 });
			db.close();
		}

		public void deleteRecord() {
			// TODO Auto-generated method stub
			// To define here for delete all Message;
			// --------------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TABLE, null, null);
			db.close();
		}
	};
	/**
	 * return string id;
	 * format ##-00000001;
	 */
	@Override
	public String generateAutoId() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		Integer MessageId = 1;
		try {
			String ORDER_BY = FIELD[0] + " DESC LIMIT 1";
			cursor  = db.query(TABLE, FIELD, null, null, null,null, ORDER_BY);
			if (cursor.moveToFirst()) {
				do {
					MessageId = cursor.getInt(0);
					MessageId = Integer.valueOf(MessageId) + 1;
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			cursor.close();
			db.close();
		}
		return MessageId.toString();
	}

}
