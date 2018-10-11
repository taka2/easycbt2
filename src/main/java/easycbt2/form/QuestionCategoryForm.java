package easycbt2.form;

import javax.validation.constraints.NotBlank;

public class QuestionCategoryForm {
	private Long id;
	@NotBlank
	private String name;
	@NotBlank
	private String scope;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
}
