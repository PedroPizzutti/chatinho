package br.com.pizzutti.chatinho.api.domain.message;

public enum MessageEnum {
    UNKNOWN("UNKNOWN"),
    LOG_IN("LOG_IN"),
    LOG_OUT("LOG_OUT"),
    MSG("MSG");

    private final String value;

    MessageEnum(String value) {
        this.value = value;
    }

    public String asString() {
        return this.value;
    }

    public static MessageEnum fromString(String value) {
        for (MessageEnum e : MessageEnum.values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        return MessageEnum.UNKNOWN;
    }
}
