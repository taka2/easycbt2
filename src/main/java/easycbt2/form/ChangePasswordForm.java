package easycbt2.form;

import javax.validation.constraints.NotBlank;

import easycbt2.model.User;

public class ChangePasswordForm {
	private User user;
	@NotBlank
	private String currentPassword;
	@NotBlank
	private String newPassword;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
