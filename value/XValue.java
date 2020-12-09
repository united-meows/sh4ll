package sh4ll.value;

public class XValue<X> {

    private X value;

    public XValue() {}

    public XValue setValue(X _newValue) {
        value = _newValue;
        return this;
    }

    public X getValue() {
        return value;
    }
}
