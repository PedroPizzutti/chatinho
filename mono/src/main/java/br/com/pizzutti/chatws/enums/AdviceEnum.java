package br.com.pizzutti.chatws.enums;

public enum AdviceEnum {
    UNKNOWN(1),
    BAD_REQUEST(400),
    INVALID_CREDENTIALS(401),
    NOT_ENOUGH_RIGHTS(403),
    SERVER_ERROR(500);

    private final Integer value;

    AdviceEnum(Integer value) {
        this.value = value;
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
