package com.smk.funnymessenger.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("from_user_id")
	@Expose
	private Integer fromUserId;
	@SerializedName("to_user_id")
	@Expose
	private Integer toUserId;
	@SerializedName("message")
	@Expose
	private String message;
	@SerializedName("type")
	@Expose
	private String type;
	@SerializedName("seen")
	@Expose
	private Boolean seen;
	@SerializedName("delivered")
	@Expose
	private Boolean delivered;
	@SerializedName("created_at")
	@Expose
	private String createdAt;
	@SerializedName("updated_at")
	@Expose
	private String updatedAt;

	private Boolean isSelf;

	public Message(){

	}

	public Message(Integer fromUserId, Integer toUserId, String message, String type) {
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.message = message;
		this.type = type;
	}

	/**
	 * 
	 * @return The id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The fromUserId
	 */
	public Integer getFromUserId() {
		return fromUserId;
	}

	/**
	 * 
	 * @param fromUserId
	 *            The from_user_id
	 */
	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

	/**
	 * 
	 * @return The toUserId
	 */
	public Integer getToUserId() {
		return toUserId;
	}

	/**
	 * 
	 * @param toUserId
	 *            The to_user_id
	 */
	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	/**
	 * 
	 * @return The message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message
	 *            The message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return The type
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            The type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return The seen
	 */
	public Boolean getSeen() {
		return seen;
	}

	/**
	 * 
	 * @param seen
	 *            The seen
	 */
	public void setSeen(Boolean seen) {
		this.seen = seen;
	}

	/**
	 * 
	 * @return The delivered
	 */
	public Boolean getDelivered() {
		return delivered;
	}

	/**
	 * 
	 * @param delivered
	 *            The delivered
	 */
	public void setDelivered(Boolean delivered) {
		this.delivered = delivered;
	}

	/**
	 * 
	 * @return The createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * 
	 * @param createdAt
	 *            The created_at
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * 
	 * @return The updatedAt
	 */
	public String getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * 
	 * @param updatedAt
	 *            The updated_at
	 */
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean isSelf() {
		return isSelf;
	}

	public void setSelf(Boolean self) {
		isSelf = self;
	}
}