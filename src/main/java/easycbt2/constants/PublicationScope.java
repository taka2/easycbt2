package easycbt2.constants;

public enum PublicationScope {
	PUBLIC("public")
	, PRIVATE("private");

    private final String value;

    private PublicationScope(final String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }
}
