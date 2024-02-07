package com.projeto.appspringthymeleaf.record;

public record AlertRecord(String type, String title, String message) {

	public boolean isEmpty() {
		return type == null && title == null && message == null;
	}
}
