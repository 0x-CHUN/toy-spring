package spring.beans;

public enum ScopeType {
    DEFAULT,
    SINGLETON,
    PROTOTYPE;

    public static ScopeType getType(String scopeType) {
        switch (scopeType) {
            case "singleton":
                return SINGLETON;
            case "prototype":
                return PROTOTYPE;
            default:
                return DEFAULT;
        }
    }
}
