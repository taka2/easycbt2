package easycbt2.model;

public enum QuestionType {
	SINGLE_CHOICE("single_choice")
	, MULTIPLE_CHOICE("multiple_choice")
	, TEXT("text");

    private final String value;

    private QuestionType(final String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
