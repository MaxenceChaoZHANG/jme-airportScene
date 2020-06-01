package atmosphere;

@FunctionalInterface
public interface FloatValueListener {
    void valueChanged(float oldValue, float newValue);
}
