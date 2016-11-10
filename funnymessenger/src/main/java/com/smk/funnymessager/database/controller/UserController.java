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
import com.smk.funnymessager.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * DEVELOP BY SMK
 * YOU DON'T FORGET TO CHANGE DATABASE NAME IN THE DATABASE MANAGER CLASS;
 */

public class UserController extends DatabaseManager<User> {

	//To define table name;
	private static final String TABLE = "tbl_user";
	//To define field name array;
	private static final String[] FIELD = { "id", "name", "email", "phone", "image", "lat", "lng", "on_session", "message", "updated_at" };

	public UserController(Context ctx) {
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
				+ FIELD[1] + " TEXT NOT NULL,"
				+ FIELD[2] + " TEXT NOT NULL,"
				+ FIELD[3] + " TEXT NULL,"
				+ FIELD[4] + " TEXT NULL,"
				+ FIELD[5] + " TEXT NULL,"
				+ FIELD[6] + " TEXT NULL,"
				+ FIELD[7] + " TEXT NULL,"
				+ FIELD[8] + " TEXT NULL,"
				+ FIELD[9] + " TEXT NULL)");
	}

	private OnSave<User> onSaveRecords = new OnSave<User>() {

		public void saveRecord(User value) {
			// TODO Auto-generated method stub
			// To define here for save to User;
			// ---------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.beginTransaction();
			try {
				/**
				 * YOU DON'T FORGET UNCOMMENT VALUE PUT METHODS "REMOVE //"
				 */
				ContentValues values = new ContentValues();
				values.put(FIELD[0], value.getId());
				values.put(FIELD[1], value.getName());
				values.put(FIELD[2], value.getEmail());
				values.put(FIELD[3], value.getPhone());
				values.put(FIELD[4], value.getImage());
				values.put(FIELD[5], value.getLat());
				values.put(FIELD[6], value.getLng());
				values.put(FIELD[7], value.getOnSession());
				values.put(FIELD[8], value.getMessage());
				values.put(FIELD[9], value.getUpdatedAt());

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

		public void saveRecord(List<User> value) {
			// TODO Auto-generated method stub
			// To define here for save to User list;
			// --------------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.beginTransaction();
			try {
				/**
				 * YOU DON'T FORGET UNCOMMENT VALUE PUT METHODS "REMOVE //";
				 */
				for (User user : value) {

					ContentValues values = new ContentValues();
					values.put(FIELD[0], user.getId());
					values.put(FIELD[0], user.getId());
					values.put(FIELD[1], user.getName());
					values.put(FIELD[2], user.getEmail());
					values.put(FIELD[3], user.getPhone());
					values.put(FIELD[4], user.getImage());
					values.put(FIELD[5], user.getLat());
					values.put(FIELD[6], user.getLng());
					values.put(FIELD[7], user.getOnSession());
                    values.put(FIELD[8], user.getMessage());
                    values.put(FIELD[9], user.getUpdatedAt());

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

	private OnSelect<User> onSelectRecords = new OnSelect<User>() {

		public User selectRecord(Integer arg0) {
			// TODO Auto-generated method stub
			// To define here for select User by String arg0;
			// Maybe as User id;
			// --------------------------------------
			User user = null;
			try {

				String[] VALUE = { arg0.toString() };
				String WHERE = FIELD[0] + "=? ";
				String ORDER_BY = FIELD[0] + " ASC";

				SQLiteDatabase db = getReadableDatabase();
				Cursor cursor = db.query(TABLE, FIELD, WHERE, VALUE, null,null, ORDER_BY);
				try {
					if (cursor.moveToFirst()) {
						do {
							user = new User();
							user.setId(cursor.getInt(0));
							user.setName(cursor.getString(1));
							user.setEmail(cursor.getString(2));
							user.setPhone(cursor.getString(3));
							user.setImage(cursor.getString(4));
							user.setLat(Double.valueOf(cursor.getString(5)));
							user.setLng(Double.valueOf(cursor.getString(6)));
							user.setOnSession(cursor.getString(7).equals("1") ? true : false);
                            user.setMessage(cursor.getString(8));
                            user.setUpdatedAt(cursor.getString(9));

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
			return user;
		}

		public List<User> selectRecords() {
			// TODO Auto-generated method stub
			// To define here for select all User;
			// --------------------------------------
			List<User> list = new ArrayList<User>();
			try {

				String ORDER_BY = FIELD[0] + " ASC";

				SQLiteDatabase db = getReadableDatabase();
				Cursor cursor = db.query(TABLE, FIELD, null, null, null,null, ORDER_BY);
				try {
					if (cursor.moveToFirst()) {
						do {
							User user = new User();
							user.setId(cursor.getInt(0));
							user.setName(cursor.getString(1));
							user.setEmail(cursor.getString(2));
							user.setPhone(cursor.getString(3));
							user.setImage(cursor.getString(4));
							user.setLat(Double.valueOf(cursor.getString(5)));
							user.setLng(Double.valueOf(cursor.getString(6)));
							user.setOnSession(cursor.getString(7).equals("1") ? true : false);
                            user.setMessage(cursor.getString(8));
                            user.setUpdatedAt(cursor.getString(9));
							list.add(user);
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

		public List<User> selectRecords(String arg0) {
			// TODO Auto-generated method stub
			// To define here for select User list by String arg0;
			// Maybe as User name;
			// --------------------------------------
			List<User> list = new ArrayList<User>();
			try {

				String[] VALUE = { arg0, arg0 };
				String WHERE = FIELD[1] + "=? or "+FIELD[2] + "=? ";
				String ORDER_BY = FIELD[0] + " ASC";

				SQLiteDatabase db = getReadableDatabase();
				Cursor cursor = db.query(TABLE, FIELD, WHERE, VALUE, null,null, ORDER_BY);
				try {
					if (cursor.moveToFirst()) {
						do {
							User user = new User();
							user.setId(cursor.getInt(0));
							user.setName(cursor.getString(1));
							user.setEmail(cursor.getString(2));
							user.setPhone(cursor.getString(3));
							user.setImage(cursor.getString(4));
							user.setLat(Double.valueOf(cursor.getString(5)));
							user.setLng(Double.valueOf(cursor.getString(6)));
							user.setOnSession(cursor.getString(7).equals("1") ? true : false);
                            user.setMessage(cursor.getString(8));
                            user.setUpdatedAt(cursor.getString(9));
							list.add(user);
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

		public List<User> selectRecords(User value) {
			// TODO Auto-generated method stub
			// To define here for select User list by String arg0;
			// Maybe as multiple User name;
			// --------------------------------------
			List<User> list = new ArrayList<User>();
			try {

				String[] VALUE = { value.getName().toString(), value.getEmail().toString() };
				String WHERE = FIELD[1] + "=? and "+FIELD[2] + "=? ";
				String ORDER_BY = FIELD[0] + " ASC";

				SQLiteDatabase db = getReadableDatabase();
				Cursor cursor = db.query(TABLE, FIELD, WHERE, VALUE, null,null, ORDER_BY);
				try {
					if (cursor.moveToFirst()) {
						do {
							User user = new User();
							user.setId(cursor.getInt(0));
							user.setName(cursor.getString(1));
							user.setEmail(cursor.getString(2));
							user.setPhone(cursor.getString(3));
							user.setImage(cursor.getString(4));
							user.setLat(Double.valueOf(cursor.getString(5)));
							user.setLng(Double.valueOf(cursor.getString(6)));
							user.setOnSession(cursor.getString(7).equals("1") ? true : false);
                            user.setMessage(cursor.getString(8));
                            user.setUpdatedAt(cursor.getString(9));
							list.add(user);
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

	private OnUpdate<User> onUpdateRecords = new OnUpdate<User>() {

		public void updateRecord(User user) {
			// TODO Auto-generated method stub
			// To define here for update an User;
			// --------------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.beginTransaction();
			try {
				/**
				 * YOU DON'T FORGET UNCOMMENT VALUE PUT METHODS "REMOVE //"
				 */
				ContentValues values = new ContentValues();
				values.put(FIELD[0], user.getId());
				values.put(FIELD[1], user.getName());
				values.put(FIELD[2], user.getEmail());
				values.put(FIELD[3], user.getPhone());
				values.put(FIELD[4], user.getImage());
				values.put(FIELD[5], user.getLat());
				values.put(FIELD[6], user.getLng());
				values.put(FIELD[7], user.getOnSession());
				values.put(FIELD[8], user.getMessage());
				values.put(FIELD[9], user.getUpdatedAt());

				db.update(TABLE, values, FIELD[0]+"=? ", new String[]{user.getId().toString()});
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

		public void updateReourd(List<User> value) {
			// TODO Auto-generated method stub
			// To define here for update the User list;
			// --------------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.beginTransaction();
			try {
				/**
				 * YOU DON'T FORGET UNCOMMENT VALUE PUT METHODS "REMOVE //";
				 */
				for (User user : value) {

					ContentValues values = new ContentValues();
					values.put(FIELD[0], user.getId());
					values.put(FIELD[1], user.getName());
					values.put(FIELD[2], user.getEmail());
					values.put(FIELD[3], user.getPhone());
					values.put(FIELD[4], user.getImage());
					values.put(FIELD[5], user.getLat());
					values.put(FIELD[6], user.getLng());
					values.put(FIELD[7], user.getOnSession());
					values.put(FIELD[8], user.getMessage());
					values.put(FIELD[9], user.getUpdatedAt());

					db.update(TABLE, values, FIELD[0]+"=? ", new String[]{user.getId().toString()});
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
			// To define here for delete an User by id or User's field;
			// --------------------------------------
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TABLE, FIELD[0]+"=? ", new String[]{ arg0 });
			db.close();
		}

		public void deleteRecord() {
			// TODO Auto-generated method stub
			// To define here for delete all User;
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
		Integer UserId = 1;
		try {
			String ORDER_BY = FIELD[0] + " DESC LIMIT 1";
			cursor  = db.query(TABLE, FIELD, null, null, null,null, ORDER_BY);
			if (cursor.moveToFirst()) {
				do {
					UserId = cursor.getInt(0);
					UserId = Integer.valueOf(UserId) + 1;
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			cursor.close();
			db.close();
		}
		return UserId.toString();
	}

}
