package br.com.pizzutti.chatinho.api.infra.config.communication;

public enum AdviceEnum {
    UNKNOWN(1),
    BAD_REQUEST(400),
    INVALID_CREDENTIALS(401),
    NOT_ENOUGH_RIGHTS(403),
    NOT_FOUND(404),
    SERVER_ERROR(500);

    private final Integer value;

    AdviceEnum(Integer value) {
        this.value = value;
    }

    public Integer toHttpStatus() {
        return this.value;
    }

    public static AdviceEnum fromHttpStatus(Integer httpStatus) {
        for (AdviceEnum advice : AdviceEnum.values()) {
            if (advice.value.equals(httpStatus)) {
                return advice;
            }
        }
        return AdviceEnum.UNKNOWN;
    }
}
